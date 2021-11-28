package de.darthweiter.banplugin.thread;

import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.database.Database;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.Callable;

public class PrePlayerLoginEventTask implements Callable<String> {
    private final AsyncPlayerPreLoginEvent event;

    public PrePlayerLoginEventTask(AsyncPlayerPreLoginEvent event) {
        this.event = event;
    }

    /**
     * The call Function of the Task.
     * Loads the Database for AccessValidation, check if a User is banned or allowed to join
     *
     * @return The Ban Reason if the Player is banned, otherwise null.
     */
    @Override
    public String call() {
        String uuid = event.getUniqueId().toString();

        Timestamp loginTime = new Timestamp(System.currentTimeMillis());

        Map<String, String> resultMap = Database.selectUUID(uuid);
        if (resultMap != null) {
            if (Boolean.parseBoolean(resultMap.get(Database.SQL_IS_BANNED))) {
                boolean permanentBan = Boolean.parseBoolean(resultMap.get(Database.SQL_BAN_IS_PERMANENT));
                String banTime;
                Timestamp timestamp;
                boolean banNotExpired = true;
                if (permanentBan) {
                    banTime = Configuration.getLabelPermanentBan();
                } else {
                    timestamp = new Timestamp(Long.parseLong(resultMap.get(Database.SQL_BAN_EXPIRES_TIME)));
                    banTime = timestamp.toString();
                    banNotExpired = timestamp.getTime() > loginTime.getTime();
                }
                String reason = resultMap.get(Database.SQL_BAN_REASON);
                if (reason == null) {
                    reason = Configuration.getDefaultBanReason();
                }
                String message =
                        Configuration.getLabelBannedUntil()
                                + ": "
                                + banTime
                                + " "
                                + Configuration.getLabelBannedReason()
                                + ": "
                                + reason;

                if (banNotExpired) {
                    return message;
                } else {
                    ThreadPooling.addRunnable(new UpdateBanWithUUIDTask(uuid));
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
