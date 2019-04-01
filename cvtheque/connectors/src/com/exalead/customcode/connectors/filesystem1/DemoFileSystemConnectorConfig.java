package com.exalead.customcode.connectors.filesystem1;

import com.exalead.papi.framework.connectors.ConnectorConfig;

import com.exalead.config.bean.IsMandatory;
import com.exalead.config.bean.PropertyLabel;

import java.io.File;

/**
 * The configuration class for the filesystem connector.
 */
@PropertyLabel("Filesystem simple demo (java)")
public class DemoFileSystemConnectorConfig extends ConnectorConfig {
    protected File startingFolder = null;

    @IsMandatory(false)
    @PropertyLabel("Starting folder")
    public void setStartingFolder(File startingFolder)
    {
        this.startingFolder = startingFolder;
    }

    public File getStartingFolder()
    {
        return startingFolder;
    }

    /**
     * Optional method listing all methods, ordered (GUI)
     **/
    public static String[] getMethods() {
        return new String[] { "StartingFolder" };
        // other methods will follow
    }
}
