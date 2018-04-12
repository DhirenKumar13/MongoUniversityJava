package org.mongo.java.find.filter.projection.javadriver;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static org.mongo.java.Helpers.printJsonResult;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

	public static void main(String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("node_students");
		MongoCollection<Document> collection = db.getCollection("students");

		collection.drop();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				collection.insertOne(new Document("x", i).append("y", j));
			}
		}

		// Projection to include the fields
		Bson complexProjection = fields(include("y", "x"), excludeId());

		// Simple sort technique using new Document()
		Bson sort = new Document("x", 1).append("y", -1);

		// Simple sort technique using new Document()
		Bson complexSort = orderBy(ascending("x"), descending("y"));

		// retrieving whole document onto an array with filter applied
		List<Document> wholelist = collection.find().sort(sort).projection(complexProjection)
				.into(new ArrayList<Document>());

		wholelist.forEach(document -> printJsonResult(document));

		// retrieving whole document onto an array with filter applied
		List<Document> wholelistAgain = collection.find().sort(complexSort).projection(complexProjection)
				.into(new ArrayList<Document>());

		wholelistAgain.forEach(document -> printJsonResult(document));

		System.out.println("Total count matched to the simple filtered criteria is : " + wholelist.size());

		client.close();

	}
}
