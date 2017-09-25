package org.ut.driver;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import org.ut.entity.FolderInfo;
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

    public boolean store(MultipartFile file, String path, boolean rewrite) throws IOException {
        if (!file.isEmpty()) {
            FileTools.saveFile(file, this.fullBasePath + File.separatorChar + path, rewrite);
            return true;
        }
        return false;
    }

    @Override
    public FolderInfo list(String path, int depth) throws IOException {
        FolderInfo f = new FolderInfo();
        File[] files = FileTools.getFilesFolder(FilenameUtils.normalize(this.fullBasePath + File.separatorChar + path));
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (depth > 0)
                        depth -= 1;
                    if (depth != 0)
                        f.addFolder(this.list(path + File.separatorChar + file.getName(), depth));
                    else {
                        FolderInfo tmp = new FolderInfo();
                        tmp.setName(file.getName());
                        tmp.setConnection(this.getName());
                        tmp.setPath(path + File.separatorChar + file.getName());
                        f.addFolder(tmp);
                    }
                } else {
                    f.addFile(file, path);
                }
            }
        }
        f.setPath(path);
        String name = FilenameUtils.getBaseName(path);
        f.setName(name.isEmpty() ? "/" : name);
        f.setConnection(this.getName());
        return f;
    }

    @Override
    public boolean isFile(String path) throws IOException {
        return FileTools.isFile(this.fullBasePath + File.separatorChar + path);
    }

    @Override
    public String getFullPath() {
        return this.fullBasePath;
    }
}
