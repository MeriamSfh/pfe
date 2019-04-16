package com.pfe.customcode.connectorgit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

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
@CVComponentConfigClass(configClass = DbConneectorConfig.class)
@CVComponentDescription(value = "test")
public class DbConneector extends Connector implements CVComponent {

	private Logger logger = Logger.getLogger(DbConneector.class);
	protected String url ;
	protected String driver ;
	public DbConneector(DbConneectorConfig config) throws Exception {
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
				MetaContainer metaList = new MetaContainer();
				String login = res.getString("login_git") ;  
				System.out.println("login ????: "+login);
				metaList.addMeta("logintest", login);
				String uri = login;
				papi.addDocument(new Document(uri, "0", partList, metaList));
			}
			res.close();
			st.close();
			conn.close(); 
			} catch (Exception e) { 
				logger.info("error in db"); 
				}
	}

}
