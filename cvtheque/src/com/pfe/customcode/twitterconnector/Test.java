package com.pfe.customcode.twitterconnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class Test {

	public static void main(String[] args) throws Exception {
		
		
		String str = "meriam.sfaihi.pdf";
		String[] vall = str.split("\\.");
		
		System.out.println(String.valueOf(vall[0]+vall[1]));
	
		/*String res = parse("http://149.202.64.60:8888/search/spotgabbiani");
		JSONObject myResponse = new JSONObject(res);
		String name = myResponse.get("screen_name").toString();
		System.out.println(name);
		JSONArray arr1 = new JSONArray();
		arr1 = myResponse.getJSONArray("timeline");
		String text = "";
		String date = "";
		for (int i = 0; i < arr1.length(); i++) {
			
			text = arr1.getJSONObject(i).getString("text");
			date = arr1.getJSONObject(i).getString("created_at");
			System.out.println(text+" & date "+date);
		}*/

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
