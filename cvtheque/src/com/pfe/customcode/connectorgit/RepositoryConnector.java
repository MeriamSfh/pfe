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
			String loginnnn = res.getString("login_git") ;  
			System.out.println("login projet: "+loginnnn);
			while (res.next()) { 
				
				String login = res.getString("login_git") ;  
				System.out.println("login projet: "+login);
				HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
				hmap = getMore(login);
				List<String> names = new ArrayList<String>();
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
					MetaContainer metaList = new MetaContainer();
					metaList.addMeta("login_git", login);
					metaList.addMeta("titre", names.get(i));
					metaList.addMeta("description", description.get(i));
					metaList.addMeta("langage", langages.get(i));
					String uri = login+"."+names.get(i);
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
	public static HashMap<String, HashMap<String, String>> getMore(String login) throws Exception  {
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String urlRepos =getApiRepos(url);
		
		
		HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> hmap2 = new HashMap<String, HashMap<String, String>>();
		
		hmap = getRepos(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		
		URL obj = new URL(urlRepos+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c&page=1&per_page=1");
		URLConnection conn = obj.openConnection();
		String str = conn.getHeaderField("Link");
		List<String> strList = new ArrayList<String>(Arrays.asList(str.split(",")));
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
		return hmap;
	}
	
	public static HashMap<String, HashMap<String, String>> getRepos(String url) throws Exception{
		HashMap<String, HashMap<String, String>> hmap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> hmap2 = new HashMap<String, String>();
		String resRepos = parse(url);
		JSONObject myFirstRespos = new JSONObject(resRepos.substring(resRepos.indexOf('{')));
		hmap2.put(myFirstRespos.get("language").toString() , myFirstRespos.get("description").toString());
		hmap.put(myFirstRespos.get("name").toString(), hmap2);
		 
		return hmap;
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
