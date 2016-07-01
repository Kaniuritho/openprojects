package io.durability.spark.kafka;

import scala.Tuple2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import io.durability.spark.phoenix.PhoenixForwarder;
import kafka.serializer.StringDecoder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Is a Kafka consumer using Spark api. Reads from a topic specified in
 * consumer.props. Writes to 2 outputs: HBase,HDFS 
 */
public final class SparkKafkaConsumer implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String topics_str = null;
	private int numThreads;
	public SparkKafkaConsumer(String _topics, int _numThreads) {
		topics_str=_topics;
		numThreads=_numThreads;
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Usage: SparkKafkaConsumer <topics> <numThreads>");
			System.exit(1);
		}
		int numThreads = Integer.parseInt(args[1]);
		
		new SparkKafkaConsumer(args[0], numThreads).run();
	}
	
	public void run() throws IOException{
		InputStream props = getClass().getClassLoader().getResourceAsStream("consumer.props");
		Properties properties = new Properties();
		properties.load(props);
			

		if (properties.getProperty("group.id") == null) {
			properties.setProperty("group.id", "group-localtest");
		}
		
		final String phoenix_zk_JDBC = properties.getProperty("phoenix.zk.jdbc");
		final String hdfs_output_dir = properties.getProperty("hdfs.outputdir");
		
		//StreamingExamples.setStreamingLogLevels();
		SparkConf sparkConf = new SparkConf().setAppName("SparkConsumer").set("spark.driver.allowMultipleContexts","true");
		   
		// Create the context with 1 seconds batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));

		
		Map<String, Integer> topicMap = new HashMap<>();
		String[] topics = topics_str.split(",");
		for (String topic : topics) {
			topicMap.put(topic, numThreads);
		}
		
		 // Create direct kafka stream with brokers and topics
		 Set<String> topicsSet = new HashSet<>(Arrays.asList(topics_str.split(",")));
		 Map<String, String> kafkaParams = new HashMap<>();
		    kafkaParams.put("metadata.broker.list", properties.getProperty("bootstrap.servers"));
	    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
	        jssc,
	        String.class,
	        String.class,
	        StringDecoder.class,
	        StringDecoder.class,
	        kafkaParams,
	        topicsSet
	    );
	    
	    
	    
	    // Get the lines, split them into words, count the words and print
	    JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
			private static final long serialVersionUID = 1L;

		@Override
	      public String call(Tuple2<String, String> tuple2) throws IOException {

			String ret = (tuple2._2 ==null?"": tuple2._2).concat(tuple2._1 ==null?"": tuple2._1);
			
			appendToHDFS(hdfs_output_dir + "/MSG_" + System.currentTimeMillis() + ".txt",  ret);

			//apply rules here to determine what messages proceed to next level
			//you may also add tags for other rules to process downstream for re-routing etc
			
				
			return ret ;
	      }
	    });

	    
	    lines = lines.repartition(6);
	    
	    lines.foreachRDD(new Function<JavaRDD<String>, Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Void call(JavaRDD<String> stringJavaRDD) throws Exception {

				stringJavaRDD.foreachPartition(new VoidFunction<Iterator<String>>() {

							private static final long serialVersionUID = 1L;
							

							@Override
							public void call(Iterator<String> it) throws Exception {
								PhoenixForwarder phoenixConn = new PhoenixForwarder(phoenix_zk_JDBC);
								String msg = null;
								while(it.hasNext() && (msg =it.next() ) !=null ){
									phoenixConn.saveToJDBC(msg);
								}
								phoenixConn.closeCon();
								
							}

						}

				);
				
				return null;
			}});


		jssc.start();
		jssc.awaitTermination();
		
	}
	
	private  Configuration createHDFSConfiguration() {

		Configuration hadoopConfig = new Configuration();
		hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

		return hadoopConfig;
	}

	private  void appendToHDFS(String pathStr, String data) throws IOException {
		System.out.println(data);
		// long key = new Random().nextLong() + System.currentTimeMillis();

		Path path = new Path(pathStr);
		Configuration conf = createHDFSConfiguration();
		FileSystem fs = path.getFileSystem(conf);

		try {

			if (!fs.exists(path)) {
				fs.create(path);
				fs.close();
			}
			 fs = path.getFileSystem(conf);
			FSDataOutputStream out = fs.append(path);
			out.writeUTF(data);
		} finally {
			fs.close();
		}

	}

	public  boolean writeToHDFS(String pathStr, String data) throws IOException {
		System.out.println(data);
		Path path = new Path(pathStr);
		FileSystem fs = FileSystem.get(createHDFSConfiguration());

		if (!fs.exists(path)) {
			fs.create(path);
		}

		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.create(path, true)));

		

		br.write(data);
		br.close();

		return true;

	}


}
