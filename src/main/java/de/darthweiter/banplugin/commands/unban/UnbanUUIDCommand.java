package de.darthweiter.banplugin.commands.unban;

import de.darthweiter.banplugin.BanPlugin;
import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnbanUUIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String target = args[0]; // uuid
        if (!(sender instanceof Player)) {
            BanPlugin.log(Configuration.getNotPlayerErrorMsg());
            return true;
        } else if (sender.hasPermission("de.darthweiter.banplugin.staff")) {
            try {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(target));
                Database.updateBanInfosWithUUID(target, null, null, false, false, null);
                String successMessage = Configuration.getUnbanSuccessMessage()
                        + " "
                        + Configuration.getLabelUnbannedPlayer()
                        + ": "
                        + targetPlayer.getName();
                sender.sendMessage(successMessage);
                BanPlugin.log(successMessage);
            } catch (Exception e) {
                sender.sendMessage(Configuration.getErrorMsgUnban());
            }
        } else {
            sender.sendMessage(Configuration.getErrorMsgUnban());
        }
        return false;
    }
}
