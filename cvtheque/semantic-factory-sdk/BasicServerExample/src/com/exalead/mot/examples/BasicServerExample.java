package com.exalead.mot.examples;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.exalead.lang.Language;
import com.exalead.linguistic.LinguisticFactory;
import com.exalead.linguistic.MOTPool;
import com.exalead.linguistic.v10.MOTConfig;
import com.exalead.mot.core.MOTPipe;
import com.exalead.mot.v10.AnnotatedToken;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="BasicServerExample")
public class BasicServerExample {
    @XmlAttribute
    protected String inputConfigurationFile;

    public void setInputConfigurationFile(String name) {
        this.inputConfigurationFile = name;
    }

    public String getInputConfigurationFile() {
        return inputConfigurationFile;
    }

    private MOTPool _pool = null;

    private void initializePool() throws Exception {
        MOTConfig config = LinguisticFactory.readMOTConfig(getInputConfigurationFile());
        _pool = LinguisticFactory.buildPipePool(config.getTokenizer(), config.getNormalizerConfig(),
                config.getSemanticProcessor(), true, 2, 4, null);
    }

    public TokenStream analyze(Analyze params) throws Exception {
        if (_pool == null) {
            initializePool();
        }
        MOTPipe pipe = _pool.acquirePipe();
        AnnotatedToken[] res = pipe.process(params.query, Language.code(params.lang));
        TokenStream ret = new TokenStream();
        ret.tokens = new ArrayList<AnnotatedToken>();
        for (int i = 0; i < res.length; ++i) {
            ret.tokens.add(res[i]);
        }
        _pool.releasePipe(pipe);
        return ret;
    }
}
