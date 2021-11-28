package de.darthweiter.banplugin;

import de.darthweiter.banplugin.commands.ban.BanUUIDCommand;
import de.darthweiter.banplugin.commands.unban.UnbanUUIDCommand;
import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.database.DataSourceFactory;
import de.darthweiter.banplugin.database.Database;
import de.darthweiter.banplugin.events.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BanPlugin extends JavaPlugin {

    public static String PREFIX = "§aTutorial-Test §7§o";


    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();
        registerEvents();

        Configuration.generateConfigFile();
        DataSourceFactory.generateDBFile();
        if (!Database.tableExists("spieleruebersicht")) {
            Database.createTable();
        }
        log(Configuration.getInfoMsgOnEnable());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Configuration.generateConfigFile();
        DataSourceFactory.generateDBFile();
        Database.shutdownPool();
        log(Configuration.getInfoMsgOnDisable());
    }

    public static void log(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("ban-own-uuid").setExecutor(new BanUUIDCommand());
        Bukkit.getPluginCommand("unban-own-uuid").setExecutor(new UnbanUUIDCommand());
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

}
