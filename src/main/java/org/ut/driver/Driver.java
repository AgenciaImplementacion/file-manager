package org.ut.driver;

import org.springframework.web.multipart.MultipartFile;
import java.util.Properties;

public interface Driver {
    String getName();
    void setConfig(Properties config);
    boolean store(MultipartFile file, String path);
}
