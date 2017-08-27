package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;
import org.ut.entity.FolderInfo;

import java.io.IOException;
import java.util.Properties;

public interface Driver {
    String getName();
    void setConfig(Properties config);
    boolean store(MultipartFile file, String path, boolean rewrite) throws IOException;
    FolderInfo list(String path) throws IOException;
}
