package com.exalead.customcode.connectors.staticdata;

import com.exalead.config.bean.BeeKeyValueType;
import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyDescription;
import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.security.RSAConfigKey;
import com.exalead.papi.framework.connectors.ConnectorConfig;

@PropertyLabel("Informatica PowerCenter")
public class SampleConnectorConfig extends ConnectorConfig {
    private SampleDocument[] docs;
    private String userName;
    private String password;

    public SampleConnectorConfig() {
        // FIXME remove this once the ConnectorConfig Constructor does not fill the filter list
        this.customPushAPIFilters = null;
    }

    /**
     * set the user name used to login
     *
     * @param userName
     *            an user name
     */
    @IsMandatory(true)
    @PropertyLabel("User name")
    @PropertyDescription("Fake username (not used)")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * set the password used to login
     *
     * @param password
     *            a password
     */
    @IsMandatory(true)
    @PropertyLabel("Password")
    @BeeKeyValueType("encrypted")
    @PropertyDescription("Fake password (not used)")
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Documents pushed to the CloudView
     *
     * @param docs
     *            list of document configurations
     */
    @IsMandatory(true)
    @PropertyLabel("Documents")
    @PropertyDescription("Pushed Documents")
    public void setDocs(SampleDocument[] docs) {
		this.docs = docs;
	}

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return RSAConfigKey.decryptIfEncryptedOnly(password);
    }

    public SampleDocument[] getDocs() {
		return docs;
	}

}
