package org.ut.storage;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;
import org.ut.driver.DLocalFiles;
import org.ut.driver.Driver;
import org.ut.driver.DriverFactory;
import org.ut.driver.DriverNotFoundException;
import org.ut.entity.FolderInfo;
import org.ut.util.ConfigTools;
import org.ut.util.FileTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageClient {

    private static StorageClient instance = null;

    private final static Logger LOGGER = Logger.getLogger(DLocalFiles.class.getName());

    private Map<String, Driver> drivers;

    private ArrayList<String> keysDrivers;

    public static StorageClient getInstance() {
        if (instance == null) {
            instance = new StorageClient();
        }
        return instance;
    }

    public StorageClient() {
        this.drivers = new HashMap<>();
        this.keysDrivers = new ArrayList<>();
        PathMatchingResourcePatternResolver a = new PathMatchingResourcePatternResolver();
        try {
            Resource[] r = a.getResources("*");
            for (int i = 0; i < r.length; i++) {
                if (FilenameUtils.getExtension(r[i].getFilename()).equals("properties") && !r[i].getFilename().equals("application.properties")) {
                    Driver d = DriverFactory.getDriver(r[i].getInputStream());
                    this.drivers.put(d.getName(), d);
                    this.keysDrivers.add(d.getName());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.StorageClient.IOException) " + e.getMessage(), e);
        }
    }

    public ArrayList<String> getDriverList() {
        return this.keysDrivers;
    }

    public boolean store(MultipartFile file, String path, String driver, boolean rewrite) throws IOException, DriverNotFoundException {
        if (this.drivers.containsKey(driver)) {
            Driver d = this.drivers.get(driver);
            return d.store(file, path, rewrite);
        } else {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.store.DriverNotFound) " + driver);
            throw new DriverNotFoundException("Error: driver " + driver + " not found.");
        }
    }

    public FolderInfo list(String path, String driver) throws DriverNotFoundException, IOException {
        if (this.drivers.containsKey(driver)) {
            Driver d = this.drivers.get(driver);
            return d.list(path);
        } else {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.list.DriverNotFound) " + driver);
            throw new DriverNotFoundException("Error: driver " + driver + " not found.");
        }
    }
}
