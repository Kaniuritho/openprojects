package io.parser.avro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.avro.Schema;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class AvroParser {

    private final ObjectMapper mapper = new ObjectMapper();
    private Schema schema = null;

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
    
    /**
     * 
     * @param avroSchemaFile Avro Schema as File.
     * @throws IOException
     */
    public void setSchema(File avroSchemaFile) throws IOException  {
    	this.schema = new Schema.Parser().parse(avroSchemaFile);
    }

    /**
     * 
     * @param avroSchemaString Avro Schema as String.
     * @throws IOException
     */
    public void setSchema(String avroSchemaString)  {
    	this.schema = new Schema.Parser().parse(avroSchemaString);
    }
    
    /**
     * 
     * @param avroSchemaStream Avro Schema as InputStream.
     * @throws IOException
     */
    public void setSchema(InputStream avroSchemaStream) throws IOException  {
    	this.schema = new Schema.Parser().parse(avroSchemaStream);
    }
    
    public Schema schema() {
        return schema;
    }

    public Map<String, Object> parse(byte[] avrodata) throws Exception {
        try {
        	
        	String data =  AvroUtils.avroToJson(avrodata, schema);
            return mapper.readValue(data, new TypeReference<Map<String, Object>>(){});
            
        } catch (IOException e) {
            throw new Exception("Error trying to parse data.", e);
        }
    }
    
    public Map<String, Object> parse(byte[] avrodata, Schema schema) throws Exception {
       setSchema(schema);
       return parse(avrodata);
    }
    
    
}
