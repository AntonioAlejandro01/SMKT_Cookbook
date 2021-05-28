package com.antonioalejandro.smkt.cookbook.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.enums.FilterEnum;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;
import com.antonioalejandro.smkt.cookbook.service.RecipeService;
import com.antonioalejandro.smkt.cookbook.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Cookbook Controller Class
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("recipes")
@Api(value = "/recipes", tags = { "Recipes" })
public class CookbookController {

	@Autowired
	private RecipeService service;

	@Autowired
	private Validations validations;

	/**
	 * All.
	 *
	 * @param userId the user id
	 * @return the response entity
	 * @throws CookbookException the error service
	 */
	@ApiOperation(value = "Get all Recipes for user in token", responseContainer = "List", httpMethod = "GET", notes = "Get all Recipes that the user in the token is owner")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Recipe.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized") })
	@GetMapping("all")
	public ResponseEntity<List<Recipe>> all(@RequestHeader(name = "userID", required = false) String userId)
			throws CookbookException {
		log.info("--> Cookbook Controller - GET - /recipes/all userID: {}", userId);
		return service.findAll(userId).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * By id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the response entity
	 * @throws CookbookException the error service
	 */
	@ApiOperation(value = "Get Recipe by id", httpMethod = "GET", notes = "Get a Recipe by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id for search", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 200, response = Recipe.class, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("/{id}")
	public ResponseEntity<Recipe> byId(@RequestHeader(name = "userID", required = false) final String userId,
			@PathVariable(name = "id", required = true) final String id) throws CookbookException {
		log.info("--> Cookbook Controller - GET - /recipes/{} userId: {}", id, userId);
		validations.id(id);
		return service.findById(userId, id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@ApiOperation(value = "Search a Recipe by a Ingredients", httpMethod = "GET", notes = "Get all Recipes that matches with list of ingredients names")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Recipe.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("search/ingredients")
	public ResponseEntity<List<Recipe>> byIngredients(@RequestHeader(name = "userID", required = false) String userId,
			@RequestParam(name = "names", required = true) List<String> names) throws CookbookException {
		log.info("--> Cookbook Controller - GET - /recipes/ingredients?names={} - userID: {}", names, userId);
		validations.listStrings("names", names);
		return service.findByIngredients(userId, names).map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}

	@ApiOperation(value = "Search a Recipe by a filter", httpMethod = "GET", notes = "Get all Recipes that matches with filter and value for this filter")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "filter", value = "The type of filter", required = true, type = "string", allowableValues = "TIME, TITLE", readOnly = true, paramType = "Path"),
			@ApiImplicitParam(name = "value", value = "the value for filter", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Recipe.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("search/{filter}/{value}")
	public ResponseEntity<List<Recipe>> byFilterAndValue(
			@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "filter", required = true) String filter,
			@PathVariable(name = "value", required = true) String value) throws CookbookException {
		log.info("--> Cookbook Controller - GET - /recipes/search/{}/{}  - userID: {}", filter, value, userId);
		validations.string("filter", filter);
		validations.string("value", value);
		return service.findByFilter(userId, filter, value).map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}

	@ApiOperation(value = "Get PDF File ", httpMethod = "GET", notes = "Get excel file for all products that the user in token have it", produces = MediaType.APPLICATION_PDF_VALUE)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> pdf(@RequestHeader(name = "userID", required = false) String userId,
			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestParam(name = "id", required = false) Optional<String> id) throws CookbookException {
		log.info("--> Cookbook Controller - GET - /recipes/pdf{} userID: {}",
				id.isEmpty() ? "" : String.format("?id=%s", id.get()), userId);
		validations.optionalId(id);
		return service.getPdf(userId, id, token).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
	}

	@ApiOperation(value = "Get all Filter", httpMethod = "GET", notes = "Get all Filters")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = HashMap.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	@GetMapping("filters")
	public ResponseEntity<List<Map<String, String>>> filters() {
		log.info("--> Cookbook Controller - GET - /recipes/filters");
		return ResponseEntity.ok(FilterEnum.toBeSended());
	}

	@ApiOperation(value = "Create a new Recipe", httpMethod = "POST", notes = "Create a new Recipe, the owner is the user in the token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = {
			@ApiResponse(code = 201, response = Recipe.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@PostMapping("/")
	public ResponseEntity<Recipe> createRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@RequestBody RecipeDTO recipe) throws CookbookException {
		log.info("--> Cookbook Controller - POST - /recipes/ - recipe: {}, userId: {}", recipe, userId);
		validations.recipe(recipe);
		return service.createRecipe(userId, recipe).map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body))
				.orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
	}

	@ApiOperation(value = "Update a Recipe by id", httpMethod = "PUT", notes = "Update a Recipe by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = {
			@ApiResponse(code = 202, response = Recipe.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = CookbookException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@PutMapping("/{id}")
	public ResponseEntity<Recipe> updateRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "id", required = true) String id, @RequestBody RecipeDTO recipe)
			throws CookbookException {
		log.info("--> Cookbook Controller - PUT - recipes/{}  - recipe:{} userID: {}", id, recipe, userId);
		validations.id(id);
		validations.recipe(recipe);
		return service.updateRecipe(userId, id, recipe).map(body -> ResponseEntity.accepted().body(body))
				.orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
	}

	@ApiOperation(value = "Delete a Recipe", httpMethod = "DELETE", notes = "Delete a Recipe by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 202, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 403, message = "Forbidden", response = CookbookException.JSONServiceError.class) })
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "id", required = true) String id) throws CookbookException {
		log.info("--> Cookbook Controller - DELETE - /recipes/{}  - userID: {}", id, userId);
		validations.id(id);
		service.deleteRecipe(userId, id);
		return ResponseEntity.accepted().build();
	}

	/**
	 * Handle error service.
	 *
	 * @param errorService the error service
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<String> handleErrorService(final CookbookException error) {
		log.error("HANDLE ERROR - STATUS: {} - MESSAGE: {} - TIMESTAMP: {}", error.getError().getStatus(),
				error.getError().getMessage(), error.getError().getTimestamp());
		return error.toResponse();
	}
}
