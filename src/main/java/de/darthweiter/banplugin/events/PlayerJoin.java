package de.darthweiter.banplugin.events;

import de.darthweiter.banplugin.thread.InsertOrUpdateTask;
import de.darthweiter.banplugin.thread.ThreadPooling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ThreadPooling.addRunnable(new InsertOrUpdateTask(e));
    }
}
