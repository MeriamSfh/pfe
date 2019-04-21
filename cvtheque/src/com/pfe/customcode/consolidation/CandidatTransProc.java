package com.pfe.customcode.consolidation;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("candidat Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=CandidatTransProcConfig.class)
public class CandidatTransProc implements IJavaAllUpdatesTransformationProcessor{
	
	Logger LOGGER = Logger.getLogger(CandidatTransProc.class);
	
	 public CandidatTransProc(final CandidatTransProcConfig config ) {
	}

	@Override
	public String getTransformationDocumentType() {
		return "candidat";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler handler, IMutableTransformationDocument document)
			throws Exception {
		final String nom = document.getMeta("nom");
    	final String prenom = document.getMeta("prenom");
    	final String login_git = document.getMeta("login_git");
    	final String login_stackoverflow = document.getMeta("login_stackoverflow");
    	final String login_twitter = document.getMeta("login_twitter");
        
        document.addArcTo("curriculum vitae", "/%2Fhome%2Fmsfaihi%2Finstall%2Fonecall-2016x.R1.11820-linux-x64%2Fdoc%2Fcv_sample/"+ nom.trim() + "."+ prenom.trim()+".pdf");
        document.addArcTo("github account", login_git);
        document.addArcTo("stackoverflow account", login_stackoverflow);
        document.addArcTo("twitter account", login_twitter);
        
	}

}
