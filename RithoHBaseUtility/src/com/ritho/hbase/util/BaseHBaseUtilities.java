package com.ritho.hbase.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public abstract class BaseHBaseUtilities {

	/**HBase default data version is 3. You can make the data versions larger in HBase Config before updating this value;*/
	private int versionCount = 3;
	
	
	@SuppressWarnings("resource")
	public synchronized HTable createTable(Configuration conf, String tableName , String...columnNames) throws IOException{
		
		HTableDescriptor tbl = new HTableDescriptor( tableName );

		for(String columnName : columnNames ){
			tbl.addFamily( new HColumnDescriptor( columnName ) ); 
		}

		HBaseAdmin hba = new HBaseAdmin( conf );
		hba.createTable( tbl );
		
		return new HTable(conf,tableName);
	}
	
	public synchronized void addColumn(Configuration conf, String tableName, String columnName) throws IOException{
		
		HBaseAdmin hba = new HBaseAdmin( conf );
		
		if(hba.tableExists(tableName)){
			hba.addColumn(tableName,  new HColumnDescriptor( columnName ) );
		}
		
		hba.close();
		
	}
	
	@SuppressWarnings("resource")
	public synchronized void dropTable(Configuration conf, String tableName) throws IOException{
		HBaseAdmin hba = new HBaseAdmin( conf );
		hba.deleteTable(tableName);
	}
	
	public void deleteColumn(HTable table, String tableName, String columnName) throws IOException{
		Delete del = new Delete();
		del.deleteColumn(Bytes.toBytes(tableName), Bytes.toBytes(columnName));
		
		table.delete(del);
	}
	
	public void deleteColumn(HTable table, String tableName, String rowName , String columnName) throws IOException{
		Delete del = new Delete(Bytes.toBytes(rowName));
		del.deleteColumn(Bytes.toBytes(tableName), Bytes.toBytes(columnName));
		
		table.delete(del);
	}
	
	public void deleteRow(HTable table , String rowName) throws IOException{
		Delete del = new Delete(Bytes.toBytes(rowName));		
		table.delete(del);
	}

	public byte[] getRowColumnData(HTable table, String row, String columnName, String qualifier) throws IOException{
		
		Get get = new Get( Bytes.toBytes(row) );
		
		Result result = table.get(get);
		
		byte[] value = result.getValue(Bytes.toBytes(columnName), Bytes.toBytes(qualifier));
		
		return value;
	}
	
	
	public NavigableMap<byte[], byte[]> getRowColumnMap(HTable table, String row, String columnName) throws IOException{
		
		Get get = new Get( Bytes.toBytes(row) );
		
		Result result = table.get(get);
		
		NavigableMap<byte[], byte[]> value = result.getFamilyMap(Bytes.toBytes(columnName));
		
		return value;
	}
	
	
	public List <Result> scanRowColumnData(HTable table, String startRow, String stopRow) throws IOException{
		
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner resScan = table.getScanner(scan); 
		
		List <Result> returnList = new ArrayList<Result>();
		for (Result result = resScan.next(); result != null; result = resScan.next()) {
			returnList.add(result);
		}
			
		return returnList;
	}
	
	
	public List <Result> scanRowColumnData(HTable table, Scan scan) throws IOException{

		ResultScanner resScan = table.getScanner(scan); 
		
		List <Result> returnList = new ArrayList<Result>();
		
		for (Result result = resScan.next(); result != null; result = resScan.next()) {
			returnList.add(result);
		}
			
		return returnList;
	}
	
	/**
	 * To get versions make <code>qualifier<code>='version'.
	 * */
	public List<KeyValue> getVersionedRowColumnData(HTable table, String row,  String columnName, String qualifier) throws IOException{
		
		Get get = new Get( Bytes.toBytes(row));
	
		get.setMaxVersions(getVersionCount());

		Result result = table.get(get);
		
		List<KeyValue> versionedValues = result.getColumn(Bytes.toBytes(columnName), Bytes.toBytes(qualifier));
		
		return versionedValues;
	}
	
	
	public KeyValue getLatestRowColumnData(HTable table, String row,  String columnName, String qualifier) throws IOException{
		
		Get get = new Get( Bytes.toBytes(row));
	
		Result result = table.get(get);
		
		KeyValue keyVal = result.getColumnLatest(Bytes.toBytes(columnName), Bytes.toBytes(qualifier));
		
		return keyVal;
	}
	
	/**
	 * Add Implicitly versioned data. Hbase adds newest data with version = timestamp.
	 * */
	public synchronized void addData(HTable table, String row, String column, String qualifier, byte[] value) throws IOException{
		
		Put put = new Put( Bytes.toBytes(row));
		
		put.add(Bytes.toBytes(column), Bytes.toBytes(qualifier), value);
		
		table.put(put);
		
	}
	
	/**
	 * Add explicitly versioned data. Same as overwrite data.
	 *  */
	public synchronized void addData(HTable table, String row, String column, String qualifier,long version,  byte[] value) throws IOException{
		
		Put put = new Put( Bytes.toBytes(row));
		
		put.add(Bytes.toBytes(column), Bytes.toBytes(qualifier),version,  value);
		
		table.put(put);
		
	}

	
	public int getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(int versionCount) {
		this.versionCount = versionCount;
	}
}
