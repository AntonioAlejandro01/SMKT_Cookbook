package com.antonioalejandro.smkt.cookbook.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.cookbook.model.TokenData;
import com.antonioalejandro.smkt.cookbook.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Token Service Implementation
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see TokenService
 * @see TokenData
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
	/** The discovery client. */
	@Autowired
	private DiscoveryClient discoveryClient;

	@Value("${id_oauth_instance}")
	private String idOauthInstance;

	@Value("${oauthBasicAuth}")
	private String oauthBasicAuth;

	/** The Constant TEMPLATE_REQUEST. */
	private static final String REQUEST_KEY = "token";

	/** The Constant TEMPLATE_URL. */
	private static final String TEMPLATE_URL = "http://%s:%s/oauth/check_token";

	/** The Constant HEADER_AUTH. */
	private static final String HEADER_AUTH = "Authorization";

	private static final String BASIC_AUTH_TEMPLATE = "Basic %s";

	/** {@inheritDoc} */
	@Override
	public Optional<String> getUserId(String token) {
		var client = new OkHttpClient();
		RequestBody body = new FormBody.Builder().add(REQUEST_KEY, token).build();
		Request req = new Request.Builder().url(getUrl()).post(body)
				.addHeader(HEADER_AUTH, String.format(BASIC_AUTH_TEMPLATE, oauthBasicAuth)).build();
		try (var response = client.newCall(req).execute()) {
			if (response.code() != HttpStatus.OK.value()) {
				log.info("response code for /check_token is not 200. CODE: {}", response.code());
				return Optional.empty();
			}

			Optional<TokenData> tokenData = parseJson(response.body().string());

			return Optional.ofNullable(tokenData.map(TokenData::getUsername).orElse(null));

		} catch (IOException e) {
			log.error("Error in /check_token request");
			log.debug("{}", e);
			return Optional.empty();
		}
	}

	/**
	 * Get the URL from Eureka Discovery Service
	 *
	 * @return the url {@link String}
	 */
	private String getUrl() {
		ServiceInstance instanceInfo = discoveryClient.getInstances(idOauthInstance).get(0);
		return String.format(TEMPLATE_URL, instanceInfo.getHost(), instanceInfo.getPort());
	}

	/**
	 * Parse the JSON to {@link TokenData}
	 *
	 * @param json the json
	 * @return the optional
	 */
	private Optional<TokenData> parseJson(String json) {
		try {
			var mapper = new ObjectMapper();
			TokenData data = mapper.readValue(json, TokenData.class);
			log.debug("JSON parsed. VALUE: {}", data);
			return Optional.of(data);
		} catch (Exception e) {
			log.error("Error parsing JSON into TokenData class");
			log.debug("{}", e);
			return Optional.empty();
		}

	}
}