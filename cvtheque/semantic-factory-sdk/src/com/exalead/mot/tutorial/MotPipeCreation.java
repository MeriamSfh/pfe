package com.exalead.mot.tutorial;
 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exalead.linguistic.LinguisticFactory;
import com.exalead.linguistic.v10.NormalizerConfig;
import com.exalead.linguistic.v10.SemanticProcessor;
import com.exalead.linguistic.v10.StandardTokenizer;
import com.exalead.linguistic.v10.Tokenizer;
import com.exalead.mot.core.MOTPipe;
 
class MotPipeCreation{
	
    public static void main(String[] argv) throws IOException {
         try {
             List<Tokenizer> tokenizers = new ArrayList<Tokenizer>();
             tokenizers.add(new StandardTokenizer());
             MOTPipe pipe = LinguisticFactory.buildPipe(tokenizers, new NormalizerConfig(), new ArrayList<SemanticProcessor>());
             System.out.println("Loading pipe...");
             pipe.init();
             System.out.println("Pipe loaded.");
         } catch(Exception e){
             System.err.println("Could not load pipe: " + e.getMessage());
         }
    }
    

}