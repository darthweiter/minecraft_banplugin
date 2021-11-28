package de.darthweiter.banplugin.util;

import de.darthweiter.banplugin.configuration.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Util {

    private Util() {
    }

    /**
     * Updates the Values of the Map with the Values of the Properties File.
     *
     * @param propertiesFile - The Properties File with the values to update
     * @param map            - the Map to update
     */
    public static void updateMap(File propertiesFile, Map<String, String> map) {
        try (InputStream in = new FileInputStream(propertiesFile)) {
            Properties prop = new Properties();
            prop.load(in);
            for (Object k : prop.keySet()) {
                String key = (String) k;
                String msg = prop.getProperty(key);
                if (msg != null) {
                    map.computeIfPresent(key, (x, val) -> val = msg); // update of the value from the map.
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Builds the BanReason of the StringBuilder.
     *
     * @param reason - A String Builder with the Ban Reason.
     * @return The Ban Reason of the String builder or Default Ban-Reason if the String Builder is empty.
     */
    public static String buildStringReason(StringBuilder reason) {
        String stringReason = reason.toString();
        if (stringReason.equals("")) {
            stringReason = Configuration.getDefaultBanReason();
        }
        return stringReason;
    }
}
