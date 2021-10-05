package com.malevdb.Utils;

import com.malevdb.Application.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    private static FileInputStream fileInputStream;
    private static Properties PROPERTIES;
    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();
    private static final String PROPERTIES_PATH = FileResourcesUtils.RESOURCE_PATH + "properties/";
    private static final String FILE_POSTFIX = ".properties";

    public static String getPropertyKey(PropertyType property, String key) {
        if (getProperties(property) == null) {
            Logger.Log(PropertyReader.class, "Cannot load key: " + key);
            return "";
        }
        Logger.Log(PropertyReader.class, "Key loaded: " + key);
        return PROPERTIES.getProperty(key);
    }

    public static Properties getProperties(PropertyType property) {
        String propertyName = property.name().toLowerCase(Locale.ROOT);
        if(PROPERTIES_MAP.containsKey(propertyName)){
            PROPERTIES = PROPERTIES_MAP.get(propertyName);
        }
        else {
            if(!readProperty(propertyName)) {
                return null;
            }
        }
        return PROPERTIES;
    }

    private static boolean readProperty(String property) {
        try {
            fileInputStream = FileResourcesUtils.getFileAsStream(PROPERTIES_PATH + property + FILE_POSTFIX);
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
            PROPERTIES_MAP.put(property, PROPERTIES);
            Logger.Log(PropertyReader.class, "Property loaded: " + property);
            return true;
        } catch (IOException e) {
            Logger.Log(PropertyReader.class, "Cannot read property: " + property);
            e.printStackTrace();
            return false;
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
