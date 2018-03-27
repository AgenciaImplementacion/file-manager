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
import java.io.IOException;
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
            for (Resource r1 : r) {
                if (FilenameUtils.getExtension(r1.getFilename()).equals("properties") && !r1.getFilename().equals("application.properties")) {
                    Driver d = DriverFactory.getDriver(r1.getInputStream());
                    this.drivers.put(d.getName(), d);
                    this.keysDrivers.add(d.getName());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.StorageClient.IOException) " + e.getMessage(), e);
        }
        System.out.print(this.drivers);
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
        return this.list(path, driver, -1);
    }

    public FolderInfo list(String path, String connection, int depth) throws DriverNotFoundException, IOException {
        if (this.drivers.containsKey(connection)) {
            Driver d = this.drivers.get(connection);
            return d.list(path, depth);
        } else {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.list.DriverNotFound) " + connection);
            throw new DriverNotFoundException("Error: connection " + connection + " not found.");
        }
    }

    public boolean isFile(String path, String connection) throws IOException, DriverNotFoundException {
        if (this.drivers.containsKey(connection)) {
            Driver d = this.drivers.get(connection);
            return d.isFile(path);
        } else {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.list.DriverNotFound) " + connection);
            throw new DriverNotFoundException("Error: connection " + connection + " not found.");
        }
    }

    public String getFullPath(String connection) throws DriverNotFoundException {
        if (this.drivers.containsKey(connection)) {
            Driver d = this.drivers.get(connection);
            return d.getFullPath();
        } else {
            LOGGER.log(Level.SEVERE, "Error: (StorageClient.list.DriverNotFound) " + connection);
            throw new DriverNotFoundException("Error: connection " + connection + " not found.");
        }
    }
}
