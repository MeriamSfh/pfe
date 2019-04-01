package com.exalead.customcode.search;

import java.util.ArrayList;
import java.util.List;

import com.exalead.linguistic.v10.SemanticProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.search.query.linguistic.CustomHandler;
import com.exalead.search.query.linguistic.LinguisticExpanderResource;
import com.exalead.search.query.linguistic.LinguisticPostProcessor;
import com.exalead.search.query.linguistic.LinguisticExpanderResource.PostProcessorFactory;
import com.exalead.search.query.linguistic.ModuleConfigs.Base;
import com.exalead.search.query.node.TokenizedNode;
import com.exalead.search.query.node.TokenizedNode.Token;

/**
 * A linguistic handler processes the parts of the query after they have been tokenized.
 */
@CVComponentDescription("(Sample) Rot13 Linguistic Handler")
public class Rot13LinguisticHandler extends CustomHandler {
    public Rot13LinguisticHandler(CVComponentConfig config) {}

    /**
     * This linguistic handler does not have any native MOT processor
     */
    public List<SemanticProcessor> buildSemanticProcessor() {
        return new ArrayList<SemanticProcessor>();
    }

    /**
     * But it has one Java post-processor that will receive the results of the tokenization
     */
    public PostProcessorFactory buildPostProcessorFactory() {
        return new LinguisticExpanderResource.PostProcessorFactory() {
            public LinguisticPostProcessor build() {
                return new PostProcessor();
            }
        };
    }

    public static class PostProcessor extends  LinguisticPostProcessor {
        @Override
        public void process(TokenizedNode node, Base expansion) {
            for (int i = 0; i < node.getTokens().size(); i++) {
                Token t = node.getTokens().get(i);
                String orig = t.value;
                com.exalead.search.query.node.TokenizedNode.Form f = new com.exalead.search.query.node.TokenizedNode.Form(expansion);
                f.setSingleToken(Rot13Utils.rot13(orig),
                        /*corpusFrequency*/ t.getCorpusFrequency(),
                        /*firstToken*/ i,
                        /*nbTokensSpanned*/ 1,
                        /*formName*/ "normalized",
                        /*groupId*/ t.groupId,
                        /*seqId*/ t.seqId);
                node.forms.add(f);
            }
        }
        
        @Override
        public void cleanup(TokenizedNode node) {
        }
    }

    public void release() {
    }
}