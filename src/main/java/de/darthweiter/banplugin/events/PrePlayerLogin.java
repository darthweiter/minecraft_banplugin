package de.darthweiter.banplugin.events;

import de.darthweiter.banplugin.thread.PrePlayerLoginEventTask;
import de.darthweiter.banplugin.thread.ThreadPooling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PrePlayerLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {

        PrePlayerLoginEventTask task = new PrePlayerLoginEventTask(e);
        FutureTask<String> futureTask = new FutureTask<>(task);
        ThreadPooling.addRunnable(futureTask);
        String message = null;
        try {
            message = futureTask.get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        if (message == null) {
            e.allow();
        } else {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
        }
    }
}
