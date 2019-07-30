package com.pfe.customcode.consostack;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("stack Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=StackTransProcConfig.class)
public class StackTransProc implements IJavaAllUpdatesTransformationProcessor {
	Logger LOGGER = Logger.getLogger(StackTransProc.class);
	
	public  StackTransProc(StackTransProcConfig config) {
	}

	@Override
	public String getTransformationDocumentType() {
		// TODO Auto-generated method stub
		return "question";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler arg0, IMutableTransformationDocument document)
			throws Exception {
		final String quesuri = document.getUri();
		 final String[] values = quesuri.split("\\.");
	        
	        document.addArcFrom("ref_question", values[0]);
	}

}
