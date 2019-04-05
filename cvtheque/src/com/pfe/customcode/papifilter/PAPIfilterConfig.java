package com.pfe.customcode.papifilter;

import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;

@CVComponentConfigClass(configClass = PAPIfilterConfig.class)
public class PAPIfilterConfig extends DefaultImpl {

	public PAPIfilterConfig() throws Exception {
		setComponentClassName(PAPIfilter.class.getName());
	}

}
