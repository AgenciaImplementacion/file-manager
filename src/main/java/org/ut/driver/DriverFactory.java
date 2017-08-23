package org.ut.driver;

import org.ut.util.ConfigTools;
import java.util.Properties;

public class DriverFactory {

    public static final int LOCAL_FILES = 1;
    public static final int FTP = 2;
    public static final int SFTP = 3;
    public static final int GOOGLE_DRIVE = 4;

    public Driver getDriver(String urlConfig) {
        Properties p = ConfigTools.readConfigFile(urlConfig);
        if (p != null) {
            int driver = Integer.parseInt(p.getProperty("driver"));
            if (driver > 0) {
                return this.getDriver(driver, p);
            } else {
                return this.getDriver(DriverFactory.LOCAL_FILES, p);
            }
        } else {
            return null;
        }
    }

    public Driver getDriver(int driver, String urlConfig) {
        Properties p = ConfigTools.readConfigFile(urlConfig);
        if (p != null) {
            return this.getDriver(driver, p);
        } else {
            return null;
        }
    }

    public Driver getDriver(int driver, Properties config) {
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
