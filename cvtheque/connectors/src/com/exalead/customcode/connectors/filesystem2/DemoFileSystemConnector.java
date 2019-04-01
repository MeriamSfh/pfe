package com.exalead.customcode.connectors.filesystem2;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.papi.framework.connectors.Connector;
import com.exalead.papi.framework.connectors.ConnectorConfigCheck;
import com.exalead.papi.helper.Document;
import com.exalead.papi.helper.EnumerationMode;
import com.exalead.papi.helper.MetaContainer;
import com.exalead.papi.helper.Part;
import com.exalead.papi.helper.PartContainer;
import com.exalead.papi.helper.PushAPI;
import com.exalead.papi.helper.SecurityMeta;
import com.exalead.papi.helper.SyncedEntry;

/**
 * This is a more advanced version of the "filesystem1" connector, which handles a basic level of
 * incrementality using stamps.
 */
@CVComponentDescription("(Sample) Filesystem with incrementality")
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
        /* Retrieve the list of documents currently in the index together with their current
         * stamp.
         * This is a simple implementation. For huge document collections, you'll often want
         * to retrieve this list folder-per-folder, using the partial enumerations method
         * of the PAPI.
         * 
         * In our connector, the stamp is the last modified timestamp of the file
         */
        Map<String, Long> stamps = new HashMap<String, Long>();
        
        for (SyncedEntry status : papi.enumerateSyncedEntries("/", EnumerationMode.RECURSIVE_DOCUMENTS)) {
            Long lastModified = Long.parseLong(status.getStamp());
            stamps.put(status.getUri(), lastModified);
        }

        /* We'll also be collecting the list of all files that we see */
        Set<String> seenInFS = new HashSet<String>();

        pushFolder(papi, startingFolder, stamps, seenInFS);
        
        /* We have now updated in the index all documents that were modified, and pushed all new ones.
         * Now, we need to remove deleted documents from the index: these are the ones that are currently
         * in the index, but not anymore in the filesystem
         */
        for (String uriInIndex : stamps.keySet()) {
            if (!seenInFS.contains(uriInIndex)) {
                /* This document has been deleted from the FS, delete it from the index */
                papi.deleteDocument(uriInIndex);
            }
        }
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

        /* As stamp, we put the current last modified date */
        String stamp = "" + file.lastModified();
        return new Document(file.getAbsolutePath(), stamp, partList, metaList);
    }

    private void pushDocument(PushAPI papi, File file, Map<String, Long> stamps, Set<String> seen) throws Exception {
        /* Record the fact that we have seen this file */
        seen.add(file.getAbsolutePath());

        long currentLastModified = file.lastModified();
        Long lastModifiedInIndex = stamps.get(file.getAbsolutePath());
        
        if (lastModifiedInIndex == null) {
            /* Document is not in index, meaning that it was added since last scan: push it */
            papi.addDocument(getDocument(file));
        } else {
            if (lastModifiedInIndex == currentLastModified) {
                /* Last modified date didn't change : document is already up-to-date, skip it */
                return;
            } else {
                papi.addDocument(getDocument(file));
            }
        }
    }

    private void pushFolder(PushAPI papi, File folder, Map<String, Long> stamps, Set<String> seen) throws Exception {
        for (File child : folder.listFiles()) {
            /* We need to check as often as possible if the user has sent an abort order */
            checkAbortingOperation();
            if (child.isDirectory()) {
                pushFolder(papi, child, stamps, seen);
            } else {
                pushDocument(papi, child, stamps, seen);
            }
        }
    }
}
