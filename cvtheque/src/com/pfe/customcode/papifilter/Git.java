package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

public class Git {

	public static void main(String[] args) throws Exception {
HashMap<String, String> map = getMore("https://api.github.com/users/arindam-bandyopadhyay/repos");
		
		Set set = map.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }
		

	}
	
	public static HashMap<String, String> getMore(String url) throws Exception  {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map = getRepos(url+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		
		URL obj = new URL(url+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>(Arrays.asList(str.split(",")));
		
		boolean trouve = false;
		int k = 0;
		while (k<=strList.size() && trouve==false ) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				//String link = repos+"?page="+p+"&per_page=1";
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				 map = getRepos(link);
				trouve = true;
				 
			}else {
				k++;
			}
		}
		
		return map;
	}
	
	public static HashMap<String, String> getRepos(String url) throws Exception{
		HashMap<String, String> hmap = new HashMap<String, String>();
		List<String> name = new ArrayList<String>();
		List<String> description = new ArrayList<String>();
		List<String> langage = new ArrayList<String>();
		
		/*String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();*/
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		name.add(myFirstRespos.get("name").toString()) ;
		description.add(myFirstRespos.get("description").toString()) ;
		langage.add(myFirstRespos.get("language").toString()) ;
		hmap.put(myFirstRespos.get("name").toString()+" : "+myFirstRespos.get("description").toString(), myFirstRespos.get("language").toString());
		 
		return hmap;
	}
	
public static String parse(String purl) throws Exception {
	    
	    URL obj = new URL(purl);
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
