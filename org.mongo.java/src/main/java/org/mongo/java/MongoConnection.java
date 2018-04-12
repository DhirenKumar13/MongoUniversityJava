package org.mongo.java;

import org.bson.Document;
import static org.mongo.java.Helpers.printJsonResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

	public static void main(String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10).build();
		MongoClient client = new MongoClient(new ServerAddress(),options);
		MongoDatabase db = client.getDatabase("node_students");
		MongoCollection<Document> collection = db.getCollection("students");
		
		collection.drop();
		
		Document student = new Document("name","Dhiren")
				.append("age", 24)
				.append("salary", 49756.0);
		
		printJsonResult(student);
		
		collection.insertOne(student);
		
		printJsonResult(student);
		
		client.close();
		
	}
}
