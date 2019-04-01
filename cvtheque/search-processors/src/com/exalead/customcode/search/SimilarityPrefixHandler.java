package com.exalead.customcode.search;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.search.query.QueryContext;
import com.exalead.search.query.QueryProcessingException;
import com.exalead.search.query.node.Node;
import com.exalead.search.query.node.NodeVisitor;
import com.exalead.search.query.node.PrefixNode;
import com.exalead.search.query.node.UserQueryChunk;
import com.exalead.search.query.parser.ParseException;
import com.exalead.search.query.prefix.CustomPrefixHandler;
import com.exalead.search.query.prefix.PrefixHandlerContext;
import com.exalead.search.query.util.NodeBuilder;
import com.exalead.search.query.util.NodeInspector;
import com.exalead.search.query.util.UQLPrettyPrinter;

@CVComponentDescription("(Sample) SimilarityPrefixHandler")
public class SimilarityPrefixHandler extends CustomPrefixHandler {
    public SimilarityPrefixHandler(CVComponentConfig config) {
        super(config);
    }

    /** USED FOR TEST ONLY */
    SimilarityPrefixHandler() {
        super(null);
    }
   
    @Override
    public void onInit(PrefixHandlerContext context) {
        System.out.println("SimilarityPrefixHandler onInit");
    }

    @Override
    public void onDeinit() {
        System.out.println("SimilarityPrefixHandler onDeinit");

    }

    @Override
    public void onQuery(QueryContext context) throws QueryProcessingException {
        System.out.println("SimilarityPrefixHandler onQuery");
        System.out.println(UQLPrettyPrinter.prettyPrint(context.currentNode));
    }

    @Override
    public Node handlePrefix(Phase phase, PrefixNode node, NodeVisitor parentVisitor, QueryContext queryContext)
            throws QueryProcessingException {
        /**
         * A prefix handler is called several times during evaluation of the
         * query. Here, we want to work after the syntactic parsing, but before
         * the linguistic expansions have taken place.
         */
        System.out.println("SimilarityPrefixHandler handlePrefix phase=" + phase);
       
        // INPUT   prefix_handler_name:category=v1,v2,v3
        // OUTPUT  category:(v1{s=42} OR v2{s=42} OR v3{s=42})
       
        if (phase == Phase.PRE_LINGUISTIC) {
            UserQueryChunk prefixContent = NodeInspector.concatenatedContent(node);
            
            // extract category and comma-separated values
            int equalPos = prefixContent.value.indexOf("=");
            if (equalPos <= 0) {
                throw new QueryProcessingException("Bad format: " + prefixContent.value);
            }
            String category = prefixContent.value.substring(0, equalPos);
            String[] values = prefixContent.value.substring(equalPos + 1).split(",");
            if (values.length == 0) {
                throw new QueryProcessingException("Bad format: " + prefixContent.value);
            }
            
            // build the output in UQL
            StringBuilder sb = new StringBuilder();
            for (String v : values) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(v + "{s=42}");
            }
            
            // parse it and return the associated AST node
            try {
                return NodeBuilder.buildNodeFromUQL(category + ":" + sb.toString());
            } catch (ParseException e) {
                throw new QueryProcessingException("Failed to parse " + prefixContent.value, e);
            }
        } else {
            return node;
        }
    }
}