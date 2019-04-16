package com.pfe.customcode.twitterconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestBD {

	public static void main(String[] args) {
		String login_twitter = "";
		String url = "jdbc:sqlite:/home/msfaihi/install/onecall-2016x.R1.11820-linux-x64/doc/db_candidats/"; 
		String dbName = "candidat"; 
		String driver = "org.sqlite.JDBC";
	try { 
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName); 
		Statement st = conn.createStatement(); 
		ResultSet res = st.executeQuery("Select login_twitter, '' as  from Candidats"); 
		while (res.next()) { 
			 login_twitter = res.getString("login_twitter");  
			 System.out.println("twitter login is : "+login_twitter);
			} 
		
		conn.close(); 
		} catch (Exception e) { 
			System.out.println("error in db");
			}

	}

}
