package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;

import java.util.Properties;

public class DGoogleDrive implements Driver {

    private Properties config;

    public DGoogleDrive(Properties config) {
        this.setConfig(config);
    }

    @Override
    public void setConfig(Properties config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return this.config.getProperty("name");
    }

    @Override
    public boolean store(MultipartFile file, String path, boolean rewrite) {
        return false;
    }
}
