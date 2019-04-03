package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.exalead.util.StringUtils;

public class JsonReader {

	public static void main(String[] args) throws Exception {
		/*String res = parse("https://api.github.com/users/arindam-bandyopadhyay");
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		System.out.println("url repos"+repos);
		String myrepos = parse(repos);
		JSONObject reposResp = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
		System.out.println("repos!"+reposResp.toString());
		/*
		
		
		Boolean s = getMore("https://api.github.com/users/AdamLindenthal/repos?page=10&per_page=1");
		System.out.println(s);
		//String name = getReposName("https://api.github.com/users/AdamLindenthal");
		//System.out.println(name);
		//String desc = getReposDescription("https://api.github.com/users/AdamLindenthal");
		//System.out.println("des: "+desc);
		//String lang = getReposLangages("https://api.github.com/users/AdamLindenthal");
		//System.out.println("langages: "+lang);
		//String str = parse("https://api.github.com/users/AdamLindenthal/repos?page=1&per_page=1");
		//System.out.println(str);
		//String more = getMore("https://api.github.com/users/AdamLindenthal/repos?page=1&per_page=1");
		//System.out.println(more);
		 
		 
		/*URL obj = new URL("https://api.github.com/users/AdamLindenthal/repos?page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		
		//get all headers
		Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + 
	                 " ,Value : " + entry.getValue());
		}
		
		//get header by 'key'
		String Link = conn.getHeaderField("Link");
		System.out.println("!!!!"+Link);*/
    }
	public static String getLink(String url) throws Exception{
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String link = myResponse.get("html_url").toString();
		return link;
	}
	/*public static String getReposLangages(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getRepos(url);
		
		return jobj.get("language").toString();
	}
	
	public static String getReposDescription(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getRepos(url);
		//String name = jobj.get("name").toString();
		return jobj.get("description").toString();
	}*/
	
	public static String getReposName(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getMore(url);
		//String name = jobj.get("name").toString();
		return jobj.get("name").toString();
	}
	
	
	/*public static JSONObject getRepos(String url) throws Exception{
		JSONObject reposResp = new JSONObject();
		String resultat=null;
		String link=null;
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		//System.out.println("url repos"+repos);
		
		
		//String name = reposResp.get("name").toString();
		//String description = reposResp.get("description").toString();
		
		
		int p = 1;
		if (getMore(repos)==true) {
			link = repos+"?page="+p+"&per_page=1";
			String myrepos = parse(link);
			 reposResp = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
			p++;
			
		}
		
		
		return reposResp;
	}*/
	
	
	
	public static JSONObject getMore(String url) throws Exception {
		
		JSONObject reposResp = new JSONObject();
		String resultat=null;
		String link=null;
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		
		
				String[] Tableau2 = new String[2];
				
		URL obj = new URL(repos+"?page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		
		//get all headers
		Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + 
	                 " ,Value : " + entry.getValue());
		}
		String str = conn.getHeaderField("Link");
		String Tableau[] = str.split(","); 
		
			int k = 0;
			while (k<=Tableau.length-1) {
				String text = Tableau[k];
				
				if (text.contains("next")==true) {
					link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
					String myrepos = parse(link);
					 reposResp = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
				}	
					
					
					//resultat = parse(link);
				
				k++;
			}
		return reposResp;
	}
	
	public static String parse(String purl) throws Exception {
	     
	     URL obj = new URL(purl);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("GET");
	     //con.setRequestProperty("Authorization", "token 9cc26815ae56d43a7238535c8dbf48ddfa2c8af2");
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