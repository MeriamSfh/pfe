package com.pfe.customcode.papifilterStack;

import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;

@CVComponentConfigClass(configClass = PapiFilterConfig.class)
public class PapiFilterConfig extends DefaultImpl {

	public PapiFilterConfig() throws Exception {
		setComponentClassName(PapiFilter.class.getName());
	}

}
