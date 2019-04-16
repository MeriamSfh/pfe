package com.pfe.customcode.papifilterStack;

import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;
@PropertyLabel(value = "Stack")
@CVComponentConfigClass(configClass = StackPAPIFilterConfig.class)
public class StackPAPIFilterConfig extends DefaultImpl {

	public StackPAPIFilterConfig() throws Exception {
		setComponentClassName(StackPAPIFilter.class.getName());
	}

}
