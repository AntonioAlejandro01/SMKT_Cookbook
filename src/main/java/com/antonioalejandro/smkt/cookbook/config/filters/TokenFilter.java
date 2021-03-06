package com.antonioalejandro.smkt.cookbook.config.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.cookbook.service.TokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TokenFilter.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see Filter
 * @apiNote Filter that verify the token and insert into request the field
 *          {@code userId} and her value is the userId in the token
 */
@Component
@Order(1)
@Slf4j
public class TokenFilter implements Filter {

	/** The token service. */
	@Autowired
	private TokenService tokenService;

	/** The Constant SECURE_ENDPOINT. */
	private static final String SECURE_ENDPOINT = "/recipes/";

	/**
	 * Do filter.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param chain    the chain
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse myResponse = (HttpServletResponse) response;
		if (httpRequest.getRequestURI().startsWith(SECURE_ENDPOINT)) {

			Optional<String> token = Optional.ofNullable(httpRequest.getHeader("Authorization"))
					.filter(tok -> tok.contains("Bearer ") && tok.length() > 8).map(tok -> tok.split(" ")[1]);

			if (token.isEmpty()) {
				log.info("Request without token or token format not valid");
				myResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
				return;
			}

			var requestWrapper = new HeaderRequestWrapper(httpRequest);
			Optional<String> userId = tokenService.getUserId(token.get());
			if (userId.isEmpty()) {
				log.info("Token is not valid");
				myResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
				return;
			}
			requestWrapper.addHeader("userID", userId.get());
			chain.doFilter(requestWrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * The Class HeaderRequestWrapper.
	 */
	public class HeaderRequestWrapper extends HttpServletRequestWrapper {

		/**
		 * construct a wrapper for this request.
		 *
		 * @param request the request
		 */
		public HeaderRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		/** The header map. */
		private Map<String, String> headerMap = new HashMap<>();

		/**
		 * add a header with given name and value.
		 *
		 * @param name  the name
		 * @param value the value
		 */
		public void addHeader(String name, String value) {
			headerMap.put(name, value);
		}

		/**
		 * Gets the header.
		 *
		 * @param name the name
		 * @return the header
		 */
		@Override
		public String getHeader(String name) {
			String headerValue = super.getHeader(name);
			if (headerMap.containsKey(name)) {
				headerValue = headerMap.get(name);
			}
			return headerValue;
		}

		/**
		 * get the Header names.
		 *
		 * @return the header names
		 */
		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			for (String name : headerMap.keySet()) {
				names.add(name);
			}
			return Collections.enumeration(names);
		}

		/**
		 * Gets the headers.
		 *
		 * @param name the name
		 * @return the headers
		 */
		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> values = Collections.list(super.getHeaders(name));
			if (headerMap.containsKey(name)) {
				values.add(headerMap.get(name));
			}
			return Collections.enumeration(values);
		}

	}

}
