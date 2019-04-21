package com.pfe.customcode.consoprojet;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("Candidat Git Transformation Processor")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=CandidatGitTransConfig.class)
public class CandidatGitTrans implements IJavaAllUpdatesTransformationProcessor {
	
	Logger LOGGER = Logger.getLogger(CandidatGitTrans.class);
	
	public CandidatGitTrans(final CandidatGitTransConfig config){
		
	}

	@Override
	public String getTransformationDocumentType() {
		return "candidat";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler handler, IMutableTransformationDocument document) throws Exception {
		final String login = document.getMeta("login_git");
        if (login == null || login.isEmpty()) {
            throw new Exception("login git not available");
        }
       
        LOGGER.info("doc uri:[" + document.getUri() + "] login_git:[" + login + "]");

        document.addArcTo("github account", login);
		
	}

}
