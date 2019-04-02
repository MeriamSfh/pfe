package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class JsonReader {

	public static void main(String[] args) throws Exception {
		/*String res = parse("https://api.github.com/users/AdamLindenthal");
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		System.out.println("url repos"+repos);
		String myrepos = parse(repos+"?page=1&per_page=1");
		JSONObject reposResp = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
		System.out.println("repos!"+reposResp.toString());*/
		
		String name = getReposName("https://api.github.com/users/AdamLindenthal");
		System.out.println(name);
		String desc = getReposDescription("https://api.github.com/users/AdamLindenthal");
		System.out.println("des: "+desc);
		String lang = getReposLangages("https://api.github.com/users/AdamLindenthal");
		System.out.println("langages: "+lang);
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
	public static String getReposLangages(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getRepos(url);
		
		return jobj.get("language").toString();
	}
	
	public static String getReposDescription(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getRepos(url);
		//String name = jobj.get("name").toString();
		return jobj.get("description").toString();
	}
	
	public static String getReposName(String url) throws Exception{
		JSONObject jobj = new JSONObject();
		jobj = getRepos(url);
		//String name = jobj.get("name").toString();
		return jobj.get("name").toString();
	}
	
	
	public static JSONObject getRepos(String url) throws Exception{
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		System.out.println("url repos"+repos);
		String myrepos = parse(repos+"?page=1&per_page=1");
		JSONObject reposResp = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
		
		//String name = reposResp.get("name").toString();
		//String description = reposResp.get("description").toString();

		
		return reposResp;
	}
	
	
	
	public static String getMore(String url) throws Exception {
		URL obj = new URL(url);
		URLConnection conn = obj.openConnection();
		
		//get all headers
		/*Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + 
	                 " ,Value : " + entry.getValue());
		}*/
		
		return conn.getHeaderField("Link");
	}
	
	public static String parse(String purl) throws Exception {
	     
	     URL obj = new URL(purl);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
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