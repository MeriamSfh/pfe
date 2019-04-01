package com.exalead.mot.tutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.mot.core.MOTPipe;
import com.exalead.mot.v10.AnnotatedToken;
import com.exalead.mot.v10.Annotation;

public class Step1Tokenization {

    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("usage: java com.exalead.mot.tutorial.Step1Tokenization /path/to/mot/config.xml");
            
            System.exit(1);
        }
        Step1Tokenization step1 = new Step1Tokenization();
        if (!step1.init(argv[0])) {
            return;
        }

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("Enter a sentence to parse: ");
                step1.processText(stdin.readLine());
            }
        } catch (IOException e) {
            step1._pipe.release();
        }
    }

    private MOTPipe _pipe;

    public Step1Tokenization() {
    }

    public boolean init(String motConfigPath) {
        try {
            _pipe = LinguisticFactory.buildPipe(motConfigPath);
            System.out.println("Loading pipe...");
            _pipe.init();
            System.out.println("Pipe loaded.");
        } catch (Exception e) {
            System.err.println("Could not load pipe: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void print(AnnotatedToken[] tokens) {
        for (int i = 0 ; i < tokens.length ; ++i) {
            AnnotatedToken tok = tokens[i];
            System.out.println(" Token[" + tok.token + "] kind["
                               + AnnotatedToken.nameOfKind(tok.kind) + "] lng["
                               + Language.name(tok.lang) + "] offset[" + tok.offset + "]");
            print(tok.annotations);
        }
    }

    private void print(Annotation[] annotations) {
        for (int j = 0 ; j < annotations.length ; ++j) {
            Annotation ann = annotations[j];
            System.out.println(" Annotation[" + ann.displayForm + "] tag["
                               + ann.tag + "] nbTokens[" + ann.nbTokens + "]");
        }
    }

    public void processText(String content) {
        _pipe.newDocument();
        AnnotatedToken[] tokens = _pipe.process(content, Language.XX);
        print(tokens);
        _pipe.endDocument();
        Annotation[] annotations = _pipe.getDocumentAnnotations();
        System.out.println(" Document");
        print(annotations);
    }
}