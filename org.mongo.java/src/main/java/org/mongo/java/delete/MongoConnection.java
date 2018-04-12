package org.mongo.java.delete;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoConnection {

	public static void main(String[] args) {
		
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("node_students");
		MongoCollection<Document> collection = db.getCollection("students");

		collection.drop();

		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document("_id", i).append("x", i).append("y", true));
		}

		collection.deleteMany(Filters.gte("_id", 5));

		Set<Document> set = collection.find().into(new HashSet<Document>());

		set.forEach(Helpers::printJsonResult);

		collection.deleteOne(Filters.eq("_id", 4));

		Set<Document> set1 = collection.find().into(new HashSet<Document>());

		set1.forEach(Helpers::printJsonResult);

		client.close();

	}
}
