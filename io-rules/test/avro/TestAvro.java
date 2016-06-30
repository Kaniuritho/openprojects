package avro;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import org.junit.Assert;

public class TestAvro {
	
	@Test public void testAvro() throws IOException{
		Schema schema = new Schema.Parser().parse(TestAvro.class.getResourceAsStream("/avro/User.avsc"));
		
		GenericRecord user1 = new GenericData.Record(schema);
		
		user1.put("name", "Alyssa");
		user1.put("favorite_number", 256);
		// Leave favorite color null

		GenericRecord user2 = new GenericData.Record(schema);
		user2.put("name", "Ben");
		user2.put("favorite_number", 7);
		user2.put("favorite_color", "red");
		
		
		// Serialize user1 and user2 to disk
		File file = new File("Users.avro");
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		dataFileWriter.create(schema, file);
		dataFileWriter.append(user1);
		dataFileWriter.append(user2);
		dataFileWriter.close();
		
		
		
		// Deserialize users from disk
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
		GenericRecord user = null;
		while (dataFileReader.hasNext()) {
		// Reuse user object by passing it to next(). This saves us from
		// allocating and garbage collecting many objects for files with
		// many items.
		user = dataFileReader.next(user);
		System.out.println(user);
		}
	}
	
	
	@Test public void testAvroJsonConversion() throws IOException{
		Schema schema = new Schema.Parser().parse(TestAvro.class.getResourceAsStream("/avro/CustomMessage.avsc"));
		
		//create json
		GenericRecord mesg = new GenericData.Record(schema);		
		mesg.put("id", "device1");
		mesg.put("payload", "{'type':'internal json'}");
		

		System.out.println(mesg.toString());
		byte[] avro = AvroUtils.serializeJson(mesg.toString(), schema);
		
		///send byte array in mqtt
		
		///receive data
		String json = AvroUtils.avroToJson(avro, schema);
		
		System.out.println(json);
		
		//Assert.assertEquals(mesg.toString(), json);
		}
	
	
	@Test public void testAvroMapConversion() throws IOException{
		Schema schema = new Schema.Parser().parse(TestAvro.class.getResourceAsStream("/avro/CustomMessage.avsc"));
		
		//create json
		GenericRecord mesg = new GenericData.Record(schema);		
		mesg.put("id", "device1");
		mesg.put("payload", "{'type':'internal json'}");
		
		System.out.println(mesg.toString());
		byte[] avro = AvroUtils.serializeJson(mesg.toString(), schema);
		
		///send byte array in mqtt
		
		///receive data
		String data =  AvroUtils.avroToJson(avro, schema);
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = mapper.readValue(data, new TypeReference<Map<String, Object>>(){});
        
		Assert.assertEquals(map.get("payload"), mesg.get("payload"));
		Assert.assertEquals(map.get("id"), mesg.get("id"));
		}
	
	
	@Test public void testAvroJavaConversion() throws IOException{
		
		Schema schema = new Schema.Parser().parse(TestAvro.class.getResourceAsStream("/avro/CustomMessage.avsc"));
		GenericRecord mesg = new GenericData.Record(schema);		
		mesg.put("id", "device1");
		mesg.put("payload", "{'type':'internal json'}");
		System.out.println(mesg.toString());
		
		//create avro
		byte[] avro = AvroUtils.serializeJson(mesg.toString(), schema);
		
		GenericDatumReader<CustomMessage> reader = new SpecificDatumReader<CustomMessage>(schema);	        
		Decoder decoder = DecoderFactory.get().binaryDecoder(avro, null);
		CustomMessage msg1 = reader.read(null, decoder);
	    
		CustomMessage message = new CustomMessage();
		message.setId("device1");
		message.setPayload("{'type':'internal json'}");
		
		Assert.assertEquals(msg1, message);
		
	}
	
	@Test public void testAvroUtils() throws IOException{
		
		Schema schema = new Schema.Parser().parse(TestAvro.class.getResourceAsStream("/avro/CustomMessage.avsc"));
		GenericRecord mesg = new GenericData.Record(schema);		
		mesg.put("id", "device1");
		mesg.put("payload", "{'type':'internal json'}");
		
		//create avro
		byte[] avro = AvroUtils.serializeJson(mesg.toString(), schema);
		
		//avro to java
		CustomMessage msg1 = new AvroUtils<CustomMessage>().avroToJava(avro, schema);
		
		Assert.assertEquals(msg1.toString(),mesg.toString());
	}
	

}
