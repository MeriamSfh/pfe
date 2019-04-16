package com.pfe.customcode.papifilter;

import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;

@CVComponentConfigClass(configClass = GitPapifilterConfig.class)
public class GitPapifilterConfig extends DefaultImpl {

	public GitPapifilterConfig() throws Exception {
		setComponentClassName(GitPapifilter.class.getName());
	}

}
