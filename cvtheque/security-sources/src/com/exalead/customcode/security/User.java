package com.exalead.customcode.security;

import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyLabel;

/**
 * Configuration for one user of the static security source.
 */
public class User {
    protected String[] tokens;
    protected String login;
    protected String password;
    protected String displayName=null;

    public String getLogin() {
        return login;
    }

    @IsMandatory(true)
    @PropertyLabel("Login of the user")
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    @IsMandatory(true)
    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTokens() {
        return tokens;
    }

    @IsMandatory(true)
    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public String getDisplayName() {
        return displayName;
    }

    @IsMandatory(false)
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}