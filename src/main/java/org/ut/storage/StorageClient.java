package org.ut.storage;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import org.ut.driver.DLocalFiles;
import org.ut.driver.Driver;
import org.ut.util.FileTools;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

public class StorageClient {

    private static StorageClient instance = null;

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    private Map<String, Driver> drivers;

    public static StorageClient getInstance() {
        if(instance == null) {
            instance = new StorageClient();
        }
        return instance;
    }

    public StorageClient() {
        URL configPath = this.getClass().getClassLoader().getResource("/");
        File[] cfglist = FileTools.getFilesFolder(configPath.getPath());
        if (cfglist != null) {
            for (int i = 0; i < cfglist.length; i++) {
                if (cfglist[i].isFile() && FilenameUtils.getExtension(cfglist[i].getName()) == "properties" && cfglist[i].getName() != "application.properties") {
                    System.out.println("FILE=" + cfglist[i].getName());
                    System.out.println("PATH=" + cfglist[i].getAbsolutePath());
                }
            }
        }

        System.out.println("URL=" + configPath.toString());
    }

    public boolean store(MultipartFile file, String path, String driver) {
        if (drivers.containsKey(driver)) {
            Driver d = drivers.get(driver);
            return d.store(file, path);
        } else {

        }
        return false;
    }
}
