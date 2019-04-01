package com.exalead.customcode.connectors.staticdata;


import com.exalead.customcode.connectors.staticdata.SampleDocument.SampleMeta;
import com.exalead.customcode.connectors.staticdata.SampleDocument.SamplePart;
import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.IntrospectableComponent;
import com.exalead.mercury.component.IntrospectionQuery;
import com.exalead.mercury.component.SupportedQuery;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.framework.connectors.ConnectorConfigCheck;
import com.exalead.papi.framework.connectors.introspection.CheckConnectivity;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.Meta;
import com.exalead.papi.helper.Part;
import com.exalead.papi.helper.PushAPI;

/**
 * Sample Connector pushing a given list of documents.
 * The documents are described in the connector's configuration (CloudView Administration UI).
 */
@CVComponentConfigClass(configClass = SampleConnectorConfig.class, configCheckClass = ConnectorConfigCheck.class)
@CVComponentDescription("(Sample) Static data")
@IntrospectableComponent(introspectorClass = SampleConnectorIntrospector.class, supportedQueries = { @SupportedQuery(queryClass = CheckConnectivity.class) })
public class SampleConnector extends Connector {
    org.apache.log4j.Logger logger;

    public SampleConnector(SampleConnectorConfig config) throws Exception {
        super(config);
        logger = this.getLogger();
    }

    /*
     * (non-Javadoc)
     * Run the workflow given in the configuration on the Data Integration Service
     *
     * @see com.exalead.papi.framework.connectors.Connector#scan(com.exalead.papi.helper.PushAPI, java.lang.String, java.lang.Object)
     */
    @Override
    public void scan(PushAPI papi, String scanMode, Object scanModeConfig) throws Exception {
        final SampleConnectorConfig config = (SampleConnectorConfig) this.config;
        for (SampleDocument docConf : config.getDocs()) {
            Document doc = new Document(docConf.getUri());
            doc.setStamp(docConf.getStamp());
            for (SampleMeta meta : docConf.getMetas()) {
                doc.addMeta(new Meta(meta.getName(), meta.getValue()));
            }
            for (SamplePart part : docConf.getParts()) {
                doc.addPart(new Part(part.getValue().getBytes()));
            }
            papi.addDocument(doc);
        }
        papi.sync();
    }

    /*
     * (non-Javadoc)
     * Ping the Data Integration Service and check that the asked workflow exists
     *
     * @see com.exalead.papi.framework.connectors.Connector#checkDataSourceAvailability()
     */
    @Override
    protected void checkDataSourceAvailability() throws Exception {
        // throw an exception if the source is not available
    }

    public Object introspectorExecute(IntrospectionQuery query)
            throws Exception {
        logger.debug("introspection: called with " + query.getClass().getName());
        if (query instanceof CheckConnectivity) {
            final CheckConnectivity.Answer ret = new CheckConnectivity.Answer();
            ret.setOk(true);
            // CHECK CONNECTIVITY...
            return ret;
        } else {
            throw new Exception("Unknown query type: "
                    + query.getClass().getName());
        }
    }
}
