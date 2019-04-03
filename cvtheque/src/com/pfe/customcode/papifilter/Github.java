package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;

public class Github {

	public static void main(String[] args) throws Exception {
		HashMap<String, String> map = getMore("https://api.github.com/users/arindam-bandyopadhyay?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c");
		
		Set set = map.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }
		

	}
	
	public static List<String> getRel(String url) throws Exception{
	URL obj = new URL(url+"?per_page=1");
	URLConnection conn = obj.openConnection();
	
	//get all headers
	Map<String, List<String>> map = conn.getHeaderFields();
	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		//System.out.println("Key : " + entry.getKey() + 
               //  " ,Value : " + entry.getValue());
	}
	String str = conn.getHeaderField("Link");
	//String Tableau[] = str.split(","); 
	List<String> strList = new ArrayList<String>(Arrays.asList(str.split(",")));
	return strList;
	}
	
	
public static HashMap<String, String> getMore(String url) throws Exception {
	HashMap<String, String> hmap = new HashMap<String, String>();
	List<String> name = new ArrayList<String>();
	List<String> description = new ArrayList<String>();
	List<String> langage = new ArrayList<String>();
		
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos = myResponse.get("repos_url").toString();
		String resRepos = parse(repos+"?page=1&per_page=1");
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		name.add(myFirstRespos.get("name").toString()) ;
		 description.add(myFirstRespos.get("description").toString()) ;
		 langage.add(myFirstRespos.get("language").toString()) ;
		 hmap.put(myFirstRespos.get("name").toString()+" : "+myFirstRespos.get("description").toString(), myFirstRespos.get("language").toString());
		 
		 List<String> strList = getRel(repos);
		
		/*URL obj = new URL(repos+"?per_page=1");
		URLConnection conn = obj.openConnection();
		
		//get all headers
		Map<String, List<String>> map = conn.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + 
	                 " ,Value : " + entry.getValue());
		}
		String str = conn.getHeaderField("Link");
		//String Tableau[] = str.split(","); 
		List<String> strList = new ArrayList<String>(Arrays.asList(str.split(",")));*/
		
		
		boolean trouve = false;
			int k = 0;
			String link = null;
			while (k<=strList.size() ) {
				String text = strList.get(k);
				if (text.contains("next")==true) {
					//String link = repos+"?page="+p+"&per_page=1";
			 link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
					String myrepos = parse(link+"&client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c");
					
					
					
					JSONObject myRespos = new JSONObject(myrepos.substring(myrepos.indexOf('{')));
					 name.add(myRespos.get("name").toString()) ;
					 description.add(myRespos.get("description").toString()) ;
					 langage.add(myRespos.get("language").toString()) ;
					 hmap.put(myRespos.get("name").toString()+" : "+myRespos.get("description").toString(), myRespos.get("language").toString());
					 //trouve = true;
					// strList = getRel(link);
				}else {
					k++;
				}
				
				
			}
			
		return hmap;
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
