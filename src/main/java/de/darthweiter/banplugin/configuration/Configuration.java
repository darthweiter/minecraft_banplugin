package de.darthweiter.banplugin.configuration;

import de.darthweiter.banplugin.BanPlugin;
import de.darthweiter.banplugin.util.FileIO;
import de.darthweiter.banplugin.util.Util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configuration {

    private static final String DEFAULT_BAN_REASON = "default.ban.reason";

    private static final String ERROR_MSG_BAN = "error.msg.ban";
    private static final String ERROR_MSG_DATE_FORMAT = "error.msg.date.format";
    private static final String ERROR_MSG_LOAD_PROPERTIES = "error.msg.load.properties";
    private static final String ERROR_MSG_NO_PLAYER = "error.msg.no.player";
    private static final String ERROR_MSG_UNBAN = "error.msg.unban";

    private static final String INFO_MSG_BAN = "info.msg.ban";
    private static final String INFO_MSG_ON_DISABLE = "info.msg.on.disable";
    private static final String INFO_MSG_ON_ENABLE = "info.msg.on.enable";

    private static final String SUCCESS_MSG_BAN = "success.msg.ban";
    private static final String SUCCESS_MSG_UNBAN = "success.msg.unban";

    private static final String LABEL_BANNED_PLAYER = "label.banned.player";
    private static final String LABEL_BANNED_REASON = "label.banned.reason";
    private static final String LABEL_BANNED_UNTIL = "label.banned.until";
    private static final String LABEL_PERMANENT_BAN = "label.permanent.ban";
    private static final String LABEL_UNBANNED_PLAYER = "label.unbanned.player";

    private static final Map<String, String> textMap = new LinkedHashMap<>();

    private static final String FILE_NAME = "text.properties";

    private Configuration() {
    }

    public static String getInfoMsgOnEnable() {
        return textMap.get(INFO_MSG_ON_ENABLE);
    }

    public static String getInfoMsgOnDisable() {
        return textMap.get(INFO_MSG_ON_DISABLE);
    }

    public static String getBanInfoMsg() {
        return textMap.get(INFO_MSG_BAN);
    }

    public static String getBanSuccessMessage() {
        return textMap.get(SUCCESS_MSG_BAN);
    }

    public static String getUnbanSuccessMessage() {
        return textMap.get(SUCCESS_MSG_UNBAN);
    }

    public static String getDefaultBanReason() {
        return textMap.get(DEFAULT_BAN_REASON);
    }

    public static String getLabelBannedPlayer() {
        return textMap.get(LABEL_BANNED_PLAYER);
    }

    public static String getLabelUnbannedPlayer() {
        return textMap.get(LABEL_UNBANNED_PLAYER);
    }

    public static String getLabelBannedUntil() {
        return textMap.get(LABEL_BANNED_UNTIL);
    }

    public static String getLabelBannedReason() {
        return textMap.get(LABEL_BANNED_REASON);
    }

    public static String getErrorMsgDateFormat() {
        return textMap.get(LABEL_UNBANNED_PLAYER);
    }

    public static String getLabelPermanentBan() {
        return textMap.get(LABEL_PERMANENT_BAN);
    }

    public static String getErrorMsgBan() {
        return textMap.get(ERROR_MSG_BAN);
    }

    public static String getErrorMsgUnban() {
        return textMap.get(ERROR_MSG_UNBAN);
    }

    public static String getErrorMsgLoadProperties() {
        return textMap.get(ERROR_MSG_LOAD_PROPERTIES);
    }

    public static String getNotPlayerErrorMsg() {
        return textMap.get(ERROR_MSG_NO_PLAYER);
    }


    /**
     * Generates a text.properties file under the directory config, with the actual Values of the textMap.
     */
    public static void generateConfigFile() {
        initTextMap();

        File directory = FileIO.loadDirectory("config");

        if (directory != null) {
            File textPropertiesFile = new File(directory.getAbsolutePath() + File.separator + FILE_NAME);
            if (textPropertiesFile.exists()) {
                Util.updateMap(textPropertiesFile, textMap);
            }
            FileIO.createFile(directory, FILE_NAME, textMap);
        } else {
            BanPlugin.log(getErrorMsgLoadProperties() + " :" + FILE_NAME);
        }
    }

    /**
     * Default Values for the Text, can be changed on the text.properties file.
     */
    private static void initTextMap() {

        textMap.put(DEFAULT_BAN_REASON, "No Ban Reason");

        textMap.put(ERROR_MSG_BAN, "An Error Occurs on Command Ban.");
        textMap.put(ERROR_MSG_DATE_FORMAT, "Date was not recognized, set permanent to true");
        textMap.put(ERROR_MSG_LOAD_PROPERTIES, "The Properties File can't loaded");
        textMap.put(ERROR_MSG_NO_PLAYER, "Sender is not a Player");
        textMap.put(ERROR_MSG_UNBAN, "An Error Occurs on Command Unban.");

        textMap.put(INFO_MSG_BAN, "Banned by Staff");
        textMap.put(INFO_MSG_ON_DISABLE, "Plugin Ban successfully disabled.");
        textMap.put(INFO_MSG_ON_ENABLE, "Plugin Ban successfully enabled.");

        textMap.put(LABEL_BANNED_PLAYER, "Banned Player");
        textMap.put(LABEL_BANNED_REASON, "Reason");
        textMap.put(LABEL_BANNED_UNTIL, "banned until");
        textMap.put(LABEL_PERMANENT_BAN, "Permanent Ban");
        textMap.put(LABEL_UNBANNED_PLAYER, "Unbanned Player");

        textMap.put(SUCCESS_MSG_BAN, "Command Ban was successfully executed.");
        textMap.put(SUCCESS_MSG_UNBAN, "Command Unban was successfully executed.");
    }
}
