package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;
import org.ut.entity.FolderInfo;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public interface Driver {
    String getName();

    void setConfig(Properties config);

    boolean store(MultipartFile file, String name, String path, boolean rewrite) throws IOException;

    FolderInfo list(String path, int depth) throws IOException;

    boolean isFile(String path) throws IOException;

    String getFullPath();

    Map<String, Object> getFile(String path, Boolean thumbnail) throws IOException;
}
