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
import java.util.Map.Entry;

import org.json.JSONObject;

import com.exalead.papi.helper.Meta;

public class Git {

	public static void main(String[] args) throws Exception {
		List<String> titre = getTitre("AdamLindenthal");
		List<String> langage = getLangage("AdamLindenthal");
		List<String> des = getdescription("AdamLindenthal");
		List<String> htmlurl= getUrlProjet("AdamLindenthal") ;
		for (int i = 0; i < langage.size(); i++) {
			System.out.println("titre: "+titre.get(i));
			System.out.println("lag: "+langage.get(i));
			System.out.println("des: "+des.get(i));
			System.out.println("html url: "+htmlurl.get(i));
			System.out.println("---------------------------");
		}

		
		/*HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
		hmap = getMore("AdamLindenthal");*/

		/*Set set = map.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }*/
	
		/*List<String> names = new ArrayList<String>();
		List<String> langages = new ArrayList<String>();
		List<String> description = new ArrayList<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		for (Entry<String, HashMap<String, String>> entry : hmap.entrySet()) {
			names.add(entry.getKey()) ;
			map = hmap.get(entry.getKey());
		      for (Entry<String, String> entry2 : map.entrySet()) {
		    	  langages.add(entry2.getKey()) ;
		    	  description.add(entry2.getValue()) ;
				}
		}
		for (int i = 0; i < names.size(); i++) {
			System.out.println(names.get(i)+"langages: "+langages.get(i)+"description: "+description.get(i));
	    	 
		}*/
	      
		
	
	      
	      /*int i=0;
	      while (i<titles.size()) {
			String title = titles.get(i);
			String lang = langages.get(i);
			System.out.println(title+" && "+lang);
			i++;
			
		}
		
	      String link = getLink("bshannon");
	      System.out.println("link is: "+link);
	      int nbre = getNbreRepos("bshannon");
	      String s= Integer.toString(nbre);
	      System.out.println("nbre : "+nbre);
	      int following = getfollowing("bshannon");
	      String s2= Integer.toString(following);
	      System.out.println("following : "+following);
	      int followers = getfollowers("bshannon");
	      String s3= Integer.toString(followers);
	      System.out.println("followers : "+followers);
*/
	      
	}
	
	public static int getfollowers(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String res = parse(url+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c");
		JSONObject myResponse = new JSONObject(res);
		int followers = myResponse.getInt("followers");
		return followers;
	}
	
	public static int getfollowing(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String res = parse(url+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c");
		JSONObject myResponse = new JSONObject(res);
		int following = myResponse.getInt("following");
		return following;
	}
	
	public static int getNbreRepos(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		int nbre = myResponse.getInt("public_repos");
		return nbre;
	}
	public static String getApiRepos(String url) throws Exception{
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos_url = myResponse.get("repos_url").toString();
		return repos_url;
	}
	
	public static String getLink(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String link = myResponse.get("html_url").toString();
		return link;
	}
	
	/*public static HashMap<String, HashMap<String, String>> getMore(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		
		
		HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> hmap2 = new HashMap<String, HashMap<String, String>>();
		
		hmap = getRepos(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>();
		if (str!=null) {
			 strList = Arrays.asList(str.split(","));
		
		
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				hmap2 = getRepos(link);
				hmap.putAll(hmap2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
			}else {
				k++;
			}
		}
		}
		return hmap;
	}*/
	
	public static List<String> getMoredescription(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		List<String> des = new ArrayList<String>();
		List<String> des2 = new ArrayList<String>();
		des = getdescription(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>();
		if (str!=null) {
			 strList = Arrays.asList(str.split(","));
		
		
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				des2 = getdescription(link);
				des.addAll(des2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
			}else {
				k++;
			}
		}
		}
		return des;
	}
	
	
	
	public static List<String> getdescription(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"/repos?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		List<String> des = new ArrayList<String>();
		des.add(myFirstRespos.get("description").toString());
		return des;
	}
	
	
	
	
	public static List<String> getMoreLangage(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		List<String> langages = new ArrayList<String>();
		List<String> langages2 = new ArrayList<String>();
		langages = getLangage(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>();
		if (str!=null) {
			 strList = Arrays.asList(str.split(","));
		
		
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				langages2 = getLangage(link);
				langages.addAll(langages2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
			}else {
				k++;
			}
		}
		}
		return langages;
	}
	
	
	
	public static List<String> getLangage(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"/repos?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		List<String> langages = new ArrayList<String>();
		langages.add(myFirstRespos.get("language").toString());
		return langages;
	}
	
	
	
	public static List<String> getMoreTitre(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		List<String> names = new ArrayList<String>();
		List<String> names2 = new ArrayList<String>();
		names = getTitre(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>();
		if (str!=null) {
			 strList = Arrays.asList(str.split(","));
		
		
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				names2 = getTitre(link);
				names.addAll(names2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
			}else {
				k++;
			}
		}
		}
		return names;
	}
	
	
	
	public static List<String> getTitre(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"/repos?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		List<String> names = new ArrayList<String>();
		names.add(myFirstRespos.get("name").toString());
		return names;
	}
	
	
	
	
	public static List<String> getMoreUrlProjet(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		List<String> htmlurl = new ArrayList<String>();
		List<String> htmlurl2 = new ArrayList<String>();
		htmlurl = getUrlProjet(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		String urlprojet2="";
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>();
		if (str!=null) {
			 strList = Arrays.asList(str.split(","));
		
		
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				htmlurl2 = getUrlProjet(link);
				htmlurl.addAll(htmlurl2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
			}else {
				k++;
			}
		}
		}
		return htmlurl;
	}
	
	
	
	public static List<String> getUrlProjet(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"/repos?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		List<String> htmlurl = new ArrayList<String>();
		htmlurl.add(myFirstRespos.get("html_url").toString());
		return htmlurl;
	}
	
	/*public static HashMap<String, HashMap<String, String>> getRepos(String url) throws Exception{
		HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> hmap2 = new HashMap<String, String>();
		List<String> name = new ArrayList<String>();
		List<String> description = new ArrayList<String>();
		List<String> langage = new ArrayList<String>();
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		//name.add(myFirstRespos.get("name").toString()) ;
		//description.add(myFirstRespos.get("description").toString()) ;
		//langage.add(myFirstRespos.get("language").toString()) ;
		hmap2.put(myFirstRespos.get("language").toString() , myFirstRespos.get("description").toString());
		hmap.put(myFirstRespos.get("name").toString(), hmap2);
		 
		return hmap;
	}*/
	
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
