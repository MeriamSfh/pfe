package com.pfe.customcode.connectorgit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
@CVComponentConfigClass(configClass = GitConnectorConfig.class)
@CVComponentDescription(value = "github connector")
public class GitConnector extends Connector implements CVComponent {

	private Logger logger = Logger.getLogger(GitConnector.class);
	protected String url ;
	protected String driver ;
	public GitConnector(GitConnectorConfig config) throws Exception {
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
				int followers = getfollowers(login);
				int following = getfollowing(login);
				int nbre = getNbreRepos(login);
				String link = getLink(login);
				MetaContainer metaList = new MetaContainer();
				metaList.addMeta("login_git", login);
				metaList.addMeta("followers", Integer.toString(followers));
				metaList.addMeta("following", Integer.toString(following));
				metaList.addMeta("total_projets", Integer.toString(nbre));
				metaList.addMeta("url", link);
				String uri = login;
				papi.addDocument(new Document(uri, "0", partList, metaList));
				
				
			}
				res.close();
				st.close();
				conn.close(); 
				} catch (Exception e) { 
					logger.info("error in github"); 
					}
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
	
	public static String getLink(String login) throws Exception{
		String url = "https://api.github.com/users/"+login+"?client_id=11de3e641df591747467&client_secret=92cd066854ae4cb568f793f926a8789fbb7ce73c";
		String res = parse(url);
		JSONObject myResponse = new JSONObject(res);
		String link = myResponse.get("html_url").toString();
		return link;
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
