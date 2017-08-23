package org.ut.driver;

import org.ut.util.ConfigTools;

import java.io.InputStream;
import java.util.Properties;

public class DriverFactory {

    public static final int LOCAL_FILES = 1;
    public static final int FTP = 2;
    public static final int SFTP = 3;
    public static final int GOOGLE_DRIVE = 4;

    public static Driver getDriver(InputStream configFile) {
        Properties p = ConfigTools.readConfigFile(configFile);
        if (p != null) {
            int driver = Integer.parseInt(p.getProperty("driver"));
            if (driver > 0) {
                return DriverFactory.getDriver(driver, p);
            } else {
                return DriverFactory.getDriver(DriverFactory.LOCAL_FILES, p);
            }
        } else {
            return null;
        }
    }

    public static Driver getDriver(int driver, Properties config) {
        switch (driver) {
            case DriverFactory.LOCAL_FILES:
                return new DLocalFiles(config);
            case DriverFactory.FTP:
                return new DFtp(config);
            case DriverFactory.SFTP:
                return new DSftp(config);
            case DriverFactory.GOOGLE_DRIVE:
                return new DGoogleDrive(config);
        }
        return null;
    }
}
