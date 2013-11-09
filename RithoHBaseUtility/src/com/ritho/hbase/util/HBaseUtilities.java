package com.ritho.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.sun.istack.NotNull;

/**
 * HBase 1.1.0 common DB functions
 * UPDATES:
 * 1. Instantiation with config.
 * 2. Manage connections through PostConstruct and Predestroy
 * 3. Explore annotations for method calls!
 * 
 * 
 * @author kaniu ndungu
 * */
public class HBaseUtilities extends BaseHBaseUtilities{

	private static HBaseUtilities singleton = null;
	private HBaseAdmin hbaseAdmin = null;
	
	public HBaseUtilities(Configuration conf) throws HBaseUtilException {
		try {
			setHbaseAdmin(new HBaseAdmin( conf ));
		} catch (MasterNotRunningException e) {
			throw new HBaseUtilException(e);
		} catch (ZooKeeperConnectionException e) {
			throw new HBaseUtilException(e);
		}
	}

	public HBaseUtilities() {
		// TODO Auto-generated constructor stub
	}

	public static HBaseUtilities getSingleton(){ 
		if(singleton==null){
			singleton = new HBaseUtilities();
		}
		return singleton;
	}
	
	public static HBaseUtilities getInstance(){return new HBaseUtilities();}
	
	
	public static HBaseUtilities getSingleton(Configuration conf) throws HBaseUtilException{ 
		if(singleton==null){
			singleton = new HBaseUtilities( conf);
		}
		return singleton;
	}
	
	public static HBaseUtilities getInstance(Configuration conf) throws HBaseUtilException{
		return new HBaseUtilities( conf);
	}

	//@NotNull
	public HBaseAdmin getHbaseAdmin() {
		return hbaseAdmin;
	}

	public void setHbaseAdmin(HBaseAdmin _hbaseAdmin) {
		hbaseAdmin = _hbaseAdmin;
	}

}
