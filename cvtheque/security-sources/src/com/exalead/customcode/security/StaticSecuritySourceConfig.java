package com.exalead.customcode.security;

import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyLabel;
import com.exalead.security.sources.common.SecuritySourceConfig;

public class StaticSecuritySourceConfig extends SecuritySourceConfig {
    protected User[] users;

    public User[] getUsers() {
        return users;
    }

    @IsMandatory(true)
    @PropertyLabel("Allowed users")
    public void setUsers(User[] users) {
        this.users = users;
    }
}