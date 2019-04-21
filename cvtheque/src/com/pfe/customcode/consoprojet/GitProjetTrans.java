package com.pfe.customcode.consoprojet;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("Git projet Transformation Processor")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=GitProjetTransConfig.class)
public class GitProjetTrans implements IJavaAllUpdatesTransformationProcessor {

	Logger LOGGER = Logger.getLogger(GitProjetTrans.class);
	public GitProjetTrans(GitProjetTransConfig config){
		
	}
	@Override
	public String getTransformationDocumentType() {
		return "projet";
	}

	@Override
	public void process(IJavaAllUpdatesTransformationHandler handler, IMutableTransformationDocument document) throws Exception {
		final String login = document.getUri();
		if (login == null || login.isEmpty()) {
            throw new Exception("login name not available");
        }
        final String[] values = login.split("\\.");
        LOGGER.info(values);
        if (values == null || values.length == 0) {
            throw new Exception("Invalid git name");
        }
        LOGGER.info("doc uri:[" + document.getUri() + "] login_git:[" + values[0] + "]");

        document.addArcTo("projet", "login_git=" + values[0] );
	}

}
