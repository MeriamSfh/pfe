package com.exalead.customcode.analysis;

import java.util.HashSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mot.v10.AnnotatedToken;
import com.exalead.mot.v10.Annotation;
import com.exalead.pdoc.analysis.JavaCustomSemanticProcessor;
import com.exalead.config.bean.IsMandatory;

/**
 * This sample demonstrates how to write a custom semantic processor that:
 *   - looks for a first name that is part of its dictionary
 *   - check if the following word starts with an upper-case letter
 *   - add an annotation "people" if there isn't an annotation "NE.people" already
 *
 * This processor can be configured with:
 *  <CustomDocumentProcessor classId="com.exalead.customcode.analysis.CustomSemanticProcessor">
 *   <KeyValue key="Meta" value="mymeta" />
 *  </CustomDocumentProcessor>
 */

@CVComponentConfigClass(configClass = com.exalead.customcode.analysis.CustomSemanticProcessor.CustomSemanticProcessorConfig.class)
@CVComponentDescription(value = "My semantic processor in Java")

public class CustomSemanticProcessor extends JavaCustomSemanticProcessor {

    public static class CustomSemanticProcessorConfig implements CVComponentConfig 
    {
        private String meta;
        public String getMeta() {
            return meta;
        }
        @IsMandatory(true)
        public void setMeta(String meta) {
            this.meta = meta;
        }
    }
 
    public CustomSemanticProcessor(CustomSemanticProcessorConfig config) throws Exception {
        super(config);
        for(String s : firstNames) {
            names.add(s);
        }
    }

    @Override
    public void newDocument() {
        logger.debug("I'm about to start tokenizing a new document!");
    }

    @Override
    public void endDocument() {
        logger.debug("Done with this document!");
    }

    @Override
    public String[] declareAnnotations() {
        return new String[] { "people" };
    }

    @Override
    public void processChunk(String chunk, int language, String context) throws Exception {
        for (AnnotatedToken token = getNextToken(); token != null; token = getNextToken()) {
            if (token.kind == AnnotatedToken.TOKEN_ALPHA && names.contains(token.token) && token.getAnnotationsWithTag("NE.people").isEmpty()) {
                AnnotatedToken next = getNextToken();
                if (next != null) {
                    if (next.kind == AnnotatedToken.TOKEN_SEP_SPACE) {
                        AnnotatedToken next2 = getNextToken();
                        if (next2 != null) {
                            if (next2.kind == AnnotatedToken.TOKEN_ALPHA && next2.token.matches("\\p{Lu}.+")) {
                                Annotation annotation = newAnnotation("people", token.token + " " + next2.token, 3);
                                token.annotations = (Annotation[]) ArrayUtils.add(token.annotations, annotation);
                            }
                            pushToken(token);
                            pushToken(next);
                            pushToken(next2);
                            continue;
                        } 
                    } 
                    pushToken(token);
                    pushToken(next);
                    continue;
                }
            } 
            pushToken(token);
        }
    }

    private Logger logger = Logger.getLogger("custom-semantic-processor");
    private static String[] firstNames = { "John", "Bill", "Steve", "Robert", "George", "William", "Frank" };
    private HashSet<String> names = new HashSet<String>();
}
