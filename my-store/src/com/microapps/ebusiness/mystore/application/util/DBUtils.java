/*package com.microapps.ebusiness.mystore.application.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.h2.jdbc.JdbcSQLException;

import com.mycontacts.application.business.exception.AnotherInstanceRunningException;

public final class DBUtils {
	
	private static final Logger LOGGER = Logger.getLogger(DBUtils.class.getName());
	
	private static Connection conn;
	
	public static void openDatabase() throws ClassNotFoundException, SQLException, AnotherInstanceRunningException {
		Class.forName ("org.h2.Driver"); 
		try {
			conn = DriverManager.getConnection ("jdbc:h2:~/myshop-db", "sa","sa");
		} catch (JdbcSQLException e) {
			throw new AnotherInstanceRunningException("The DB is blocked by another instance of the application");
		} 
		
		Class.forName ("com.mysql.jdbc.Driver"); 
		try {
			conn = DriverManager.getConnection ("jdbc:mysql://localhost:3306/my-shop-test-db", "root","root");
		} catch (JdbcSQLException e) {
			throw new AnotherInstanceRunningException("The DB is blocked by another instance of the application");
		}
	}
	
	public static void closeDatabase() throws ClassNotFoundException, SQLException {
		if(conn != null) {
			conn.close();
		}
		conn = null;
	}
	
	public static void runDDL() throws SQLException{
	 Statement stmt = getConnection().createStatement();
	  // User table
	  stmt.execute("CREATE TABLE IF NOT EXISTS business_user (id IDENTITY, name varchar(255), username varchar(50) UNIQUE, password varchar(255))");
	 // stmt.execute("insert into user (name, username, password) values ('Test User', 'test', 'test');");
	  
	  //Activity table
	  stmt.execute("CREATE TABLE IF NOT EXISTS activity (id IDENTITY, cust_id int, name varchar(50), item_group varchar(50), amount double, earned_points BIGINT, created_on TIMESTAMP, created_by varchar(50))");
	  
	  //Customer table
	  stmt.execute("CREATE TABLE IF NOT EXISTS customer (id IDENTITY, name varchar(50), mobile varchar(12) UNIQUE, email varchar(50), cardnum varchar(20) UNIQUE, dob Date, gender char, status boolean, "
	  		+ "created_on TIMESTAMP, created_by varchar(50))");
	  		
	  //Business table
	   stmt.execute("CREATE TABLE IF NOT EXISTS business (id IDENTITY, name varchar(100), business_name varchar(100), address varchar(500), act_code varchar(100), status boolean, "
		  		+ "created_on TIMESTAMP)");
		  		
	  //Item Master table
	  stmt.execute("CREATE TABLE IF NOT EXISTS item_group (id IDENTITY, name varchar(100) UNIQUE, status boolean, created_on TIMESTAMP, created_by varchar(50))");
		  
		  
	  //Item App settings table
	  stmt.execute("CREATE TABLE IF NOT EXISTS app_settings (id IDENTITY, mpf float, pmf float, rtp int, status boolean, created_on TIMESTAMP, created_by varchar(50))");
		  
	  
	  
	  stmt.close();
	  
		
		Statement stmt = getConnection().createStatement();
		  // User table
		  stmt.execute("CREATE TABLE IF NOT EXISTS business_user (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(255), username varchar(50) UNIQUE, password varchar(255))");
		//  stmt.execute("insert into user (name, username, password) values ('Test User', 'test', 'test');");
		  
		  //Activity table
		  stmt.execute("CREATE TABLE IF NOT EXISTS activity (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, cust_id int, name varchar(50), item_group varchar(50), amount double, earned_points BIGINT, created_on TIMESTAMP, created_by varchar(50))");
		  
		  //Customer table
		  stmt.execute("CREATE TABLE IF NOT EXISTS customer (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(50), mobile varchar(12) UNIQUE, email varchar(50), cardnum varchar(20) UNIQUE, dob Date, gender char, status boolean, "
		  		+ "created_on TIMESTAMP, created_by varchar(50))");
		  
		 //Business table
		  stmt.execute("CREATE TABLE IF NOT EXISTS business (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(100), business_name varchar(100), address varchar(500), act_code varchar(100), status boolean, "
		  		+ "created_on TIMESTAMP)");
		  
		//Item Master table
		  stmt.execute("CREATE TABLE IF NOT EXISTS item_group (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(100) UNIQUE, status boolean, created_on TIMESTAMP, created_by varchar(50))");
		  
		  
		  //Item App settings table
		  stmt.execute("CREATE TABLE IF NOT EXISTS app_settings (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, mpf float, pmf float, rtp int, status boolean, created_on TIMESTAMP, created_by varchar(50))");
			  
		 
		  stmt.close();
	}
	
	
	public static Connection getConnection() throws SQLException {
		if(conn != null) {
			return conn;	
		}else {
			throw new SQLException("No database connection is available!");
		}
		
	}

}
*/