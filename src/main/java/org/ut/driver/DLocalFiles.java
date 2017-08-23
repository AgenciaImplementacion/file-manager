package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;
import org.ut.util.FileTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DLocalFiles implements Driver {

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    private Properties config;
    private String fullBasePath;

    public DLocalFiles(Properties config) {
        this.setConfig(config);
        this.fullBasePath = config.getProperty("path");
    }

    @Override
    public void setConfig(Properties config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return this.config.getProperty("name");
    }

    public boolean store(MultipartFile file, String path) throws IOException {
        if (!file.isEmpty()) {
            FileTools.saveFile(file, this.fullBasePath + File.separatorChar + path);
            return true;
        }
        return false;
    }

}
