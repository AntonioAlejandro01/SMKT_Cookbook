package com.antonioalejandro.smkt.cookbook.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.cookbook.db.CookbookDatabase;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.enums.FilterEnum;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;
import com.antonioalejandro.smkt.cookbook.service.RecipeService;
import com.antonioalejandro.smkt.cookbook.utils.Mappers;
import com.antonioalejandro.smkt.cookbook.utils.UUIDGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The Class Recipe Service Implementation.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * 
 */
@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService, Mappers, UUIDGenerator {

	private static final String TEMPLATE_URL = "http://%s:%s/pdf/cookbook";
	private static final String ENDPOINT_ID = "/recipe";

	@Autowired
	private CookbookDatabase db;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Value("${id_files_instance}")
	private String idFileInstance;

	@Override
	public Optional<List<Recipe>> findAll(String userId) throws CookbookException {
		log.info("--> RecipeServiceImpl findAll userId: {}", userId);
		return db.all(userId);
	}

	@Override
	public Optional<Recipe> findById(String userId, String id) throws CookbookException {
		log.info("--> RecipeServiceImpl  findById userId: {}, id: {}", userId, id);
		return db.byId(userId, id);
	}

	@Override
	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames)
			throws CookbookException {
		log.info("--> RecipeServiceImpl  findByIngredients userId: {}, ingredients: {}", userId, ingredientsNames);
		return db.byIngredients(userId, ingredientsNames);
	}

	@Override
	public Optional<List<Recipe>> findByFilter(String userId, String filter, String value) throws CookbookException {
		log.info(" --> RecipeServiceImpl  findbyFilter userId: {}, filter: {}, value: {}", userId, filter, value);
		Optional<FilterEnum> filterEnum = FilterEnum.fromName(filter);
		if (filterEnum.isEmpty()) {
			throw new CookbookException(HttpStatus.BAD_REQUEST, "the filter is not valid");
		}

		return filterEnum.get().getFunctionSearch().search(userId, value, db);
	}

	@Override
	public Optional<byte[]> getPdf(String userId, Optional<String> id, String token) throws CookbookException {
		log.info("--> RecipeServiceImpl  getPdf userId: {}, haveId?:{}, id:{}", userId, !id.isEmpty(), id);
		String bodyContent = id.isPresent() ? this.recipeToJSON(userId, id.get()) : this.recipesToJSON(userId);
		var body = RequestBody.create(bodyContent,
				MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
		var url = String.format("%s%s", getUrl(), id.isPresent() ? ENDPOINT_ID : "");
		log.info("REQUEST_URL: {} - REQUEST_BODY: {}", url, bodyContent);
		Request req = new Request.Builder().url(url).post(body).addHeader(HttpHeaders.AUTHORIZATION, token).build();

		try (var response = new OkHttpClient().newCall(req).execute()) {
			log.info("Response finished");
			if (response.code() == HttpStatus.OK.value()) {
				return Optional.of(response.body().byteStream().readAllBytes());
			} else if (response.code() == HttpStatus.UNAUTHORIZED.value()
					|| response.code() == HttpStatus.BAD_REQUEST.value()) {
				log.warn("ERROR CODE :{}", response.code());
				throw new CookbookException(HttpStatus.UNAUTHORIZED, "You can't do this operation or token is expired");
			} else {
				log.error("ERROR CODE :{}", response.code());
				var status = HttpStatus.valueOf(response.code());
				throw new CookbookException(status, status.getReasonPhrase());
			}

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			log.error("ERROR HTTP CALL ERROR MSG: {}", e.getMessage());
			throw new CookbookException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Optional<Recipe> createRecipe(String userId, RecipeDTO recipe) throws CookbookException {
		log.info("--> RecipeServiceImpl createRecipe userId: {}, recipe: {}", userId, recipe);

		var recipeToInsert = this.dtoToRecipe(recipe);

		recipeToInsert.setId(this.generateUUID());
		recipeToInsert.setUserId(userId);

		log.debug("--> RecipeServiceImpl createRecipe generated UUID: {}", recipeToInsert.getId());

		return db.insert(recipeToInsert);
	}

	@Override
	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws CookbookException {
		log.info("--> RecipeServiceImpl updateRecipe userId: {}, id: {}, recipe: {}", userId, id, recipe);
		return db.update(userId, id, this.dtoToRecipe(recipe));
	}

	@Override
	public void deleteRecipe(String userId, String id) throws CookbookException {
		log.info("--> RecipeServiceImpl deleteRecipe: {} userId: {}, id: {}", userId, id);
		if (!db.delete(userId, id)) {
			log.warn("The recipe can't be deleted");
			throw new CookbookException(HttpStatus.FORBIDDEN, "the id can't be deleted");
		}

	}

	private String recipeToJSON(String userId, String id) throws CookbookException {
		Optional<Recipe> recipe = findById(userId, id);
		if (recipe.isEmpty()) {
			throw new CookbookException(HttpStatus.NOT_FOUND, "the id is not valid");
		}
		try {
			return new ObjectMapper().writeValueAsString(recipe.get());
		} catch (Exception e) {
			throw new CookbookException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not convert recipe to JSON");
		}
	}

	private String recipesToJSON(String userId) throws CookbookException {
		Optional<List<Recipe>> recipes = findAll(userId);
		if (recipes.isEmpty() || recipes.get().isEmpty()) {
			throw new CookbookException(HttpStatus.NOT_FOUND, "the user haven't got any recipes");
		}
		try {
			return new ObjectMapper().writeValueAsString(recipes.get());
		} catch (Exception e) {
			throw new CookbookException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not convert recipe to JSON");
		}

	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	private String getUrl() throws CookbookException {
		List<ServiceInstance> services = discoveryClient.getInstances(idFileInstance);
		if (services.isEmpty()) {
			throw new CookbookException(HttpStatus.SERVICE_UNAVAILABLE,
					"the service for files is unavailable now, try later O.o!");
		}
		ServiceInstance instanceInfo = services.get(0);
		return String.format(TEMPLATE_URL, instanceInfo.getHost(), instanceInfo.getPort());
	}

}
