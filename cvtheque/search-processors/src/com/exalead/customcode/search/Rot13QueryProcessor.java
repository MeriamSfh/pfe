package com.exalead.customcode.search;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.component.config.CVComponentConfigNone;
import com.exalead.search.query.QueryContext;
import com.exalead.search.query.QueryProcessingException;
import com.exalead.search.query.node.Node;
import com.exalead.search.query.node.NodeVisitor;
import com.exalead.search.query.node.RootNode;
import com.exalead.search.query.node.UserQueryChunk;
import com.exalead.search.query.processors.CustomQueryProcessor;
import com.exalead.search.query.processors.QueryProcessorContext;

/**
 * A query processor receives the whole context of the query.
 * It can be placed at different parts of the processing pipeline.
 */
@CVComponentDescription("(Sample) Rot13 transformer")
@CVComponentConfigClass(configClass=CVComponentConfigNone.class)
public class Rot13QueryProcessor extends CustomQueryProcessor {
    public Rot13QueryProcessor(CVComponentConfig config) throws QueryProcessingException {
        super(config);
    }

    @Override
    public void onInit(QueryProcessorContext logic) {
        System.out.println("Rot13QueryProcessor onInit");
    }

    @Override
    public void onDeinit(boolean allInstances) {
        System.out.println("Rot13QueryProcessor onDeinit");

    }

    @Override
    public void process(QueryContext context) throws QueryProcessingException {
        System.out.println("Rot13QueryProcessor process");
        context.currentNode = (RootNode)context.currentNode.accept(new Rot13Visitor());
    }

    static class Rot13Visitor extends NodeVisitor {
        @Override
        public Node visit(UserQueryChunk chunk) {
            System.out.println("Handle UQC: " + chunk.value);
            chunk.value = Rot13Utils.rot13(chunk.value);
            return chunk;
        }
    }
}
