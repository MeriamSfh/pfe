package com.pfe.customcode.papifilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Test {

	public static void main(String[] args) throws IOException, JSONException {
		String str = parseRepos("https://api.github.com/users/AdamLindenthal");
		System.out.println("repos:"+str);
	     }
public static String parseRepos(String url) throws IOException, JSONException{
		
		String result = parse(url);
	    JSONObject myResponse = new JSONObject(result);  
	    System.out.println(myResponse.toString());
	    String reposUrl = myResponse.get("repos_url").toString();
	    String result2 = parse(reposUrl);
		JSONObject myResponse2 = new JSONObject(result2.substring(result2.indexOf('{')));
	    return myResponse2.toString();	
	}
	public static String parse(String url) throws IOException, JSONException{
		String baseURI = url;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource wr = client.resource(baseURI); 
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		String response_data = response.getEntity(String.class);

		
	    return response_data;
	}
}


