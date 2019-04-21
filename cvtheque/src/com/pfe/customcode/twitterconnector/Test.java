package com.pfe.customcode.twitterconnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Test {
	
	public static void main(String[] args) throws Exception {
	String rep = parse("http://127.0.0.1:5000/search/spotgabbiani");
	JSONObject myResponse = new JSONObject(rep);
	String tweets_count = myResponse.get("tweets_count").toString();
	System.out.println(tweets_count);
	String followers_count = myResponse.get("followers_count").toString();
	System.out.println(followers_count);
	String average_tweet_per_day = myResponse.get("average_tweet_per_day").toString();
	System.out.println(average_tweet_per_day);
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
