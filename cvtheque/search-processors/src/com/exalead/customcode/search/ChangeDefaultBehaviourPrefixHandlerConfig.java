package com.exalead.customcode.search;

import com.exalead.config.bean.IsMandatory;
import com.exalead.mercury.component.config.CVComponentConfig;

public class ChangeDefaultBehaviourPrefixHandlerConfig implements CVComponentConfig {

    public String operator = "OR";
    
    public String getOperator(){ 
        return this.operator; 
    }
    @IsMandatory(true)
    public void setOperator(String operator){
        this.operator=operator;
    }
    
    
    
}
