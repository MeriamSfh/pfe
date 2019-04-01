package com.exalead.customcode.analysis;

import com.exalead.customcode.Rot13Utils;
import com.exalead.config.bean.IsMandatory;
import com.exalead.pdoc.analysis.CustomDocumentProcessor;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.pdoc.Meta;
import com.exalead.pdoc.ProcessableDocument;
import com.exalead.pdoc.analysis.DocumentProcessingContext;

/**
 * This sample demonstrates very simple use of the configuration mechanism, and the most basic APIs of a custom
 * document processor.
 * 
 * This processor takes as configuration the name of a meta, and will rot13 it.
 *  
 * This processor can be configured with
 *   <CustomDocumentProcessor classId="com.exalead.customcode.analysis.Rot13DocumentProcessor">
 *    <KeyValue key="Meta" value="mymeta" />
 *   </CustomDocumentProcessor>
 */
@CVComponentDescription("(Sample) Rot13")
@CVComponentConfigClass(configClass=Rot13DocumentProcessor.Rot13DocumentProcessorConfig.class)
public class Rot13DocumentProcessor extends CustomDocumentProcessor {

    public static class Rot13DocumentProcessorConfig implements CVComponentConfig {
        private String meta;
        public String getMeta() {
            return meta;
        }
        @IsMandatory(true)
        public void setMeta(String meta) {
            this.meta = meta;
        }
    }

    public Rot13DocumentProcessor(Rot13DocumentProcessorConfig config) throws Exception {
        super(config);
        this.config = config;
    }

    @Override
    public void process(DocumentProcessingContext context, ProcessableDocument document) throws Exception {
        for (Meta m : document.getMetas(config.getMeta())) {
            m.setValue(Rot13Utils.rot13(m.getValue()));
        }
    }

    private Rot13DocumentProcessorConfig config;
}