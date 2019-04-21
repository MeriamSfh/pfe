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
        return "candidat";
    }

    @Override
    public void process(final IJavaAllUpdatesTransformationHandler handler,
                         final IMutableTransformationDocument document) throws Exception {
    	
        /*final String filename = document.getMeta("file_name");
        if (filename == null || filename.isEmpty()) {
            throw new Exception("File name not available");
        }
        final String[] values = filename.split("\\.");
        LOGGER.info(values);
        if (values == null || values.length == 0) {
            throw new Exception("Invalid file name");
        }
        LOGGER.info("doc uri:[" + document.getUri() + "] candidatId:[" + values[0]+values[1] + "]");

        document.addArcFrom("curriculum vitae", "nom=" + values[0] +"+&"+"prenom="+values[1]+ "&");*/
    	
    	
    	
    	
    	final String nom = document.getMeta("nom");
    	final String prenom = document.getMeta("prenom");
    	final String login = document.getMeta("login_git");
        
        LOGGER.info("doc uri:[" + document.getUri() + "] cvUrl:[" + "/%2Fhome%2Fmsfaihi%2Finstall%2Fonecall-2016x.R1.11820-linux-x64%2Fdoc%2Fcv_sample/"+ nom + "."+ prenom+"]");

        document.addArcTo("curriculum vitae", "/%2Fhome%2Fmsfaihi%2Finstall%2Fonecall-2016x.R1.11820-linux-x64%2Fdoc%2Fcv_sample/"+ nom.trim() + "."+ prenom.trim()+".pdf");
        document.addArcTo("github account", login);
    }
    

}
