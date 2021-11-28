package de.darthweiter.banplugin.events;

import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.util.Map;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {

        String uuid = e.getUniqueId().toString();

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
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
                } else {
                    e.allow();
                    Database.updateBanInfosWithUUID(uuid, null, null, false, false, null);
                }
            } else {
                e.allow();
            }
        } else {
            e.allow();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        String ipAddress = player.getAddress().getAddress().toString();
        Timestamp loginTime = new Timestamp(System.currentTimeMillis());
        Database.insertNewEntryOrUpdate(uuid, name, ipAddress, loginTime);
    }
}
