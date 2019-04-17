package com.pfe.customcode.connectorstack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = StackConnectorConfig.class)
@CVComponentDescription(value = "stack connector")
public class StackConnector extends Connector implements CVComponent {

	private Logger logger = Logger.getLogger(StackConnector.class);
	protected String url ;
	protected String driver ;
	public StackConnector(StackConnectorConfig config) throws Exception {
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
				
				String login = res.getString("login_stackoverflow") ;  
				System.out.println("stack login!!! "+login);
				List<String> tags = parseTags(login);
				String url = parseLink(login);
				MetaContainer metaList = new MetaContainer();
				metaList.addMeta("login_stackoverflow", login);
				for (int i = 0; i < tags.size(); i++) {
					metaList.addMeta("stack_tags", "#"+tags.get(i));
				}
				
				metaList.addMeta("stack_url", url);
				String uri = login;
				papi.addDocument(new Document(uri, "0", partList, metaList));
			}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in stackoverflow"); 
				}
	}
	
	public static List<String> parseTags(String login) throws IOException, JSONException{
		String url = "https://api.stackexchange.com/2.2/users/"+login+"/tags?order=desc&sort=popular&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		List<String> tags = new ArrayList<String>();
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
		JSONArray arr = new JSONArray();
		arr = myResponse.getJSONArray("items");
		for (int i = 0; i < arr.length(); i++) {
			tags.add(arr.getJSONObject(i).get("name").toString()) ;
		}
	    return tags;	
	}
	
	public static String parseLink(String login) throws IOException, JSONException{
		String url = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		String str="";
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
		JSONArray arr = new JSONArray();
		arr = myResponse.getJSONArray("items");
		str = arr.getJSONObject(0).getJSONObject("owner").get("link").toString();
	    return str;	
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
