package com.exalead.customcode.connectors.staticdata;


import com.exalead.mercury.component.CVComponentIntrospector;
import com.exalead.mercury.component.IntrospectionQuery;
import com.exalead.mercury.component.config.CVComponentConfig;

public class SampleConnectorIntrospector implements CVComponentIntrospector {
    @Override
    public Object execute(CVComponentConfig componentConfig,
                          IntrospectionQuery query) throws Exception {
        // Ensure config type is correct
        if (componentConfig != null
                && componentConfig instanceof SampleConnectorConfig) {
            // Get config
            final SampleConnectorConfig config = (SampleConnectorConfig) componentConfig;
            // Create connector instance
            final SampleConnector connector = new SampleConnector(config);
            try {
                // Dispatch to static method
                return connector.introspectorExecute(query);
            } finally {
                connector.callBeforeDestruction();
            }
        } else if (componentConfig == null) {
            throw new Exception("null config");
        } else {
            throw new Exception("Invalid config class: "
                                + componentConfig.getClass().getName());
        }
    }
}
