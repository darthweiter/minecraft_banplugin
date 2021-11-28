package de.darthweiter.banplugin.thread;

import de.darthweiter.banplugin.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;

public class InsertOrUpdateTask implements Runnable {

    PlayerJoinEvent event;

    public InsertOrUpdateTask(PlayerJoinEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        String ipAddress = player.getAddress().getAddress().toString();
        Timestamp loginTime = new Timestamp(System.currentTimeMillis());
        Database.insertNewEntryOrUpdate(uuid, name, ipAddress, loginTime);
    }
}
