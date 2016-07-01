package io.durability.spark.phoenix;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.phoenix.jdbc.PhoenixDriver;

public class PhoenixForwarder implements Serializable {

	private static final long serialVersionUID = 1L;

	Connection conn = null;
	PreparedStatement prepStmt = null;
	public PhoenixForwarder(String zookeeper_quorum) throws SQLException, ClassNotFoundException {


			Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
			DriverManager.registerDriver(new PhoenixDriver());
			conn = DriverManager.getConnection(zookeeper_quorum);

		
	}
	
	//private PhoenixForwarder(){}
	

	public  void saveToJDBC(String message ) throws SQLException {
		
		//parse message 
		
		
		prepStmt = conn.prepareStatement("UPSERT INTO test_table (ID,Message)VALUES(?,?)");
		prepStmt.setString(1, System.currentTimeMillis()+"_"+Math.random());
		prepStmt.setString(2, message);
		
		prepStmt.executeUpdate();
	}
	
	
	public synchronized void closeCon() throws SQLException {
		
			if (conn != null){
				conn.commit();
				conn.close();
			}
			if (prepStmt != null){
				prepStmt.close();
			}
		
	}
}
