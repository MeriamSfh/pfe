package com.exalead.customcode.search;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.search.query.QueryContext;
import com.exalead.search.query.QueryProcessingException;
import com.exalead.search.query.node.Node;
import com.exalead.search.query.node.NodeVisitor;
import com.exalead.search.query.node.PrefixNode;
import com.exalead.search.query.node.UserQueryChunk;
import com.exalead.search.query.prefix.CustomPrefixHandler;
import com.exalead.search.query.prefix.PrefixHandlerContext;
import com.exalead.search.query.util.NodeInspector;

/**
 * A prefix handler receives all parts of a query that are under a prefix, for example
 *  title:(a OR b)
 */
@CVComponentDescription("(Sample) Rot13 transformer")
public class Rot13PrefixHandler extends CustomPrefixHandler {
    public Rot13PrefixHandler(CVComponentConfig config) {
        super(config);
    }

    @Override
    public void onInit(PrefixHandlerContext context) {
        System.out.println("Rot13PrefixHandler onInit");
    }

    @Override
    public void onDeinit() {
        System.out.println("Rot13PrefixHandler onDeinit");

    }

    @Override
    public void onQuery(QueryContext context) throws QueryProcessingException {
        System.out.println("Rot13PrefixHandler onQuery");
    }

    @Override
    public Node handlePrefix(Phase phase, PrefixNode node, NodeVisitor parentVisitor,
            QueryContext queryContext) throws QueryProcessingException {
        /**
         * A prefix handler is called several times during evaluation of the query.
         * Here, we want to work after the syntactic parsing, but before the linguistic
         * expansions have taken place.
         */
        if (phase == Phase.POST_PARSE) {
            UserQueryChunk prefixContent = NodeInspector.concatenatedContent(node);
            prefixContent.value = Rot13Utils.rot13(prefixContent.value);
            return prefixContent;
        } else {
            return node;
        }
    }
}