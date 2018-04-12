package org.mongo.java.find.filter.limit.skip;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static org.mongo.java.Helpers.printJsonResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

public class MongoConnection {

	public static void main(String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10).build();
		MongoClient client = new MongoClient(new ServerAddress(),options);
		MongoDatabase db = client.getDatabase("node_students");
		MongoCollection<Document> collection = db.getCollection("students");
		
		collection.drop();
		
		for(int i = 0;i<10;i++) {
			collection.insertOne(new Document("x", new Random().nextInt(2))
					.append("y", new Random().nextInt(100)));
		}
		
		//Simple filter
		Bson simpleFilter = eq("x",0);
		
		//Complex Filter
		Bson complexFilter = and(eq("x",0),gt("y",30),lt("y",50));
		
		//Projection to exclude the fields
		Bson simpleProjection = Projections.exclude("x");
		
		//Projection to include the fields
		Bson complexProjection = Projections.fields(Projections.include("y"),Projections.exclude("_id"));
		
		//Projection to include the fields
		//Bson complexProjection1 = Projections.fields(Projections.include("y"),Projections.excludeId());
		
		//retrieving first document with filter applied
		Document firstElementSimple = collection.find(simpleFilter)
				.projection(simpleProjection).first();
		printJsonResult(firstElementSimple);
		
		//retrieving first document with filter applied
		Document firstElementComplex = collection.find(complexFilter)
				.projection(complexProjection).first();
		printJsonResult(firstElementComplex);
		
		//retrieving whole document onto an array with filter applied
		List<Document> wholelistSimple = collection.find(simpleFilter)
				.projection(simpleProjection).into(new ArrayList<Document>());
		wholelistSimple.forEach(document -> printJsonResult(document));
		
		//retrieving whole document onto an array with filter applied
		List<Document> wholelistComplex = collection.find(complexFilter)
				.projection(complexProjection).into(new ArrayList<Document>());
		wholelistComplex.forEach(document -> printJsonResult(document));
		
		System.out.println("Total count matched to the simple filtered criteria is : "+wholelistSimple.size());
		System.out.println("Total count matched to the complex filtered criteria is : "+wholelistComplex.size());
		
		client.close();
		
	}
}
