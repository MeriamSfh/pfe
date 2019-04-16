package com.pfe.customcode.consocv;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.plugin.CVPluginVersion;

@CVComponentDescription("cv Transformation Processor Component")
@CVPluginVersion("1.0")
@CVComponentConfigClass(configClass=CvTransProcConfig.class)
public class CvTransProc implements IJavaAllUpdatesTransformationProcessor {
	Logger LOGGER = Logger.getLogger(CvTransProc.class);
	public CvTransProc(final CvTransProcConfig config) {
    }

    @Override
    public String getTransformationDocumentType() {
        return "cv";
    }

    @Override
    public void process(final IJavaAllUpdatesTransformationHandler handler,
                         final IMutableTransformationDocument document) throws Exception {
    	
    	document.setType("document");
        final String filename = document.getMeta("file_name");
        if (filename == null || filename.isEmpty()) {
            throw new Exception("File name not available");
        }
        final String[] values = filename.split("\\.");
        LOGGER.info(values);
        if (values == null || values.length == 0) {
            throw new Exception("Invalid file name");
        }
        LOGGER.info("doc uri:[" + document.getUri() + "] candidatId:[" + values[0] + "]");

        document.addArcFrom("describedBy", "nom=" + values[0] +"+&"+"prenom="+values[1]+ "&");
    }

}
