/*
 * run me with ./bin/java com.exalead.mot.examples.BasicServletExample 8080 com/exalead/mot/examples/complexconf.xml
 * Then you can test with http://localhost:8080/?query="hello world"&lang=en
 */

package com.exalead.mot.examples;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.linguistic.MOTPool;
import com.exalead.linguistic.v10.MOTConfig;
import com.exalead.mot.core.MOTPipe;
import com.exalead.mot.v10.AnnotatedToken;
import com.exalead.mot.v10.Annotation;

public class BasicServletExample extends HttpServlet {
    private static final long serialVersionUID = -7173158958404374632L;
    private MOTPool _pool;

    public BasicServletExample(String configPath) throws Exception {
        MOTConfig config = LinguisticFactory.readMOTConfig(configPath);
        _pool = LinguisticFactory.buildPipePool(config.getTokenizer(), config.getNormalizerConfig(),
                config.getSemanticProcessor(), true, 2, 4, null);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String query = request.getParameter("query");
        String lang = request.getParameter("lang");
        if (query == null) {
            throw new ServletException("Missing required parameter \"query\"");
        }
        if (lang == null) {
            lang = "XX";
        }
        try {
            MOTPipe pipe =  _pool.acquirePipe();
            AnnotatedToken[] tokens = pipe.process(query, Language.code(lang));
            PrintWriter writer = response.getWriter();
            response.setContentType("text/plain");
            for (int i = 0; i < tokens.length; ++i) {
                AnnotatedToken tok = tokens[i];
                writer.println("  Token[" + tok.token + "] kind[" + AnnotatedToken.nameOfKind(tok.kind) + "]");
                for (int j = 0; j < tokens[i].annotations.length; ++j) {
                    Annotation ann = tok.annotations[j];
                    writer.println("    Annotation[" + ann.displayForm + "] tag[" + ann.tag + "] nbTokens[" + ann.nbTokens + "]");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            System.err.println("Usage: [pkg].BasicServletExample port semanticConfiguration.xml");
            return ;
        }
        Server server = new Server(Integer.parseInt(argv[0]));
        Context ctx = new Context(server, "/", Context.NO_SESSIONS);
        ctx.addServlet(new ServletHolder(new BasicServletExample(argv[1])), "/*");
        server.start();
        server.join();
    }
}
