package com.pfe.customcode.papifilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilter;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.PushAPI;
import com.exalead.papi.helper.PushAPIException;
import com.exalead.papi.helper.pipe.PipedPushAPI;

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = GitPapifilterConfig.class)
@CVComponentDescription(value = "Git papi filter")
public class GitPapifilter extends PipedPushAPI implements CVComponent, PushAPIFilter {

	private Logger logger = Logger.getLogger(GitPapifilter.class);
	public GitPapifilter(PushAPI parent, GitPapifilterConfig config) {
		super(parent);
	}
	
	@Override
	public void addDocument(Document document) throws PushAPIException {
		String login_git = document.getMetaContainer().getMeta("login_git").getValue();
		HashMap<String, String> map = new HashMap<String, String>();
		String link = "";
		int following = 0;
		int followers = 0;
		int nbreRepos = 0;
		List<String> titles = new ArrayList<String>();
		List<String> langages = new ArrayList<String>();
		
		try {
			 link = getLink(login_git);
			 following = getfollowing(login_git);
			 followers = getfollowers(login_git);
			 nbreRepos = getNbreRepos(login_git);
			 map = getMore(login_git);
			 for (Entry<String, String> entry : map.entrySet()) {
		    	  titles.add(entry.getKey()) ;
		    	  langages.add(entry.getValue()) ;
				}
			
		} catch (Exception e) {
			logger.info("error !!! ");
		}
		document.addMeta("linkgit", link);
		document.addMeta("following", Integer.toString(following));
		document.addMeta("followers", Integer.toString(followers));
		document.addMeta("nombrerepositories", Integer.toString(nbreRepos));
		 int i=0;
	      while (i<titles.size()) {
			String title = titles.get(i);
			String lang = langages.get(i);
			document.addMeta("repository", title+" langage: "+lang);
			i++;
	      }
		this.parent.addDocument(document);
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
	
	public static HashMap<String, String> getMore(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		map = getRepos(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>(Arrays.asList(str.split(",")));
		int k = 0;
		while (k<strList.size()) {
			String text = strList.get(k);
			if (text.contains("next")==true) {
				String link = text.substring(text.indexOf("<")+1, text.indexOf(">"));
				map2 = getRepos(link);
				map.putAll(map2);
				URL obj2 = new URL(link);
				URLConnection conn2 = obj2.openConnection();
				String str2 = conn2.getHeaderField("Link");
				 strList = Arrays.asList(str2.split(","));
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

@Override
public void addDocumentList(Document[] documents) throws PushAPIException {
	for (Document document : documents) {
		addDocument(document);
	}
}
	

}
