package org.mongo.java.update;

import java.io.StringWriter;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

public class Helpers {
	
	public static void printJsonResult(Document document) {
		
		@SuppressWarnings("deprecation")
		JsonWriter writer = new JsonWriter(new StringWriter(),
				new JsonWriterSettings(JsonMode.SHELL,true));
		
		new DocumentCodec().encode(writer, document,
				EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		
		System.out.println(writer.getWriter());
		
		System.out.flush();
		
	}
}
