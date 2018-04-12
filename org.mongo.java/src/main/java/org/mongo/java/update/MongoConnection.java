package org.mongo.java.update;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

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

		List<Document> list = collection.find().into(new ArrayList<Document>());

		list.forEach(Helpers::printJsonResult);

		// Replacing a whole document
		collection.replaceOne(Filters.eq("x", 4), new Document("x", 24).append("updated", true));

		// updating a document using new Document()
		collection.updateOne(Filters.eq("x", 24),
				new Document("$set", new Document("x", 240).append("updated", true + "Again")));

		// updating a document using Updates
		collection.updateOne(Filters.eq("x", 240),
				Updates.combine(Updates.set("x", 2400), Updates.set("updated", false)));
		
		// updating a document using UpdatesOptions.upsert(true)
		collection.updateOne(Filters.eq("x", 11),
		Updates.combine(Updates.set("x", 2400), Updates.set("updated", false)), new UpdateOptions().upsert(true));

		System.out.println("@@@@@@@@@@@@@@@@@@@@@ After replacing @@@@@@@@@@@@@@@@@@@");

		List<Document> listAgainAfterReplacement = collection.find().into(new ArrayList<Document>());

		listAgainAfterReplacement.forEach(Helpers::printJsonResult);

		System.out.println("@@@@@@@@@@@@@@@@@@@@@ After updateOne with new Document() @@@@@@@@@@@@@@@@@@@");

		List<Document> listAgainAfterUpdateOne = collection.find().into(new ArrayList<Document>());

		listAgainAfterUpdateOne.forEach(Helpers::printJsonResult);

		System.out.println("@@@@@@@@@@@@@@@@@@@@@ After updateOne with Updates.combine(--,--) @@@@@@@@@@@@@@@@@@@");

		List<Document> listAgainAfterUpdate = collection.find().into(new ArrayList<Document>());

		listAgainAfterUpdate.forEach(Helpers::printJsonResult);
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@ After updateOne with Updates.combine(--,--) and upsert(true) @@@@@@@@@@@@@@@@@@@");

		List<Document> listAgainAfterUpsert = collection.find().into(new ArrayList<Document>());

		listAgainAfterUpsert.forEach(Helpers::printJsonResult);

		client.close();

	}
}
