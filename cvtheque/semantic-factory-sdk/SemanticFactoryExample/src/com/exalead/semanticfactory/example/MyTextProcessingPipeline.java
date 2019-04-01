package com.exalead.semanticfactory.example;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.mot.core.MOTPipe;
import com.exalead.mot.v10.AnnotatedToken;

public class MyTextProcessingPipeline {

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("usage: java com.exalead.semanticfactory.example.MyTextProcessingPipeline /path/to/motconfig.xml");
            System.exit(1);
        }
        
        MOTPipe pipe = LinguisticFactory.buildPipe(args[0]);
        pipe.init();
        
        pipe.newDocument("doc1");
        
        pipe.newField("text");
        AnnotatedToken[] textTokens = pipe.process("a small chunk of text", Language.EN);
        for (AnnotatedToken t : textTokens) {
            System.out.println(t);
        }
        
        System.out.println("========================");
        
        pipe.newField("title");
        AnnotatedToken[] titleTokens = pipe.process("a title", Language.EN);
        for (AnnotatedToken t : textTokens) {
            System.out.println(t);
        }
        
        pipe.endDocument();

        pipe.release();
    }

}
