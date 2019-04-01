package com.exalead.customcode.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.exalead.lang.Language;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.mot.v10.AnnotatedToken;
import com.exalead.mot.v10.Annotation;
import com.exalead.pdoc.analysis.JavaCustomTokenizer;
import com.exalead.config.bean.IsMandatory;

/**
 * This sample demonstrates how to write a custom tokenizer that:
 *   - splits the input text into alphabetical, numerical, punctuation or blank tokens
 *   - annotates with "Capitalized" each token that starts with an upper-case letter
 *   - annotates with "Number" each token made of digits
 *   - pushes the produced tokens to the semantic pipe
 *
 * This processor can be configured with:
 *  <CustomDocumentProcessor classId="com.exalead.customcode.analysis.CustomTokenizer">
 *   <KeyValue key="Meta" value="mymeta" />
 *  </CustomDocumentProcessor>
 */

@CVComponentConfigClass(configClass = com.exalead.customcode.analysis.CustomTokenizer.CustomTokenizerConfig.class)
@CVComponentDescription(value = "My tokenizer in Java")

public class CustomTokenizer extends JavaCustomTokenizer 
{
    public static class CustomTokenizerConfig implements CVComponentConfig 
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
    
    public CustomTokenizer(CustomTokenizerConfig config) throws Exception {
        super(config);
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
    public void processChunk(String text, int language, String context) throws Exception {
        logger.debug("Tokenizing [" + text + "] in context [" + context + "] with language [" + Language.name(language) + "]");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            AnnotatedToken token = newToken(text.substring(matcher.start(), matcher.end()));
            if (token.token.matches("^\\p{Lu}.*$")) {
                Annotation[] a = { newAnnotation("Capitalized", token.token.toLowerCase(), 1) };
                token.annotations = a;           
            } else if (token.token.matches("^[0-9]+$")){
                Annotation[] a = { newAnnotation("Number", token.token.toLowerCase(), 1) };
                token.annotations = a;
            }
            pushToken(token);
        }        
    }

    @Override
    public String[] declareAnnotations() {
        return new String[] { "Capitalized", "Number" };
    }
    
    private static Pattern pattern = Pattern.compile("\\p{L}+|\\p{N}+|\\p{Z}+|\\p{P}|[^\\p{L}\\p{N}\\p{Z}\\p{P}]+");
    private Logger logger = Logger.getLogger("mycustomtokenizer");
}
