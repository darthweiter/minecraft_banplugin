package de.darthweiter.banplugin.commands.ban;

import de.darthweiter.banplugin.BanPlugin;
import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.database.Database;
import de.darthweiter.banplugin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.UUID;


public class BanUUIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*
         * Command Structure:
         *
         * z.b /ban-own-uuid 00000000-0000-0000-0000-000000000000 60 Test
         *
         * args[0] = uuid
         * args[1] (optional) = expires_time, time in seconds the user is banned default is permanent
         * args[2 - n] (optional) = Ban Reason, if there is no Ban Reason, the default Ban-Reason is used.
         *                          Will be concatenated for the whole rest arguments.
         */
        int argLength = args.length;
        String target = "";
        StringBuilder reason = new StringBuilder();
        Timestamp timestamp = null;
        for (int i = 0; i < argLength; i++) {
            switch (i) {
                case 0:
                    target = args[i];
                    break;
                case 1:
                    try {
                        long banTime = Long.parseLong(args[i]);
                        timestamp = new Timestamp(System.currentTimeMillis() + 1000 * banTime);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Configuration.getErrorMsgDateFormat());
                    }
                    break;
                default:
                    reason.append(args[i]);
                    if (i != (argLength - 1)) {
                        reason.append(" ");
                    }
                    break;
            }
        }

        if (!(sender instanceof Player)) {
            // Command only usable in Game not via Console.
            BanPlugin.log(Configuration.getNotPlayerErrorMsg());
            return true;
        } else if (sender.hasPermission("de.darthweiter.banplugin.staff") && argLength > 0) {
            // The uuid is the only argument, which is needed, if no more arguments are presented,
            // the ban is permanent with the default Ban Reason.
            String stringReason = Util.buildStringReason(reason);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(target));
            Player targetPlayer = offlinePlayer.getPlayer();
            if (targetPlayer != null && offlinePlayer.isOnline()) {
                targetPlayer.kickPlayer(Configuration.getBanInfoMsg() + ": " + stringReason);
            }
            Database.updateBanInfosWithUUID(
                    target, stringReason, timestamp, true, timestamp == null, sender.getName()
            );
            String banSuccessMessage = Configuration.getBanSuccessMessage()
                    + " "
                    + Configuration.getLabelBannedPlayer()
                    + ": "
                    + offlinePlayer.getName();
            sender.sendMessage(banSuccessMessage);
            BanPlugin.log(banSuccessMessage);
        } else {
            sender.sendMessage(Configuration.getErrorMsgBan());
        }
        return true;
    }
}
