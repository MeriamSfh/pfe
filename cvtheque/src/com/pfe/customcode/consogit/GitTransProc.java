package com.pfe.customcode.consogit;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("github Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=GitTransProcConfig.class)
public class GitTransProc implements IJavaAllUpdatesTransformationProcessor {
	
	Logger LOGGER = Logger.getLogger(GitTransProc.class);
	public GitTransProc(final GitTransProcConfig config) {
    }

    @Override
    public String getTransformationDocumentType() {
        return "projet";
    }

    @Override
    public void process(final IJavaAllUpdatesTransformationHandler handler,
                         final IMutableTransformationDocument document) throws Exception {

        final String projetUri = document.getUri();
        final String[] values = projetUri.split("\\.");
        
        document.addArcFrom("ref_projet", values[0]);
    }
       


}
