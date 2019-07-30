package com.pfe.customcode.consotwitter;

import java.util.List;

import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IAggregationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.GraphMatchHelpers;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationProcessor;
import com.exalead.mercury.component.config.CVComponentConfigClass;

@CVComponentConfigClass(configClass=TwitterAggProcConfig.class)
public class TwitterAggProc implements IJavaAllUpdatesAggregationProcessor {

	Logger LOGGER = Logger.getLogger(TwitterAggProc.class);
	public TwitterAggProc(final TwitterAggProcConfig config) {
	}
	
	@Override
	public String getAggregationDocumentType() {
		return "twitter";
	}

	@Override
	public void process(IJavaAllUpdatesAggregationHandler handler, IAggregationDocument document) throws Exception {

		final List<IAggregationDocument> pathstweets = GraphMatchHelpers.getPathsEnd(handler.match(document, "ref_tweet"));
        for (IAggregationDocument tweet : pathstweets) {
            LOGGER.info("tweet found: " + tweet.getUri()+" /// "+ "twitter found: "+document.getUri());
            String tweet_text = tweet.getMeta("tweet_text");
            document.withMeta("tweets", tweet_text);
            LOGGER.info("tweet metas: " + document.getMeta("tweet"));
        }
	}

}
