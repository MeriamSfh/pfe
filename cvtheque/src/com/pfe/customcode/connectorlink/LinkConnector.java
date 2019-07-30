package com.pfe.customcode.connectorlink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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
@CVComponentConfigClass(configClass = LinkConnectorConfig.class)
@CVComponentDescription(value = "Linkedin Connector")
public class LinkConnector extends Connector implements CVComponent {
	
	private Logger logger = Logger.getLogger(LinkConnector.class);
	protected String url ;
	protected String driver ;

	public LinkConnector(LinkConnectorConfig config) throws Exception {
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
			ResultSet res = st.executeQuery("Select login_linkedin from Candidats"); 
			while (res.next()) { 
				
				String login = res.getString("login_linkedin") ; 
				List<String> educ = getEducations(login);
				List<String> exp = getExperiences(login);
				List<String> skills = getSkills(login);
				String url = getUrl(login);
				
				MetaContainer metaList = new MetaContainer();
				metaList.addMeta("login_linkedin", login);
				System.out.println("login :"+ login);
				
				if (educ.size() != 0) {
					for (int i = 0; i < educ.size(); i++) {
						metaList.addMeta("education", educ.get(i)+"/");
						System.out.println("education :"+ educ.get(i));
					}
				}else {
					metaList.addMeta("education", "null");
				}
				if (exp.size() != 0) {
					for (int i = 0; i < exp.size(); i++) {
						metaList.addMeta("experience", exp.get(i)+"/");
						System.out.println("experience :"+ exp.get(i));
					}
				}else {
					metaList.addMeta("experience", "null");
				}
				if (skills.size() != 0) {
				for (int i = 0; i < skills.size(); i++) {
					metaList.addMeta("linkedin_competence", skills.get(i));
					System.out.println("linkedin_competence :"+ skills.get(i));
				}
				}else {
					metaList.addMeta("linkedin_competence", "null");
				}
				metaList.addMeta("linkedin_url", url);
				System.out.println("linkedin_url :"+ url);
				String uri = login;
				papi.addDocument(new Document(uri, "0", partList, metaList));
			}
			res.close();
			st.close();
			conn.close(); 
		}
			catch (Exception e) { 
				logger.info("Error in linkedin : "+e.getMessage(),e); 
				e.printStackTrace();
				}
				
	
	}
	public static String getEmail(String login) throws Exception{
		String res = "";
		String resRepos = parse("https://api.peopledatalabs.com/v4/person?api_key=d60c3331704e825740fcb8a5218be35d12d14415e2de8054da0ee66a58f2c50c&profile=linkedin.com/in/"+login);
		JSONObject myFirstRespos = new JSONObject(resRepos);
		JSONArray arr = new JSONArray();
		arr = myFirstRespos.getJSONObject("data").getJSONObject("primary").getJSONArray("personal_emails"); 
		if (arr.length() != 0) {
			String mail = arr.get(0).toString();
			res = mail ;
		}else {
			res = null;
		}
		return res;
	}
	
	public static List<String> getEducations(String login) throws Exception{
		List<String> res = new ArrayList<String>();
		String resRepos = parse("https://api.peopledatalabs.com/v4/person?api_key=d60c3331704e825740fcb8a5218be35d12d14415e2de8054da0ee66a58f2c50c&profile=linkedin.com/in/"+login);
		JSONObject myFirstRespos = new JSONObject(resRepos);
		JSONArray arr = new JSONArray();
		arr = myFirstRespos.getJSONObject("data").getJSONArray("education");
		for (int i = 0; i < arr.length(); i++) {
			String school = arr.getJSONObject(i).get("school").toString();
			JSONArray arrdegree = arr.getJSONObject(i).getJSONArray("degrees");
			if ((arrdegree.length()!= 0) && (school != null)) {
				res.add(school+" : "+arrdegree.get(0).toString());
			}
		}
		return res;
	}
	
	public static List<String> getExperiences(String login) throws Exception{
		List<String> res = new ArrayList<String>();
		String resRepos = parse("https://api.peopledatalabs.com/v4/person?api_key=d60c3331704e825740fcb8a5218be35d12d14415e2de8054da0ee66a58f2c50c&profile=linkedin.com/in/"+login);
		JSONObject myFirstRespos = new JSONObject(resRepos);
		JSONArray arr = new JSONArray();
		arr = myFirstRespos.getJSONObject("data").getJSONArray("experience");
		for (int i = 0; i < arr.length(); i++) {
			Object comp =  arr.getJSONObject(i).get("company");
			if (!comp.equals(null)) {
				res.add(arr.getJSONObject(i).getJSONObject("title").getString("name")+" : "+((JSONObject) comp).getString("name"));
				
			}
			}
		return res;
	}
	
	public static List<String> getSkills(String login) throws Exception{
		List<String> res = new ArrayList<String>();
		String resRepos = parse("https://api.peopledatalabs.com/v4/person?api_key=d60c3331704e825740fcb8a5218be35d12d14415e2de8054da0ee66a58f2c50c&profile=linkedin.com/in/"+login);
		JSONObject myFirstRespos = new JSONObject(resRepos);
		JSONArray arr = new JSONArray();
		arr = myFirstRespos.getJSONObject("data").getJSONArray("skills");
		for (int i = 0; i < arr.length(); i++) {
			res.add(arr.getJSONObject(i).getString("name"));
		}
		return res;
	}
	
	public static String getUrl(String login) throws Exception{
		String resRepos = parse("https://api.peopledatalabs.com/v4/person?api_key=d60c3331704e825740fcb8a5218be35d12d14415e2de8054da0ee66a58f2c50c&profile=linkedin.com/in/"+login);
		JSONObject myFirstRespos = new JSONObject(resRepos);
		JSONArray arr = new JSONArray();
		arr = myFirstRespos.getJSONObject("data").getJSONArray("profiles");
		String url = arr.getJSONObject(0).getString("url");
		return url;
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
