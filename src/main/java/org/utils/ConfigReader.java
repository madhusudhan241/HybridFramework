package org.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    public static Properties props;

    public static void laodProperties(String env){
        props = new Properties();

        try {
            FileInputStream fis = new FileInputStream("src/test/resources/" + env + ".properties");
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the env deatils "+env,e);
        }
    }

    public static String getProperty(String key){
        if(props == null){
            laodProperties("config");  //default fallback
        }
        return props.getProperty(key);
    }
}
