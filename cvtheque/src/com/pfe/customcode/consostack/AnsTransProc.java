package com.pfe.customcode.consostack;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("answer Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=AnsTransProcConfig.class)
public class AnsTransProc implements IJavaAllUpdatesTransformationProcessor {

	public AnsTransProc(AnsTransProcConfig config) {
	}
	@Override
	public String getTransformationDocumentType() {
		return "answer";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler arg0, IMutableTransformationDocument document)
			throws Exception {
			final String ansuri = document.getUri();
			final String[] values = ansuri.split("\\.");
	        document.addArcFrom("ref_answer", values[0]);		
	}

}
