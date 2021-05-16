package com.antonioalejandro.smkt.cookbook.db;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

class CookbookDatabaseTest {

	private CookbookDatabaseImpl db;

	private MongoCollection<Document> collection;

	@BeforeEach
	void init() {
		collection = mock(MongoCollection.class);
	}

	@Test
	void testContructorException() throws Exception {
		assertThrows(Exception.class, () -> new CookbookDatabaseImpl("", "", ""));
	}

	@Test
	void testfindAll() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		Document doc1 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 1")
				.append("time", 1.23d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));
		Document doc2 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 2")
				.append("time", 10.5d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test 2").append("amount", "amount test 2")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn(doc1).thenReturn(doc2);

		Optional<List<Recipe>> recipes = db.all("Admin");

		assertTrue(recipes.isPresent());
		assertFalse(recipes.get().isEmpty());

		List<Recipe> recips = recipes.get();

		assertEquals(2, recips.size());
		assertEquals("Recipe Test 1", recips.get(0).getTitle());
		assertEquals("Recipe Test 2", recips.get(1).getTitle());
	}

	@Test
	void testfindAllEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(false);

		Optional<List<Recipe>> recipes = db.all("Admin");

		assertTrue(recipes.isEmpty());

	}

	@Test
	void testfindbyId() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);

		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		String id = UUID.randomUUID().toString();
		Document doc1 = new Document("_id", id).append("title", "Recipe Test 1").append("time", 1.23d)
				.append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(doc1);

		Optional<Recipe> recipe = db.byId("Admin", id);

		assertTrue(recipe.isPresent());

		Recipe r = recipe.get();

		assertEquals("Recipe Test 1", r.getTitle());
		assertEquals(id, r.getId());
	}

	@Test
	void testfindByIdEmpty() throws Exception {

		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);

		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(null);

		Optional<Recipe> recipes = db.byId("Admin", "AS123");

		assertTrue(recipes.isEmpty());

	}

	@Test
	void testfindByIngredients() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		Document doc1 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 1")
				.append("time", 1.23d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));
		Document doc2 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 2")
				.append("time", 10.5d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test 2").append("amount", "amount test 2")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn(doc1).thenReturn(doc2);

		Optional<List<Recipe>> recipes = db.byIngredients("Admin", Arrays.asList("ing"));

		assertTrue(recipes.isPresent());
		assertFalse(recipes.get().isEmpty());

		List<Recipe> recips = recipes.get();

		assertEquals(2, recips.size());
		assertEquals("Recipe Test 1", recips.get(0).getTitle());
		assertEquals("Recipe Test 2", recips.get(1).getTitle());
	}

	@Test
	void testfindbyIngredientsEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(false);

		Optional<List<Recipe>> recipes = db.byIngredients("Admin", Arrays.asList("X"));

		assertTrue(recipes.isEmpty());

	}

	@Test
	void testfindByTime() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		Document doc1 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 1")
				.append("time", 1.23d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));
		Document doc2 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 2")
				.append("time", 10.5d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test 2").append("amount", "amount test 2")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn(doc1).thenReturn(doc2);

		Optional<List<Recipe>> recipes = db.byTime("Admin", 2.3d);

		assertTrue(recipes.isPresent());
		assertFalse(recipes.get().isEmpty());

		List<Recipe> recips = recipes.get();

		assertEquals(2, recips.size());
		assertEquals("Recipe Test 1", recips.get(0).getTitle());
		assertEquals("Recipe Test 2", recips.get(1).getTitle());
	}

	@Test
	void testfindbyTimeEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(false);

		Optional<List<Recipe>> recipes = db.byTime("Admin", 2.3d);

		assertTrue(recipes.isEmpty());

	}

	@Test
	void testfindByTitle() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		Document doc1 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 1")
				.append("time", 1.23d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));
		Document doc2 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 2")
				.append("time", 10.5d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test 2").append("amount", "amount test 2")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn(doc1).thenReturn(doc2);

		Optional<List<Recipe>> recipes = db.byTitle("Admin", "TEST");

		assertTrue(recipes.isPresent());
		assertFalse(recipes.get().isEmpty());

		List<Recipe> recips = recipes.get();

		assertEquals(2, recips.size());
		assertEquals("Recipe Test 1", recips.get(0).getTitle());
		assertEquals("Recipe Test 2", recips.get(1).getTitle());
	}

	@Test
	void testfindByTitleEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(false);

		Optional<List<Recipe>> recipes = db.byTitle("Admin", "X");

		assertTrue(recipes.isEmpty());

	}

	@Test
	void testinsert() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		InsertOneResult result = mock(InsertOneResult.class);

		Document doc1 = new Document("_id", UUID.randomUUID().toString()).append("title", "Recipe Test 1")
				.append("time", 1.23d).append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		when(collection.insertOne(Mockito.any())).thenReturn(result);
		when(iterable.first()).thenReturn(doc1);
		when(result.wasAcknowledged()).thenReturn(Boolean.TRUE);
		BsonValue value = mock(BsonValue.class);
		when(value.asString()).thenReturn(new BsonString("ID"));
		when(result.getInsertedId()).thenReturn(value);
		Recipe recipe = new Recipe();
		recipe.setId(UUID.randomUUID().toString());
		recipe.setIngredients(List.of());
		recipe.setSteps(Arrays.asList("STep 1"));
		recipe.setTime(2.3d);
		recipe.setTitle("Recipe Test 1");
		recipe.setUserId("Admin");

		Optional<Recipe> r = db.insert(recipe);

		assertTrue(r.isPresent());

		Recipe recip = r.get();

		assertEquals("Recipe Test 1", recip.getTitle());
	}

	@Test
	void testInsertEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		InsertOneResult result = mock(InsertOneResult.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		when(collection.insertOne(Mockito.any())).thenReturn(result);
		when(result.wasAcknowledged()).thenReturn(Boolean.FALSE);

		Recipe recipe = new Recipe();
		recipe.setId(UUID.randomUUID().toString());
		recipe.setIngredients(List.of());
		recipe.setSteps(Arrays.asList("STep 1"));
		recipe.setTime(2.3d);
		recipe.setTitle("Recipe Test 1");
		recipe.setUserId("Admin");

		Optional<Recipe> r = db.insert(recipe);

		assertTrue(r.isEmpty());

	}

	@Test
	void testUpdate() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		UpdateResult result = mock(UpdateResult.class);

		var id = UUID.randomUUID().toString();
		Document doc1 = new Document("_id", id).append("title", "Recipe Test 1").append("time", 1.23d)
				.append("steps", Arrays.asList("Step 1", "Step 2")).append("ingredients",
						Arrays.asList(new Document("name", "ingredient test").append("amount", "amount test")));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);
		when(iterable.first()).thenReturn(doc1);
		when(result.wasAcknowledged()).thenReturn(Boolean.TRUE);
		BsonValue value = mock(BsonValue.class);
		when(value.asString()).thenReturn(new BsonString("ID"));
		when(result.getModifiedCount()).thenReturn(1L);
		Recipe recipe = new Recipe();
		recipe.setId(UUID.randomUUID().toString());
		recipe.setIngredients(List.of());
		recipe.setSteps(Arrays.asList("Step 1"));
		recipe.setTime(2.3d);
		recipe.setTitle("Recipe Test 1");
		recipe.setUserId("Admin");

		Optional<Recipe> r = db.update("Admin", id, recipe);

		assertTrue(r.isPresent());

		Recipe recip = r.get();

		assertEquals("Recipe Test 1", recip.getTitle());
	}

	@Test
	void testUpdateEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		UpdateResult result = mock(UpdateResult.class);

		var id = UUID.randomUUID().toString();

		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		when(result.getModifiedCount()).thenReturn(0L);

		Recipe recipe = new Recipe();
		recipe.setId(UUID.randomUUID().toString());
		recipe.setIngredients(List.of());
		recipe.setSteps(Arrays.asList("Step 1"));
		recipe.setTime(2.3d);
		recipe.setTitle("Recipe Test 1");
		recipe.setUserId("Admin");

		Optional<Recipe> r = db.update("Admin", id, recipe);

		assertTrue(r.isEmpty());

	}

	@Test
	void testDelete() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		DeleteResult result = mock(DeleteResult.class);
		when(result.getDeletedCount()).thenReturn(1L);
		when(collection.deleteOne(Mockito.any(Document.class))).thenReturn(result);
		assertTrue(db.delete("Admin", UUID.randomUUID().toString()));

	}

	@Test
	void testDeleteEmpty() throws Exception {
		db = new CookbookDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		MongoCursor<Document> cursor = mock(MongoCursor.class);
		DeleteResult result = mock(DeleteResult.class);
		when(result.getDeletedCount()).thenReturn(0L);
		when(collection.deleteOne(Mockito.any(Document.class))).thenReturn(result);
		assertFalse(db.delete("Admin", UUID.randomUUID().toString()));

	}
}
