package com.pfe.customcode.connectorgit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.MetaContainer;
import com.exalead.papi.helper.PartContainer;
import com.exalead.papi.helper.PushAPI;

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = RepositoryConnectorConfig.class)
@CVComponentDescription(value = "repositories connector")
public class RepositoryConnector extends Connector implements CVComponent {
	
	private Logger logger = Logger.getLogger(RepositoryConnector.class);
	protected String url ;
	protected String driver ;

	public RepositoryConnector(RepositoryConnectorConfig config) throws Exception {
		super(config);
		url = config.getUrl();
		driver = config.getDriver();
	}

	@Override
	public void scan(PushAPI papi, String scanMode, Object scanModeConfig) throws Exception {
		
		PartContainer partList = new PartContainer();
		try { 
			Class.forName(driver).newInstance(); 
			Connection conn = DriverManager.getConnection(url); 
			Statement st = conn.createStatement(); 
			ResultSet res = st.executeQuery("Select login_git from Candidats"); 
			while (res.next()) { 
				
				String login = res.getString("login_git") ;  
				List<String> titre = getMoreTitre(login);
				List<String> langage = getMoreLangage(login);
				List<String> des = getMoredescription(login);
				List<String> htmlurl= getMoreUrlProjet(login) ;
				
				for (int i = 0; i < titre.size(); i++) {
					MetaContainer metaList = new MetaContainer();
					metaList.addMeta("url_projet", htmlurl.get(i));
					metaList.addMeta("login_git", login);
					metaList.addMeta("titre", titre.get(i));
					metaList.addMeta("description", des.get(i));
					metaList.addMeta("langage", langage.get(i));
					String uri = login+"."+titre.get(i);
					papi.addDocument(new Document(uri, "0", partList, metaList));
				}
			}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in db"); 
				}
	}
	
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
	
	
	
	public static List<String> getdescription(String url) throws Exception{
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
	
	
	
	public static List<String> getLangage(String url) throws Exception{
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
	
	
	
	public static List<String> getTitre(String url) throws Exception{
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
	
	
	
	public static List<String> getUrlProjet(String url) throws Exception{
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		List<String> htmlurl = new ArrayList<String>();
		htmlurl.add(myFirstRespos.get("html_url").toString());
		return htmlurl;
	}
	
	
	
	public static String getApiRepos(String url) throws Exception{
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String repos_url = myResponse.get("repos_url").toString();
		return repos_url;
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
