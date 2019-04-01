package com.exalead.mot.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.mot.core.MOTPipe;
import com.exalead.mot.v10.AnnotatedToken;
import com.exalead.mot.v10.Annotation;

public class BasicCommandLineExample {

    private MOTPipe _pipe;
    private int _lang;

    public static void main(String[] argv) {
        if (argv.length != 2) {
            System.err.println("Usage: [pkg].basicTest semanticConfiguration.xml lang");
            return;
        }
        BasicCommandLineExample t = new BasicCommandLineExample();
        if (!t.load(argv[0], argv[1])) {
            return;
        }

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("Enter a sentence to parse: ");
                t.process(stdin.readLine());
            }
        } catch (IOException e) { }
    }

    public BasicCommandLineExample() {
    }

    public boolean load(String linguisticFile, String lang) {
        com.exalead.linguistic.v10.MOTConfig config;
        try {
            config = LinguisticFactory.readMOTConfig(linguisticFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load file (" + linguisticFile  + ") : " + e.getMessage());
            return false;
        }
        try {
            _pipe = LinguisticFactory.buildPipe(config.getTokenizer(), config.getNormalizerConfig(), config.getSemanticProcessor(), 0);
            System.out.println("Loading pipe...");
            _pipe.init();
            System.out.println("Pipe loaded...");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load pipe : " + e.getMessage());
            return false;
        }
        _lang = Language.code(lang);
        return true;
    }

    public void process(String content) {
        _pipe.newDocument();
        _pipe.newField("field");
        AnnotatedToken[] tokens = _pipe.process(content, _lang);
        for (int i = 0; i < tokens.length; ++i) {
            AnnotatedToken tok = tokens[i];
            System.out.println("  Token[" + tok.token + "] kind[" + AnnotatedToken.nameOfKind(tok.kind) + "]");
            for (int j = 0; j < tokens[i].annotations.length; ++j) {
                Annotation ann = tok.annotations[j];
                System.out.println("    Annotation[" + ann.displayForm + "] tag[" + ann.tag + "] nbTokens[" + ann.nbTokens + "]");
            }
        }
        _pipe.endDocument();
        Annotation[] annotations = _pipe.getDocumentAnnotations();
        for (int j = 0; j < annotations.length; ++j) {
            Annotation ann = annotations[j];
            System.out.println("  DocAnnotation[" + ann.displayForm + "] tag[" + ann.tag + "] nbTokens[" + ann.nbTokens + "]");
        }
    }
}
