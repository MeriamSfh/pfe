package com.pfe.customcode.consotwitter;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("twitter Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=TwitterTransProcConfig.class)
public class TwitterTransProc implements IJavaAllUpdatesTransformationProcessor {

	public TwitterTransProc(final TwitterTransProcConfig config) {
	}
	@Override
	public String getTransformationDocumentType() {
		return "tweets";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler arg0, IMutableTransformationDocument document)
			throws Exception {
		final String tweetsUri = document.getUri();
		 final String[] values = tweetsUri.split("\\.");
	        
	        document.addArcFrom("ref_tweet", values[0]);
	}

}
