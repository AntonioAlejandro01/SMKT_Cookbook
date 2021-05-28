package com.antonioalejandro.smkt.cookbook.service;

import java.util.Optional;

/**
 * The Interface TokenService.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 */
public interface TokenService {

	/**
	 * Gets the user id.
	 *
	 * @param token {@link String}
	 * @return the user id {@link String}
	 */
	public Optional<String> getUserId(String token);
}
