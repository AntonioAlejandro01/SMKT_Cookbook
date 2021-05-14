package com.antonioalejandro.smkt.cookbook.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.utils.Mappers;
import com.antonioalejandro.smkt.cookbook.utils.UUIDGenerator;
import com.antonioalejandro.smkt.cookbook.utils.Utils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CookbookDatabaseImpl implements CookbookDatabase, Mappers, UUIDGenerator {

	/** the Collection for make operations */
	private MongoCollection<Document> collection;

	/** The create consumer for mongo iterator to list. */
	private Function<List<Recipe>, Consumer<Document>> createConsumerForMongoIteratorToList;

	/**
	 * Create a MongoDB instance.
	 */
	public CookbookDatabaseImpl(@Value("${mongodb.connection}") String connectionString,
			@Value("${mongodb.database.name}") String databaseName,
			@Value("${mongodb.database.collection}") String databaseCollection) {

		log.info(Utils.formatLogText(getClass(), "Constructor", "Create Database connection and try to connect"));
		MongoClient client = MongoClients.create(connectionString);
		MongoDatabase db = client.getDatabase(databaseName);

		log.info(Utils.formatLogText(getClass(), "Constructor", "Create or access to collection into database"));
		collection = db.getCollection(databaseCollection);

		// Functions
		this.createConsumerForMongoIteratorToList = products -> doc -> products.add(this.documentToRecipe(doc));
	}

	@Override
	public Optional<List<Recipe>> all(String userId) {
		log.info(Utils.formatLog(getClass(), "all", "userId"), userId);
		return evaluateList(findList(defaultDocument(userId)));
	}

	@Override
	public Optional<Recipe> byId(String userId, String id) {
		log.info(Utils.formatLog(getClass(), "byId", "userId", "id"), userId, id);
		return Optional.ofNullable(collection.find(defaultDocument(userId).append(KEY_ID, id)).first())
				.map(this::documentToRecipe);
	}

	@Override
	public Optional<List<Recipe>> byIngredients(String userId, List<String> ingredientsNames) {
		log.info(Utils.formatLog(getClass(), "byIngredients", "userId", "ingredients"), userId, ingredientsNames);
		List<Pattern> patterns = ingredientsNames.stream().map(name -> Pattern.compile(name, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList());
		return evaluateList(findList(defaultDocument(userId).append(String.format("%s.%s", INGREDIENTS,NAME), new Document("$in", patterns))));
	}

	@Override
	public Optional<List<Recipe>> byTime(String userId, double time) {
		log.info(Utils.formatLog(getClass(), "byTime", "userId", "time"), userId, time);
		return evaluateList(findList(defaultDocument(userId).append(TIME, new Document("$lte", time))));
	}

	@Override
	public Optional<List<Recipe>> byTitle(String userId, String title) {
		log.info(Utils.formatLog(getClass(), "byTitle", "userId", "title"), userId, title);
		return evaluateList(
				findList(defaultDocument(userId).append(TITLE, new Document("$regex", title).append("$options", "i"))));
	}

	@Override
	public Optional<Recipe> insert(Recipe recipe) {
		log.info(Utils.formatLog(getClass(), "insert", "recipe"), recipe);
		InsertOneResult result = collection.insertOne(this.recipeToDocument(recipe));

		if (!result.wasAcknowledged()) {

			log.warn(Utils.formatLogText(getClass(), "insertRecipe", "The Recipe hasn't been inserted"));

			return Optional.empty();
		}

		log.debug(Utils.formatLogText(getClass(), "insert", "id inserted: {}"),
				result.getInsertedId().asString().getValue());

		return this.byId(recipe.getUserId(), result.getInsertedId().asString().getValue());
	}

	@Override
	public Optional<Recipe> update(String userId, String id, Recipe recipe) {
		log.info(Utils.formatLog(getClass(), "update", "userId", "id", "recipe"), userId, id, recipe);
		Document update = this.recipeToDocument(recipe);
		update.remove(KEY_ID);
		update.remove(USER_ID);
		UpdateResult result = collection.updateOne(defaultDocument(userId).append(KEY_ID, id), new Document("$set",update));

		if (result.getModifiedCount() == 0) {
			log.warn(Utils.formatLogText(getClass(), "update", "The recipe can't be updated"));
			return Optional.empty();
		}

		return this.byId(userId, id);

	}

	@Override
	public boolean delete(String userId, String id) {
		log.info(Utils.formatLog(getClass(), "delete", "userId", "id"), userId, id);
		return collection.deleteOne(defaultDocument(userId).append(KEY_ID, id)).getDeletedCount() > 0;
	}

	/**
	 * Find products by query passed as parameter
	 * 
	 * @param doc {@link Document} query to search products
	 * @return {@link List}&lt;{@link Product}&gt;
	 */
	private List<Recipe> findList(Document doc) {
		log.debug("findList----- QUERY: {}", doc.toJson());
		List<Recipe> products = new ArrayList<>();
		collection.find(doc).forEach(this.createConsumerForMongoIteratorToList.apply(products));
		return products;
	}

	/**
	 * Return a appropriate Optional depends if the list is empty or not
	 * 
	 * @param products {@link List}&lt;{@link Product}&gt;
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;. if the
	 *         the list is empty return {@code Optional.empty()} otherwise return a
	 *         {@link Optional} from list passed as parameter
	 */
	private Optional<List<Recipe>> evaluateList(List<Recipe> products) {
		return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}

	/**
	 * Create a Default document query with userId
	 * 
	 * @param userId {@link String}
	 * @return {@link Document} with one value appended key:
	 *         <code>{@link USER_ID}</code>, value: {@code userId}
	 */
	private Document defaultDocument(String userId) {
		return new Document().append(USER_ID, userId);
	}

}
