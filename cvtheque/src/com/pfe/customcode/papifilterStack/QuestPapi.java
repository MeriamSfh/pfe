package com.pfe.customcode.papifilterStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@PropertyLabel(value = "Label")
@CVComponentConfigClass(configClass = QuestPapiConfig.class)
@CVComponentDescription(value = "quest papi")
public class QuestPapi extends PipedPushAPI implements CVComponent, PushAPIFilter {

	private Logger logger = Logger.getLogger(QuestPapi.class);
	public QuestPapi(PushAPI parent, QuestPapiConfig config) {
		super(parent);
	}
	
	@Override
	public void addDocument(Document document) throws PushAPIException{
		String login = document.getMetaContainer().getMeta("login_stackoverflow").getValue();

		try {
			List<String> ques = parseQuestions(login);
			for (int i = 0; i < ques.size(); i++) {
				document.addMeta("questions", ques.get(i) );
			}
		} catch (Exception e) {
			logger.info("error in parsing quest stack");
		}
		this.parent.addDocument(document);
		}
	
	public static List<String> parseQuestions(String login) throws IOException, JSONException{
		List<String> ques = new ArrayList<String>();
			String url = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page=1&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
			byte[] result = parse(url);
			String message = new String(result, "UTF-8");
		    JSONObject myResponse = new JSONObject(message);
		    JSONArray arr1 = new JSONArray();
			arr1 = myResponse.getJSONArray("items");
			ques.add(arr1.getJSONObject(0).get("link").toString());
		    Boolean more = myResponse.getBoolean("has_more");
		    int p=2;
		    while (more==true && p<10) {
		    	String	res = "https://api.stackexchange.com/2.2/users/"+login+"/questions?page="+p+"&pagesize=1&order=desc&sort=activity&site=stackoverflow&key=ocsWH7idlVKJNNQIKAeVSQ((";
				byte[] moreResult = parse(res);
				String mess = new String(moreResult, "UTF-8");
				JSONObject myResp = new JSONObject(mess);
				JSONArray arr = new JSONArray();
				arr = myResp.getJSONArray("items");
				ques.add(arr.getJSONObject(0).get("link").toString());
			    
			    p++;
		    }
		    return ques;	
		}
	
	public static byte[] parse(String url) throws IOException, JSONException{
		String baseURI = url;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource wr = client.resource(baseURI); 
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		byte[] readBuffer = new byte[500000];
		GZIPInputStream inputStream = new GZIPInputStream(response.getEntityInputStream());
	    int read = inputStream.read(readBuffer,0,readBuffer.length);
	    inputStream.close();
	    byte[] result = Arrays.copyOf(readBuffer, read);
	    return result;
	}
	
	@Override
	public void addDocumentList(Document[] documents) throws PushAPIException {
		for (Document document : documents) {
			addDocument(document);
		}
	}

}
