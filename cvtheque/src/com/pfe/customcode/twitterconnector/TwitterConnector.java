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
		try { 
			Class.forName(driver).newInstance(); 
			Connection conn = DriverManager.getConnection(url); 
			Statement st = conn.createStatement(); 
			ResultSet res = st.executeQuery("Select login_twitter from Candidats"); 
			while (res.next()) { 
				String login = res.getString("login_twitter") ;  
				String response = parse("http://149.202.64.60:8888/search/"+login);
				JSONObject myResponse = new JSONObject(response);
				String tweets_count = myResponse.get("tweets_count").toString();
				String followers_count = myResponse.get("followers_count").toString();
				String average_tweet_per_day = myResponse.get("average_tweet_per_day").toString();
				String name = myResponse.get("screen_name").toString();
			
				MetaContainer metaList = new MetaContainer();
				metaList.addMeta("login_twitter", name);
				metaList.addMeta("tweets_count", tweets_count);
				metaList.addMeta("followers_count", followers_count);
				metaList.addMeta("average_tweet_per_day", average_tweet_per_day);
				
					String uri = name;
					
					papi.addDocument(new Document(uri, "0", partList, metaList));
				}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in twitter"); 
				}
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
