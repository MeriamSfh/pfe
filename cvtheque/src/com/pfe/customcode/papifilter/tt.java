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
		
		//List<String> ans = parseAnswers("https://api.stackexchange.com/2.2/users/2901002/answers?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((");
		
		//System.out.println("final result is:"+Arrays.toString(ans.toArray()));
		
		//HashMap<String, String> ques = parseQuestions("https://api.stackexchange.com/2.2/users/2901002/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((");
		/*Set set = ques.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }*/
	      
	      String link = parseLink("https://api.stackexchange.com/2.2/users/2901002/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((");
	System.out.println("link is:"+link);
	}
	
	public static String parseLink(String url) throws IOException, JSONException{
		
		String str="";
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
		JSONArray arr = new JSONArray();
		arr = myResponse.getJSONArray("items");
		for (int i = 0; i < arr.length(); i++) {
		 str = arr.getJSONObject(i).getJSONObject("owner").get("link").toString();
		}
		System.out.println("!!!"+str);
	    return str;	
	}
	
	public static HashMap<String, String> parseQuestions(String url) throws IOException, JSONException{
		HashMap<String, String> hmap = new HashMap<String, String>();
		int p=1;
		String res="";
		List<String> titles = new ArrayList<String>();
		List<String> links = new ArrayList<String>();
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
	    //System.out.println("the response is"+myResponse.toString());
	    
	    Boolean more = myResponse.getBoolean("has_more");
	    while (more==true && p<10) {
	    	p++;
			res = "https://api.stackexchange.com/2.2/users/2901002/questions?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] moreResult = parse(res);
			String mess = new String(moreResult, "UTF-8");
			JSONObject myResp = new JSONObject(mess);
			JSONArray arr = new JSONArray();
			arr = myResp.getJSONArray("items");
		    for (int i = 0; i < arr.length(); i++) {
		    	titles.add(arr.getJSONObject(i).get("title").toString()); 
		    	links.add(arr.getJSONObject(i).get("link").toString()); 
		    	hmap.put(arr.getJSONObject(i).get("title").toString(), arr.getJSONObject(i).get("link").toString());
		    }
		    
	    }
	    /*Set set = hmap.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }*/
	   System.out.println("links"+Arrays.toString(links.toArray()));
	    return hmap;	
	}
	
	
	
	public static List<String> parseAnswers(String url) throws IOException, JSONException{
		int p=1;
		String res="";
		List<String> answers = new ArrayList<String>();
		List<String> questions = new ArrayList<String>();
		List<String> resultat = new ArrayList<String>();
		byte[] result = parse(url);
		String message = new String(result, "UTF-8");
	    JSONObject myResponse = new JSONObject(message);  
	    //System.out.println("the response is"+myResponse.toString());
	    
	    Boolean more = myResponse.getBoolean("has_more");
	    while (more==true && p<10) {
	    	p++;
			res = "https://api.stackexchange.com/2.2/users/2901002/answers?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] moreResult = parse(res);
			String mess = new String(moreResult, "UTF-8");
			JSONObject myResp = new JSONObject(mess);
			JSONArray arr = new JSONArray();
			arr = myResp.getJSONArray("items");
		   // System.out.println("items is:"+arr);
		    for (int i = 0; i < arr.length(); i++) {
		    	
				answers.add(arr.getJSONObject(i).get("answer_id").toString()); 
				questions.add(arr.getJSONObject(i).get("question_id").toString());	
				
			//resultat.add("https://stackoverflow.com/questions/"+questions[i]+"/#"+answers[i]);
			//System.out.println("answer's url is ====>"+Arrays.toString(answers.toArray()));
			//System.out.println("questions's url is ====>"+Arrays.toString(answers.toArray()));
		    }
		  
		    for (int i = 0; i < answers.size(); i++) {
		    	resultat.add("https://stackoverflow.com/questions/"+questions.get(i)+"/#"+answers.get(i));
			}
		    
	    }	
	    //System.out.println("answer's url is ====>"+Arrays.toString(answers.toArray()));
		//System.out.println("questions's url is ====>"+Arrays.toString(answers.toArray()));
	   // System.out.println("resultat!!!!"+resultat);
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
