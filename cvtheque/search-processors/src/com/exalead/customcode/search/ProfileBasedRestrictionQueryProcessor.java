package com.exalead.customcode.search;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.search.query.QueryContext;
import com.exalead.search.query.QueryProcessingException;
import com.exalead.search.query.node.Node;
import com.exalead.search.query.node.NodeVisitor;
import com.exalead.search.query.node.RootNode;
import com.exalead.search.query.node.UserQueryString;
import com.exalead.search.query.processors.CustomQueryProcessor;
import com.exalead.search.query.processors.QueryProcessorContext;

/**
 * The idea of this Query Processor is to look into the context (HTML parameter), the profile value given by 
 * our custom UI. Depending on that value, the query processor could add a refinement on the query
 * by expanding the query with a specific prefix handler ( perimeter ) and its according restriction
 * value.     
 * 
 * This custom module has been written to be placed with globalPreParse or preParse processors. (before syntactic parsing = AST creation) 
 */
@CVComponentDescription("(Sample) Profile based query rewritting")
public class ProfileBasedRestrictionQueryProcessor extends CustomQueryProcessor {
    
    public ProfileBasedRestrictionQueryProcessor(CVComponentConfig config) throws QueryProcessingException {
        super(config);
    }

    @Override
    public void onInit(QueryProcessorContext logic) {
        System.out.println("Connection to the profile DataBase onInit");
    }

    @Override
    public void onDeinit(boolean allInstances) {
        System.out.println("Close the connection from the profile DataBase onDeinit");

    }
    
    public boolean hasRestrictedAccess(String profileId){
        if (profileId.equals("admin")){
            return false;
        } 
        return true;
    }

    @Override
    public void process(QueryContext context) throws QueryProcessingException {
        String profile = context.query.parameters.getParameterValue("profile");
        if(profile != null){
            if ( hasRestrictedAccess(profile) ){
                context.currentNode = new RootNode(context.currentNode.accept(new MyQueryRewriter()));
            }
        }
        
        ;
    }

    static class MyQueryRewriter extends NodeVisitor {
        
        @Override
        public Node visit(UserQueryString queryString) {
            // add a prefix handler to restrict usage 
            queryString.value += " perimeter:restrictive";
            return queryString;
        }
    }
}
