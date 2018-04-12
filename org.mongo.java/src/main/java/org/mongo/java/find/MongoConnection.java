package org.mongo.java.find;

import static org.mongo.java.Helpers.printJsonResult;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

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
		
		for(int i = 0;i<10;i++) {
			collection.insertOne(new Document("x",i));
		}
		
		//retrieving first document
		Document firstElement = collection.find().first();
		printJsonResult(firstElement);
		
		//retrieving whole document onto an array
		List<Document> wholelist = collection.find().into(new ArrayList<Document>());
		wholelist.forEach(document -> printJsonResult(document));
		
		client.close();
		
	}
}
