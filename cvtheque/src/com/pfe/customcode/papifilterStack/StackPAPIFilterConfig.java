package com.pfe.customcode.papifilterStack;


import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;

@CVComponentConfigClass(configClass = StackPAPIFilterConfig.class)
public class StackPAPIFilterConfig extends DefaultImpl {

	public StackPAPIFilterConfig() throws Exception {
		setComponentClassName(StackPAPIFilter.class.getName());
	}

}
