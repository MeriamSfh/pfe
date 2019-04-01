package com.exalead.customcode.connectors.filesystem1;

import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.MetaContainer;
import com.exalead.papi.helper.Part;
import com.exalead.papi.helper.PartContainer;
import com.exalead.papi.helper.PushAPI;
import com.exalead.papi.helper.SecurityMeta;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.Date;
import com.exalead.papi.framework.connectors.ConnectorConfigCheck;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;

/**
 * This is a very simple connector, which pushes document from a single filesystem base path.
 */
@CVComponentDescription("(Sample) Simple filesystem")
@CVComponentConfigClass(configClass = DemoFileSystemConnectorConfig.class, configCheckClass = ConnectorConfigCheck.class)
public class DemoFileSystemConnector extends Connector {
    protected File startingFolder;

    public DemoFileSystemConnector(DemoFileSystemConnectorConfig config) throws Exception {
        super(config);
        startingFolder = config.getStartingFolder();
    }

    @Override
    public MetaContainer getDocumentSecurityTokens(String uri) {
        // in this demo connector , all documents are public
        MetaContainer result = new MetaContainer();
        result.addMeta(SecurityMeta.PUBLIC);
        return result;
    }

    /**
     * This method is used to retrieve the raw content of a document, to 
     * perform the "raw download", HTML preview, and image preview features.
     */
    @Override
    public Document fetch(String uri) throws Exception {
        return getDocument(new File(uri));
    }
    

    /**
     * This is the main entry point of the connector. This method is called each time the 
     * connector is scanned (either when the user clicks on the scan button, or from the 
     * scheduler)
     */
    @Override
    public void scan(PushAPI papi, String mode, Object modeConfig) throws Exception {
        pushFolder(papi, startingFolder);
    }

    private Document getDocument(File file) throws Exception {
        // create MetaContainer
        MetaContainer metaList = new MetaContainer();
        metaList.addMeta("filename", file.getAbsolutePath());
        String parent = file.getParent();
        if (parent == null) {
            parent = "/";
        }
        metaList.addMeta("parent", parent);

        // add SecurityToken (public access for this sample)
        metaList.addMeta(SecurityMeta.PUBLIC);

        // create PartContainer
        PartContainer partList = new PartContainer();
        byte[] fileBuffer = new byte[(int) (file.length())];
        FileInputStream fileReader = new FileInputStream(file);
        fileReader.read(fileBuffer);
        fileReader.close();
        Part part = new Part(fileBuffer);
        part.setFileName(file.getName());
        partList.addPart(part);

        /* The stamp can be used for incremental scanning, to compare
         * the current version of the file to the one stored in the Exalead index.
         * This simple example does not use the incremental scanning features.
         */
        String stamp = DateFormat.getDateInstance().format(new Date(file.lastModified()));

        return new Document(file.getAbsolutePath(), stamp, partList, metaList);
    }

    private void pushDocument(PushAPI papi, File file) throws Exception {
        papi.addDocument(getDocument(file));
    }

    private void pushFolder(PushAPI papi, File folder) throws Exception {
        for (File child : folder.listFiles()) {
            /* We need to check as often as possible if the user has sent an abort order */
            checkAbortingOperation();
            if (child.isDirectory()) {
                pushFolder(papi, child);
            } else {
                pushDocument(papi, child);
            }
        }
    }

}
