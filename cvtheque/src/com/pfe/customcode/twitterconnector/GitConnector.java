package com.pfe.customcode.twitterconnector;

import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.MetaContainer;
import com.exalead.papi.helper.PartContainer;
import com.exalead.papi.helper.PushAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = GitConnectorConfig.class)
@CVComponentDescription(value = "twitter connector")
public class GitConnector extends Connector implements CVComponent {
	
	private Logger logger = Logger.getLogger(GitConnector.class);
	

	public GitConnector(GitConnectorConfig config) throws Exception {
		super(config);
	}

	@Override
	public void scan(PushAPI papi, String scanMode, Object scanModeConfig) throws Exception {
		
		String screen_name = getlogin();
		PartContainer partList = new PartContainer();
		MetaContainer metaList = new MetaContainer();
		
		String name = "";
		List<String> dates = new ArrayList<String>();
		List<String> tweets = new ArrayList<String>();
		
		try {
			 name = getname(screen_name);
			 dates = getdate(screen_name);
			 tweets = gettweet(screen_name);
			 } catch (Exception e) {
			logger.info("error in parsing !!! ");
		}
		
		metaList.addMeta("login_twitter", name);
		for (int i = 0; i < tweets.size(); i++) {
			metaList.addMeta("tweet_text", tweets.get(i));
		}
		for (int i = 0; i < dates.size(); i++) {
			metaList.addMeta("created_at", dates.get(i));
		}
		String uri = name;
		papi.addDocument(new Document(uri, "0", partList, metaList));
		
		
	}
	
	
	
	public String getlogin(){
		String login_twitter = "";
		String url = "jdbc:sqlite:/home/msfaihi/install/onecall-2016x.R1.11820-linux-x64/doc/db_candidats/"; 
		String dbName = "candidat.db"; 
		String driver = "org.sqlite.JDBC";
	try { 
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName); 
		Statement st = conn.createStatement(); 
		ResultSet res = st.executeQuery("Select login_twitter, '' as  from Candidats"); 
		while (res.next()) { 
			 login_twitter = res.getString("login_twitter");  
			 logger.info("twitter login is : "+login_twitter);
			} 
		
		conn.close(); 
		} catch (Exception e) { 
			logger.info("error in db"); 
			}
	return login_twitter;
	}
	
	public static List<String> getdate(String login) throws Exception {
		List<String> dates = new ArrayList<String>();
		String res = parse("http://149.202.64.60:8888/search/"+login);
		JSONObject myResponse = new JSONObject(res);
		JSONArray arr1 = new JSONArray();
		arr1 = myResponse.getJSONArray("timeline");
		for (int i = 0; i < arr1.length(); i++) {
			
			dates.add(arr1.getJSONObject(i).getString("created_at"));
		}
		return dates;
		}
	
	public static List<String> gettweet(String login) throws Exception {
		List<String> tweets = new ArrayList<String>();
		String res = parse("http://149.202.64.60:8888/search/"+login);
		JSONObject myResponse = new JSONObject(res);
		JSONArray arr1 = new JSONArray();
		arr1 = myResponse.getJSONArray("timeline");
		for (int i = 0; i < arr1.length(); i++) {
			
			tweets.add(arr1.getJSONObject(i).getString("text"));
		}
		return tweets;
		}
	
	public static String getname(String login) throws Exception {
	String res = parse("http://149.202.64.60:8888/search/"+login);
	JSONObject myResponse = new JSONObject(res);
	String name = myResponse.get("screen_name").toString();
	return name;
	}
public static String parse(String purl) throws Exception {
	    
	    URL obj = new URL(purl);
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod("GET");
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	    while ((inputLine = in.readLine()) != null) {
	    	response.append(inputLine);
	    }
	    in.close();
		return response.toString();
	}

}
