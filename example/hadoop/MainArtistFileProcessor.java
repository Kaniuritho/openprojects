package com.knewton;

import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author kaniu ndungu
 * @date	March 7th 2014
 * @Name MainArtistFileProcessor
 * 
 * This class is the main class for a MapReduce job which calculates
 * which two artists appear at least 50 times in a user's album.
 * 
 * */
public class MainArtistFileProcessor extends Configured implements Tool {


	public static final String INPUT_DEL =",";
	public static final String OUPUT_DEL=",";

	public static void main(String args[]) throws Exception{
		int rc = ToolRunner.run(new MainArtistFileProcessor(), args);
		System.exit(rc);
	}

	public int run(String args[]) throws IOException, InterruptedException, ClassNotFoundException{

		Configuration conf = new Configuration();

		Job job = new Job(conf, "dictionary");

		job.setJarByClass(MainArtistFileProcessor.class);

		job.setReducerClass(ReduceArtists.class);

		job.setMapperClass(MapArtists.class);

		job.setOutputKeyClass(Text.class);

		job.setOutputValueClass(IntWritable.class);

		job.setMapOutputKeyClass(Text.class);

		job.setMapOutputValueClass(IntWritable.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));

		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;


	}

	/**
	 * Mapper determines unique two name combination per file row and makes that a countable value using name combination as key.
	 * */
	public static class MapArtists extends  Mapper<Text, Text, Text, IntWritable>{	

		private IntWritable one = new IntWritable(1);
		private Text twoNames = new Text();

		@Override
		public void map(Text key, Text value,
				Context context)
						throws IOException, InterruptedException {

			String row = key.toString();

			StringTokenizer tokenizer = new StringTokenizer(row,INPUT_DEL);

			Set<String> rowNames = new TreeSet<String>();

			while(tokenizer.hasMoreTokens()){

				rowNames.add(tokenizer.nextToken());

			}


			String concatName = null;
			String[] rowNamesArray = rowNames.toArray(new String[]{});
			for(int i = 0 ; i <  rowNamesArray.length; i++){
				concatName = rowNamesArray[i];
				for (int j = i+1; j < rowNamesArray.length; j++){

					twoNames.set(concatName+OUPUT_DEL+rowNamesArray[j]);
					
					context.write(twoNames, one);
				
				}

			}

		}


	}
	
	/**
	 * Reducer recieves artist name combination as key and sums up the value/instance. 
	 * */
	public static class ReduceArtists extends Reducer<Text, IntWritable,  Text, IntWritable>{

		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context)
						throws IOException, InterruptedException {

			int sum = 0;
			
			for (IntWritable one : values){
				sum += one.get();			
			}

			if(sum>=50){
				context.write(key,null);
			}

		}

	}
}
