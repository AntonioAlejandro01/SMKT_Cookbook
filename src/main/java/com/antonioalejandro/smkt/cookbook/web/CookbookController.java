package com.antonioalejandro.smkt.cookbook.web;

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
import com.antonioalejandro.smkt.cookbook.utils.Utils;
import com.antonioalejandro.smkt.cookbook.utils.Validations;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

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
	@GetMapping("all")
	public ResponseEntity<List<Recipe>> all(@RequestHeader(name = "userID", required = false) String userId)
			throws CookbookException {
		log.info(Utils.formatLog(getClass(), "all", "userId"), userId);
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
	@GetMapping("/{id}")
	public ResponseEntity<Recipe> byId(@RequestHeader(name = "userID", required = false) final String userId,
			@PathVariable(name = "id", required = true) final String id) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "byId", "userId", "id"), userId, id);
		validations.id(id);
		return service.findById(userId, id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("search/ingredients")
	public ResponseEntity<List<Recipe>> byIngredients(@RequestHeader(name = "userID", required = false) String userId,
			@RequestParam(name = "names", required = true) List<String> names) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "byIngredients", "userId", "ingredients"), userId, names);
		validations.listStrings("names", names);
		return service.findByIngredients(userId, names).map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}

	@GetMapping("search/{filter}/{value}")
	public ResponseEntity<List<Recipe>> byFilterAndValue(
			@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "filter", required = true) String filter,
			@PathVariable(name = "value", required = true) String value) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "byFilterAndValue", "userId", "filter", "value"), userId, filter, value);
		validations.string("filter", filter);
		validations.string("value", value);
		return service.findByFilter(userId, filter, value).map(ResponseEntity::ok)
				.orElse(ResponseEntity.noContent().build());
	}

	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> pdf(@RequestHeader(name = "userID", required = false) String userId,
			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestParam(name = "id", required = false) Optional<String> id) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "getPdf", "userId", "Id?", "id"), userId, !id.isEmpty(), id);
		if (id.isPresent()) {
			validations.id(id.get());
		}
		return service.getPdf(userId, id, token).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
	}

	@GetMapping("filters")
	public ResponseEntity<List<Map<String, String>>> filters() {
		log.info(Utils.formatLogText(getClass(), "filters", ""));
		return ResponseEntity.ok(FilterEnum.toBeSended());
	}

	@PostMapping("/")
	public ResponseEntity<Recipe> createRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@RequestBody RecipeDTO recipe) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "createRecipe", "userId", "recipe"), userId, recipe);
		validations.recipe(recipe);
		return service.createRecipe(userId, recipe).map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body))
				.orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Recipe> updateRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "id", required = true) String id, @RequestBody RecipeDTO recipe)
			throws CookbookException {
		log.info(Utils.formatLog(getClass(), "updateRecipe", "userId", "id", "recipe"), userId, id, recipe);
		validations.id(id);
		validations.recipe(recipe);
		return service.updateRecipe(userId, id, recipe).map(body -> ResponseEntity.accepted().body(body))
				.orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteRecipe(@RequestHeader(name = "userID", required = false) String userId,
			@PathVariable(name = "id", required = true) String id) throws CookbookException {
		log.info(Utils.formatLog(getClass(), "deleteRecipe", "userId", "id"), userId, id);
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
		log.error(Utils.formatLog(getClass(), "ERROR", "STATUS", "TIME", "MESSAGE"), error.getError().getStatus(),
				error.getError().getTimestamp(), error.getError().getMessage());
		return error.toResponse();
	}
}
