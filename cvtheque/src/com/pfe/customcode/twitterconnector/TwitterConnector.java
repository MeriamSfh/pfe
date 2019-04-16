package com.pfe.customcode.twitterconnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.MetaContainer;
import com.exalead.papi.helper.PartContainer;
import com.exalead.papi.helper.PushAPI;

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = TwitterConnectorConfig.class)
@CVComponentDescription(value = "twitter connector")
public class TwitterConnector extends Connector implements CVComponent {
	
	protected String url ;
	protected String driver ;

	private Logger logger = Logger.getLogger(TwitterConnector.class);
	public TwitterConnector(TwitterConnectorConfig config) throws Exception {
		super(config);
		url = config.getUrl();
		driver = config.getDriver();
	}

	@Override
	public void scan(PushAPI papi, String scanMode, Object scanModeConfig) throws Exception {
		
		PartContainer partList = new PartContainer();
		//List<String> login = new ArrayList<String>();
		try { 
			Class.forName(driver).newInstance(); 
			Connection conn = DriverManager.getConnection(url); 
			Statement st = conn.createStatement(); 
			ResultSet res = st.executeQuery("Select * from Candidats"); 
			while (res.next()) { 
				MetaContainer metaList = new MetaContainer();
				String login = res.getString("login_twitter") ;  
				//logger.info("login!!!!!!!!!!!!"+login);
				String name = getname(login);
					//String name = login.get(f);
					//logger.info("name!!!!!!!!!!!!"+name);
				List<String> dates = getdate(login);
					//logger.info("dates!!!!!!!!!!!!"+dates);
				List<String> tweets = gettweet(login);
					//logger.info("tweets!!!!!!!!!!!!"+tweets);
					metaList.addMeta("login_twitter", name);
					for (int i = 0; i < tweets.size(); i++) {
						metaList.addMeta("tweet_text", tweets.get(i));
					}
					for (int k = 0; k < dates.size(); k++) {
						metaList.addMeta("created_at", dates.get(k));
					}
					String uri = name;
					
					papi.addDocument(new Document(uri, "0", partList, metaList));
				}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in db"); 
				}
		
		
	}
	
	public static String getname(String login) throws Exception {
		String res = parse("http://149.202.64.60:8888/search/"+login);
		JSONObject myResponse = new JSONObject(res);
		String name = myResponse.get("screen_name").toString();
		return name;
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
