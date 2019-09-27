package org.ut.util;

import org.ut.driver.DLocalFiles;
import java.util.Properties;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigTools {

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    public static Properties readConfigFile(InputStream configFile) {
        try {
            Properties prop = new Properties();
            prop.load(configFile);
            return prop;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error: (ConfigTools.readConfigFile.IOException) " + e.getMessage(), e);
        }
        return null;
    }

}
