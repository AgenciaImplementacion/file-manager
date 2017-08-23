package org.ut.util;

import java.util.Properties;
import java.io.*;

public class ConfigTools {
    public static Properties readConfigFile(String url){
        try {
            Properties prop = new Properties();
            InputStream is = new FileInputStream(url);
            prop.load(is);
            return prop;
        } catch(IOException e) {
            System.err.println(e.toString());
        }
        return null;
    }
}
