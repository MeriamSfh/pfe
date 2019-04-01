package com.exalead.customcode.analysis;

import org.apache.log4j.Logger;

import com.exalead.config.bean.IsMandatory;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfig;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.pdoc.Meta;
import com.exalead.pdoc.ProcessableDocument;
import com.exalead.pdoc.analysis.CustomDocumentProcessor;
import com.exalead.pdoc.analysis.DocumentProcessingContext;

/**
 * This sample demonstrates a more advanced use of the configuration mechanism, where we take
 * advantage of the hierarchical features to build a complex configuration.
 * 
 * This processor takes a list of metas, and for each meta, a list of (old value) -> (new value) replacement patterns.
 * It only works if a given "ConditionMeta" is present.
 * 
 * This processor can be configured with
 *   <CustomDocumentProcessor classId="com.exalead.customcode.analysis.MultiReplacementsDocumentProcessor">
 *    <KeyValue key="Metas">
 *      <KeyValue key="0">
 *        <KeyValue key="Name" value="meta1" />
 *        <KeyValue key="Replacements">
 *          <KeyValue key="0">
 *            <KeyValue key="OldValue" value="oldvalue0" />
 *            <KeyValue key="NewValue" value="newvalue0" />
 *          </KeyValue>
 *          <KeyValue key="1">
 *            <KeyValue key="OldValue" value="oldvalue1" />
 *            <KeyValue key="NewValue" value="newvalue1" />
 *          </KeyValue>
 *        </KeyValue>
 *      </KeyValue>
 *    </KeyValue>
 *    <KeyValue key="ConditionMeta" value="mymeta" />
 *   </CustomDocumentProcessor>
 */
@CVComponentDescription("(Sample) Multi replacements")
@CVComponentConfigClass(configClass=MultiReplacementsDocumentProcessor.MultiReplacementsProcessorConfig.class)
public class MultiReplacementsDocumentProcessor extends CustomDocumentProcessor {

    public static class MetaValueReplacement {
        private String oldValue;
        private String newValue;

        public String getOldValue() {
            return oldValue;
        }
        @IsMandatory(true)
        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }
        public String getNewValue() {
            return newValue;
        }
        @IsMandatory(true)
        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }
    }

    public static class MetaReplacement {
        private String name;
        private MetaValueReplacement[] replacements;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MetaValueReplacement[] getReplacements() {
            return replacements;
        }

        public void setReplacements(MetaValueReplacement[] replacements) {
            this.replacements = replacements;
        }
    }

    public static class MultiReplacementsProcessorConfig implements CVComponentConfig {
        private MetaReplacement[] metas;
        private String conditionMeta;
        public MetaReplacement[] getMetas() {
            return metas;
        }
        public void setMetas(MetaReplacement[] metas) {
            this.metas = metas;
        }
        public String getConditionMeta() {
            return conditionMeta;
        }
        public void setConditionMeta(String conditionMeta) {
            this.conditionMeta = conditionMeta;
        }
    }
    

    public MultiReplacementsDocumentProcessor(MultiReplacementsProcessorConfig config) throws Exception {
        super(config);
        this.config = config;
    }

    @Override
    public void process(DocumentProcessingContext context, ProcessableDocument document) throws Exception {
        if (config.getConditionMeta() != null) {
            String conditionValue = document.getFirstMetaValue(config.getConditionMeta());
            if (conditionValue == null) {
                /* No need to display any info about the document being processed, it will
                 * automatically be appended to the log line
                 */
                logger.info("Condition meta not found, skipping doc");
                return;
            }
        }

        for (MetaReplacement mr : config.getMetas()) {
            for (Meta m : document.getMetas(mr.getName())) {
                String value = m.getValue();
                if (logger.isDebugEnabled()) {
                    logger.debug("For meta " + mr.getName() + " found value: " + value);
                }
                /* Note that the implementation is quite naive. A real implementation
                 * would build a map of replacements in its "onInit" method.
                 * 
                 * There is one instance of the processor class per analysis thread, so it is
                 * safe to store data in the instance.
                 */
                for (MetaValueReplacement mvr : mr.getReplacements()) {
                    if (mvr.getOldValue().equals(value)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("  Replace to "  + mvr.getNewValue());
                        }
                        m.setValue(mvr.getNewValue());
                        break;
                    }
                }
            }
        }
    }

    private Logger logger = Logger.getLogger(MultiReplacementsDocumentProcessor.class);
    private MultiReplacementsProcessorConfig config;
}