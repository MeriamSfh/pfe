package com.pfe.customcode.connectorgit;

import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyLabel;
import com.exalead.papi.framework.connectors.ConnectorConfig;

public class GitConnectorConfig extends ConnectorConfig {
	protected String url = null; 
	protected String driver = null;
	
	
	public String getUrl() {
		return url;
	}
	
	@IsMandatory(false)
    @PropertyLabel("url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDriver() {
		return driver;
	}
	
	@IsMandatory(false)
    @PropertyLabel("driver")
	public void setDriver(String driver) {
		this.driver = driver;
	}


}
