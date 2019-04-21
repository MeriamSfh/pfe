package com.pfe.customcode.papifilterStack;

import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.PushAPIFilterConfig.DefaultImpl;

@CVComponentConfigClass(configClass = QuestPapiConfig.class)
public class QuestPapiConfig extends DefaultImpl {

	public QuestPapiConfig() throws Exception {
		setComponentClassName(QuestPapi.class.getName());
	}

}
