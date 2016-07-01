

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This producer will send a bunch of messages to topic "fast-messages". Every so often,
 * it will send a message to "slow-messages". This shows how messages can be sent to
 * multiple topics. On the receiving end, we will see both kinds of messages but will
 * also see how the two topics aren't really synchronized.
 */
public class TestKafkaProducerRegistryCall {
    public static void main(String[] args) throws IOException {
        // set up the producer
        //KafkaProducer<String, String> producer = null;
      
        	InputStream props = Resources.getResource("producer.props").openStream();
            Properties properties = new Properties();
            properties.load(props);
            KafkaProducer<String, String> producer = new KafkaProducer<String,String>(properties);
      
            String topic = properties.getProperty("topic.id");//"testexternaltopic";
        try {
            for (int i = 0; i < 100000; i++) {
                // send lots of messages
                producer.send(new ProducerRecord<String, String>(
                		topic,
                        String.format("{\"type\":\"fast\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));

                // every so often send to a different topic
                if (i % 1000 == 0) {
                    producer.send(new ProducerRecord<String, String>(
                    		topic,
                            String.format("{\"type\":\"request\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                    producer.send(new ProducerRecord<String, String>(
                    		topic,
                            String.format("{\"type\":\"log\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                    producer.send(new ProducerRecord<String, String>(
                    		topic,
                            String.format("{\"type\":\"notice\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                    producer.send(new ProducerRecord<String, String>(
                    		topic,
                            String.format("{\"type\":\"response\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
                   
                    producer.flush();
                    
                    System.out.println("Sent msg number " + i);
                }
            }
        } catch (Throwable throwable) {
            System.out.printf("%s", throwable.getStackTrace());
        } finally {
            producer.close();
        }

    }
    
    @Test 
    public void makeCallToRegistry() throws IOException{
    	InputStream props = Resources.getResource("producer.props").openStream();
        Properties properties = new Properties();
        properties.load(props);
        KafkaProducer<String, String> producer = new KafkaProducer<String,String>(properties);
  
        String topic = properties.getProperty("topic.id");//"testexternaltopic";
        
        producer.send(new ProducerRecord<String, String>(
        		topic,
                String.format("{\"id\":\"kn_device\", \"type\":\"notice\", \"t\":%.3f, \"payload\":%s}", System.nanoTime() * 1e-9, "/devices/1z0/settings/4/attributes/1?cs_initiated=true&desiredAttributeValueId=1")));

        producer.close();
        
    }
    
    

}
