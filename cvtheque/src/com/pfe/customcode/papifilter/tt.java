package com.pfe.customcode.papifilter;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

public class tt {

	public static void main(String[] args) throws IOException, JSONException {
		
		
		/*List<String> ans = parseAnswers("2901002");
		
		System.out.println("final result is:"+Arrays.toString(ans.toArray()));
		
		List<String> ques = parseQuestions("2901002");
		for (int i = 0; i < ques.size(); i++) {
			String q = ques.get(i);
			System.out.println(q);
		}
		
	      
	      String link = parseLink("2901002");
	      System.out.println("link is:"+link);*/
	      
	      List<String> str = parseTags("2901002");
	      /*for (int i = 0; i < str.size(); i++) {
				String q = str.get(i);
				System.out.println(q);
			}*/
	      
	      
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
			System.out.println("!!!!!!!!!!!!!!"+arr.getJSONObject(i));
		}
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
	
	public static List<String> parseQuestions(String login) throws IOException, JSONException{
		
		String url = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		List<String> questions = new ArrayList<String>();
		
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);
	    JSONArray arr1 = new JSONArray();
		arr1 = myResponse.getJSONArray("items");
		
		questions.add("title: "+arr1.getJSONObject(0).get("title").toString()+" & link: "+arr1.getJSONObject(0).get("link").toString());
		
	    Boolean more = myResponse.getBoolean("has_more");
	    int p=2;
	    while (more==true && p<10) {
	    	
	    	String	res = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] moreResult = parse(res);
			String mess = new String(moreResult, "UTF-8");
			JSONObject myResp = new JSONObject(mess);
			JSONArray arr = new JSONArray();
			arr = myResp.getJSONArray("items");
		    
		    	questions.add("title: "+arr.getJSONObject(0).get("title").toString()+" & link: "+arr.getJSONObject(0).get("link").toString());
		    
		    p++;
	    }
	    return questions;	
	}
	
	
	
	public static List<String> parseAnswers(String login) throws IOException, JSONException{
		String url = "https://api.stackexchange.com/2.2/users/"+login+"/answers?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
		int p=2;
		String res="";
		List<String> answers = new ArrayList<String>();
		List<String> questions = new ArrayList<String>();
		List<String> resultat = new ArrayList<String>();
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
	    JSONArray arr1 = new JSONArray();
	    arr1 = myResponse.getJSONArray("items");
			answers.add(arr1.getJSONObject(0).get("answer_id").toString()); 
			questions.add(arr1.getJSONObject(0).get("question_id").toString());	
			resultat.add("https://stackoverflow.com/questions/"+questions.get(0)+"/#"+answers.get(0));
	    Boolean more = myResponse.getBoolean("has_more");
	    while (more==true && p<10) {
	    	
			res = "https://api.stackexchange.com/2.2/users/"+login+"/answers?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] moreResult = parse(res);
			String mess = new String(moreResult, "UTF-8");
			JSONObject myResp = new JSONObject(mess);
			JSONArray arr = new JSONArray();
			arr = myResp.getJSONArray("items");
			answers.add(arr.getJSONObject(0).get("answer_id").toString()); 
			questions.add(arr.getJSONObject(0).get("question_id").toString());
			p++;
	    }	
	    for (int i = 1; i < answers.size(); i++) {
	    	resultat.add("https://stackoverflow.com/questions/"+questions.get(i)+"/#"+answers.get(i));
		}
	    return resultat;	
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
