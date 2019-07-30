package com.pfe.customcode.connectorlink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



public class Test {

	public static void main(String[] args) throws Exception {
		List<String> list = getExperiences("nessrine-razouane-93831155");
		System.out.println("Experiences: "+list);
		System.out.println("exper !!"+ list.size());
		List<String> educ = getEducations("nessrine-razouane-93831155");
		
			System.out.println("Educations: "+educ);
			System.out.println("educ !!"+ educ.size());
			System.out.println(educ.size()!=0);
		
		
		List<String> skill = getSkills("nessrine-razouane-93831155");
		System.out.println("Skills: "+skill);
		System.out.println("skill !!"+ skill.size());
		String mail = getEmail("nessrine-razouane-93831155");
		System.out.println("mail: "+mail);
		String url = getUrl("nessrine-razouane-93831155");
		System.out.println("url: "+url);
	
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
