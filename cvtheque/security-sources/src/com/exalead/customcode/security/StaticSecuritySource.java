package com.exalead.customcode.security;

import java.util.ArrayList;
import java.util.List;

import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.security.sources.common.AuthenticationResult;
import com.exalead.security.sources.common.SecurityException;
import com.exalead.security.sources.common.SecuritySource;
import com.exalead.security.sources.common.SecurityToken;

import exa.bee.KeyValue;

/** 
 * This sample security source has a configuration made of a static list of users and associated tokens.
 * It demonstrates some of the capabilities of the configuration system in terms of hierarchical configuration.
 */
@CVComponentDescription("(Sample) Static list")
@CVComponentConfigClass(configClass = StaticSecuritySourceConfig.class)
public class StaticSecuritySource extends SecuritySource implements CVComponent {

    protected StaticSecuritySourceConfig config;

    public StaticSecuritySource(StaticSecuritySourceConfig config) {
        this.config=config;
    }

    /**
     * This is the main entry point of a security source.
     * It must perform two things:
     *   - Checking that the login/password couple is valid (authentication)
     *   - Filling-in the security tokens for this user (authorization).
     *   
     * If checkPassword is false, then the security source should only check that the user is valid.
     */
    @Override
    public AuthenticationResult authenticate(String login, String password, boolean checkPassword) throws SecurityException {
        AuthenticationResult result = new AuthenticationResult();

        for(User user : config.getUsers()) {
            if(user.getLogin().equalsIgnoreCase(login)) {
                //check password if necessary
                if(checkPassword) {
                    if(!user.getPassword().equals(password)) {
                        return result.withSuccess(false).withCause("invalid password for user: "+login);
                    }
                }

                // add tokens to result
                List<SecurityToken> tokens=new ArrayList<SecurityToken>();
                for(String token : user.getTokens()) {
                    tokens.add(new SecurityToken(token));
                }
                result.securityToken = tokens;

                // generate user ID
                result.setUserId("Exalead-Security-Sample:"+login);
                // Generate a nice display name
                result.setUserDisplayName((user.getDisplayName()==null)?user.getLogin():user.getDisplayName());
                result.setSuccess(true);
                return result;
            }
        }
        return result.withSuccess(false).withCause("can not find user : "+login);
    }

    /**
     * This optional method can return various information about the security source.
     * Here, we choose to return the list of users. Only administrators can call this.
     */
    @Override
    public List<KeyValue> getInfo(List<KeyValue> params) throws SecurityException  {
        List<KeyValue> info = new ArrayList<KeyValue>();
        int i = 0;
        for (User user: config.getUsers()) {
            info.add(new KeyValue("user" + i++, user.getLogin()));
        }
        return info;
    }

    @Override
    public void reset() {
    }
}