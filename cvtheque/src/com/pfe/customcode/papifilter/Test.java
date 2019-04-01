package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		try {
			String url = "https://api.stackexchange.com/2.2/users/1144035/answers?order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		     URL obj = new URL(url);
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		     // optional default is GET
		     con.setRequestMethod("GET");
		     //add request header
		     con.setRequestProperty("User-Agent", "Mozilla/5.0");
		     
		     
		     int responseCode = con.getResponseCode();
		     System.out.println("\nSending 'GET' request to URL : " + url);
		     System.out.println("Response Code : " + responseCode);
		     
		     
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     //print in String
		     
		     System.out.println(response.toString());
		     //Read JSON response and print
		     //JSONObject myResponse = new JSONObject(response.toString().replace("[", "").replace("]", ""));
		     String resp =response.toString();
		     JSONObject myResponse = new JSONObject(resp);
		     JSONArray arr = new JSONArray();
				arr = myResponse.getJSONArray("items");
				String ownerLink = "";

				if (arr.length() > 0) {
					
					 //ownerLink = arr.getJSONObject(i).getJSONObject("owner").getString("link");
					ownerLink = arr.getJSONObject(0).getJSONObject("reputation").toString();
					
				
				}
	         System.out.println(ownerLink);
	        } catch (Exception e) {
	        	System.out.println("error ");
	       }

	}
	
	     }


