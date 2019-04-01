package com.exalead.mot.tutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.linguistic.v10.LanguageDetector;
import com.exalead.linguistic.v10.NormalizerConfig;
import com.exalead.linguistic.v10.SemanticProcessor;
import com.exalead.linguistic.v10.StandardTokenizer;
import com.exalead.linguistic.v10.Tokenizer;
import com.exalead.mot.core.MOTPipe;

import com.exalead.mot.v10.Annotation;

class LanguageDetection{

    public static void main(String [] args) throws Exception {
        List<Tokenizer> tokenizer = new ArrayList<Tokenizer>(1);
        tokenizer.add(new StandardTokenizer().withConcatAlphaNum(false).withConcatNumAlpha(false));
        List<SemanticProcessor> semantics = new ArrayList<SemanticProcessor>();
        semantics.add(new LanguageDetector().withName("language dectector"));
        MOTPipe pipe = LinguisticFactory.buildPipe(tokenizer, new NormalizerConfig(), semantics);
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            pipe.init();
            while (true) {
                System.out.print("Enter a sentence: ");
                pipe.newDocument();
                pipe.newField("text");
                pipe.process(stdin.readLine(), Language.XX);
                pipe.endDocument();
                Annotation[] annotations = pipe.getDocumentAnnotations();
                for(Annotation ann : annotations){
                    if (ann.tag.equals("language")){
                        System.out.println("The detected language code is "  +ann.displayForm);
                    }
                    
                }
            }
        } catch (IOException e) {
            pipe.release();
        }
    }
}