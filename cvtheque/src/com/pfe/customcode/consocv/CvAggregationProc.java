package com.pfe.customcode.consocv;

import java.util.List;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IAggregationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.GraphMatchHelpers;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationProcessor;
import com.exalead.mercury.component.config.CVComponentConfigClass;

@CVComponentConfigClass(configClass=CvAggregationProcConfig.class)
public final  class CvAggregationProc implements IJavaAllUpdatesAggregationProcessor {
	Logger LOGGER = Logger.getLogger(CvAggregationProc.class);
	public CvAggregationProc(final CvAggregationProcConfig config) {
    }

    @Override
    public String getAggregationDocumentType() {
        return "candidat";
    }

    @Override
    public void process(IJavaAllUpdatesAggregationHandler handler, IAggregationDocument document) throws Exception {
           final String candidatName = document.getMeta("prenom");
           if (candidatName == null || candidatName.length() == 0) {
               throw new Exception("Invalid candidat name '" + candidatName + "'");
           }

           LOGGER.info("candidat found: " + candidatName);

           // find document related to the country
           // Goal: be able to consolidate information of pdf document with country database record
           final List<IAggregationDocument> pathsEnds = GraphMatchHelpers.getPathsEnd(handler.match(document, "curriculum vitae"));
           for (IAggregationDocument file : pathsEnds) {
        	 //  String text = file.getMeta("invisibletext");
        	   
        	   
        	   document.withMetas(file.getAllMetas());
               document.withPart("master", file.getPart("master"));
               document.withMeta("hasCV", "yes");
           }
   }
}
