package com.pfe.customcode.consolidation;

import java.util.List;
import org.apache.log4j.Logger;

import com.exalead.cloudview.consolidationapi.processors.IAggregationDocument;
import com.exalead.cloudview.consolidationapi.processors.java.GraphMatchHelpers;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationHandler;
import com.exalead.cloudview.consolidationapi.processors.java.IJavaAllUpdatesAggregationProcessor;
import com.exalead.mercury.component.config.CVComponentConfigClass;

@CVComponentConfigClass(configClass=CandidatAggProcConfig.class)
public final class CandidatAggProc implements IJavaAllUpdatesAggregationProcessor {

	Logger LOGGER = Logger.getLogger(CandidatAggProc.class);
	public CandidatAggProc(final CandidatAggProcConfig config) {
	}
	
	@Override
	public String getAggregationDocumentType() {
		return "candidat";
	}

	@Override
	public void process(IJavaAllUpdatesAggregationHandler handler, IAggregationDocument document) throws Exception {
		final String prenom = document.getMeta("prenom");
        if (prenom == null || prenom.length() == 0) {
            throw new Exception("Invalid candidat name '" + prenom + "'");
        }

        final List<IAggregationDocument> pathsgit = GraphMatchHelpers.getPathsEnd(handler.match(document, "github account"));
        for (IAggregationDocument git : pathsgit) {
        	
        	//document.withMetas(git.getAllMetas());
        	String followers = git.getMeta("followers");
        	String following = git.getMeta("following");
        	String link = git.getMeta("link");
        	String total_projets = git.getMeta("total_projets");
    		document.withMeta("git_followers", followers);
    		document.withMeta("git_following", following);
    		document.withMeta("git_link", link);
    		document.withMeta("total_projets", total_projets);
    		
        	final List<IAggregationDocument> pathsprojet = GraphMatchHelpers.getPathsEnd(handler.match(git, "ref_projet"));
        	for (IAggregationDocument projet : pathsprojet) {
           // LOGGER.info("candidat found: " + document.getUri()+" /// "+ "projet found: "+projet.getUri());
            String titre = projet.getMeta("titre");
            String description = projet.getMeta("description");
            String langage = projet.getMeta("langage");
            document.withMeta("titre_projet", titre);
            document.withMeta("description_projet", description);
            document.withMeta("langage_projet", langage);
        	}
           
        }
        
        final List<IAggregationDocument> pathstwitter = GraphMatchHelpers.getPathsEnd(handler.match(document, "twitter account"));
        for (IAggregationDocument twitter : pathstwitter) {
        	String average_tweet_per_day = twitter.getMeta("average_tweet_per_day");
        	String followers_count = twitter.getMeta("followers_count");
        	String tweets_count = twitter.getMeta("tweets_count");
    		document.withMeta("average_tweet_per_day", average_tweet_per_day);
    		document.withMeta("twitter_followers", followers_count);
    		document.withMeta("tweets_count", tweets_count);
        	final List<IAggregationDocument> pathstweets = GraphMatchHelpers.getPathsEnd(handler.match(twitter, "ref_tweet"));
        	for (IAggregationDocument tweets : pathstweets) {
        		//LOGGER.info("tweets found: " + tweets.getUri()+" /// "+ "candidat found: "+document.getUri());
        		String tweet = tweets.getMeta("tweet_text");
        		document.withMeta("tweets", tweet);
        	}
        	
        	}
            
            //String logintwitter = twitter.getMeta("login_twitter");
            //document.withMeta("twitter_account", logintwitter);
           // LOGGER.info("git metas: " + document.getMeta("twitter_account"));
        
        
        final List<IAggregationDocument> pathsstack = GraphMatchHelpers.getPathsEnd(handler.match(document, "stackoverflow account"));
        for (IAggregationDocument stack : pathsstack) {
        	List<String> tags_stack = stack.getMetas("stack_tags");
        	String linkstack = stack.getMeta("stack_url");
        	document.withMeta("tags_stack", tags_stack);
        	document.withMeta("stack_link", linkstack);
        	
        	final List<IAggregationDocument> pathsques = GraphMatchHelpers.getPathsEnd(handler.match(stack, "ref_question"));
        	for (IAggregationDocument ques : pathsques) {
            String titre = ques.getMeta("titre_question");
            String url = ques.getMeta("url_question");
            document.withMeta("titre_question", titre);
            document.withMeta("url_question", url);
        	}
        	
        	final List<IAggregationDocument> pathsans = GraphMatchHelpers.getPathsEnd(handler.match(stack, "ref_answer"));
        	for (IAggregationDocument ans : pathsans) {
            String titre = ans.getMeta("url_answer");
            document.withMeta("url_answer", titre);
        	}
          
        }
        
        
        final List<IAggregationDocument> pathslink = GraphMatchHelpers.getPathsEnd(handler.match(document, "linkedin account"));
        for (IAggregationDocument link : pathslink) {
        	List<String> education = link.getMetas("education");
        	List<String> experience = link.getMetas("experience");
        	List<String> linkedin_competence = link.getMetas("linkedin_competence");
        	String linkedin_url = link.getMeta("linkedin_url");
        	document.withMeta("education", education);
        	document.withMeta("experience", experience);
        	document.withMeta("linkedin_competence", linkedin_competence);
        	document.withMeta("linkedin_url", linkedin_url);
           LOGGER.info("link found: " + link.getUri()+" /// "+ "candidat found: "+document.getUri());
            
        }
       // LOGGER.info("candidat all metas: " + document.getMetaNames());
   
	}

}
