package com.pfe.customcode.consogit;

import java.util.List;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IAggregationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.GraphMatchHelpers;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationProcessor;
import com.exalead.mercury.component.config.CVComponentConfigClass;

@CVComponentConfigClass(configClass=GitAggProcConfig.class)
public class GitAggProc implements IJavaAllUpdatesAggregationProcessor {

	Logger LOGGER = Logger.getLogger(GitAggProc.class);
	public GitAggProc(final GitAggProcConfig config) {
	}
	@Override
	public String getAggregationDocumentType() {
		return "git";
	}

	@Override
	public void process(IJavaAllUpdatesAggregationHandler handler, IAggregationDocument document) throws Exception {
		final List<IAggregationDocument> pathsprojet = GraphMatchHelpers.getPathsEnd(handler.match(document, "ref_projet"));
        for (IAggregationDocument projet : pathsprojet) {
            //LOGGER.info("projet found: " + projet.getUri()+" /// "+ "git found: "+document.getUri());
            //document.withMetas(projet.getAllMetas());
            String titre = projet.getMeta("titre");
            String description = projet.getMeta("description");
            String langage = projet.getMeta("langage");
            document.withMeta("description_projet", titre);
            document.withMeta("langage_projet", description);
            document.withMeta("titre_projet", langage);
            
        }		
	}

}
