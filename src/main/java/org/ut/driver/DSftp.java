package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;
import org.ut.entity.FolderInfo;

import java.io.IOException;
import java.util.Properties;

public class DSftp implements Driver {

    private Properties config;

    public DSftp(Properties config) {
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

    @Override
    public FolderInfo list(String path, int depth) throws IOException {
        return null;
    }

    @Override
    public boolean isFile(String path) throws IOException {
        return false;
    }

    @Override
    public String getFullPath() {
        return "";
    }
}
