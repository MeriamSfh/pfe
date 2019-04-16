package com.pfe.customcode.consogit;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IMutableTransformationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesTransformationHandler;
import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.cloudview.consolidationapi.PushAPITransformationHelpers;

@CVComponentDescription(value = "git transformation pro")
public class GitConso extends PushAPITransformationHelpers implements CVComponent {
	
	public void process(IJavaAllUpdatesTransformationHandler handler, IMutableTransformationDocument document) throws Exception {
		
		Logger LOGGER = Logger.getLogger(GitConso.class);
	
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

    document.addArcFrom("describedBy", "candidat_id=" + values[0] + "&");
}


}
