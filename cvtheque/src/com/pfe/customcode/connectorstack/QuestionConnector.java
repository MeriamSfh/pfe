package com.pfe.customcode.connectorstack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@PropertyLabel(value = "quest stack")
@CVComponentConfigClass(configClass = QuestionConnectorConfig.class)
@CVComponentDescription(value = "quest stack")
public class QuestionConnector extends Connector implements CVComponent {

	private Logger logger = Logger.getLogger(QuestionConnector.class);
	protected String url ;
	protected String driver ;
	public QuestionConnector(QuestionConnectorConfig config) throws Exception {
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
			ResultSet res = st.executeQuery("Select login_stackoverflow from Candidats"); 
			while (res.next()) { 
				
				String str = res.getString("login_stackoverflow") ; 
				String login = str.substring(0, str.indexOf("/"));
				HashMap<String, String> ques = parseQuestions(login);
				for (Entry<String, String> entry2 : ques.entrySet()) {
			    	  String title = entry2.getKey() ;
			    	  String q = entry2.getValue() ;
			    	  MetaContainer metaList = new MetaContainer();
			    	  metaList.addMeta("login_stackoverflow", login);
			    	  metaList.addMeta("titre_question", title);
			    	  metaList.addMeta("url_question", q);
			    	  String uri = login+"."+title;
			    	  papi.addDocument(new Document(uri, "0", partList, metaList));
					}
			}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in question stack"); 
				}
	}
	
public static HashMap<String, String> parseQuestions(String login) throws IOException, JSONException{
		
		String url = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		HashMap<String, String> map = new HashMap<String, String>();
		
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);
	    JSONArray arr1 = new JSONArray();
		arr1 = myResponse.getJSONArray("items");
		map.put(arr1.getJSONObject(0).get("title").toString(), arr1.getJSONObject(0).get("link").toString());
		
	    Boolean more = myResponse.getBoolean("has_more");
	    int p=2;
	    while (more==true && p<10) {
	    	
	    	String	res = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] moreResult = parse(res);
			String mess = new String(moreResult, "UTF-8");
			JSONObject myResp = new JSONObject(mess);
			JSONArray arr = new JSONArray();
			arr = myResp.getJSONArray("items");
			map.put(arr.getJSONObject(0).get("title").toString(),arr.getJSONObject(0).get("link").toString() );
		    
		    p++;
	    }
	    return map;	
	}
	public static byte[] parse(String url) throws IOException, JSONException{
		String baseURI = url;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource wr = client.resource(baseURI); 
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		byte[] readBuffer = new byte[500000];
		GZIPInputStream inputStream = new GZIPInputStream(response.getEntityInputStream());
	    int read = inputStream.read(readBuffer,0,readBuffer.length);
	    inputStream.close();
	    byte[] result = Arrays.copyOf(readBuffer, read);
	    return result;
	}

}
