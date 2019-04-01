package com.exalead.customcode.search;

import java.util.ArrayList;
import java.util.List;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mercury.component.config.CVComponentConfigNone;
import com.exalead.search.pipeline.full.CustomStringMetaProcessor;

/**
 * A meta processor receives the content of a meta in the search full hits.
 * It only applies to one meta name.
 */
@CVComponentDescription("(Sample) Rot13 Meta Transformer")
@CVComponentConfigClass(configClass=CVComponentConfigNone.class)
public class Rot13MetaProcessor extends CustomStringMetaProcessor {
    public Rot13MetaProcessor(CVComponentConfig config, String metaName) {
        super(config, metaName);
    }

    /**
     * Entry point of the meta processor, called with the list of values, and which 
     * must return the new list of values 
     */
    @Override
    public Iterable<String> onMeta(String... value) {
        List<String> uncyphered = new ArrayList<String>();
        for (String v : value) {
            uncyphered.add(Rot13Utils.rot13(v));
        }
        return uncyphered;
    }
}
