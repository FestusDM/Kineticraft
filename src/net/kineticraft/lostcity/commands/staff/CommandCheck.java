package net.kineticraft.lostcity.commands.staff;

import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.StaffCommand;
import net.kineticraft.lostcity.mechanics.metadata.Metadata;
import net.kineticraft.lostcity.mechanics.metadata.MetadataManager;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A more automatic way to teleport to spectators.
 * Created by Kneesnap on 10/8/2017.
 */
public class CommandCheck extends StaffCommand {
    public CommandCheck() {
        super(EnumRank.JR_BUILDER, "[player]", "Spectate a player", "check", "spectate");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        boolean hasReturn = MetadataManager.hasMetadata(p, Metadata.CHECK_GM);

        if (args.length > 0) {
            if (!Utils.isVisible(sender, args[0]))
                return;

            if (!hasReturn) {
                MetadataManager.setMetadata(p, Metadata.CHECK_GM, p.getGameMode());
                MetadataManager.setMetadata(p, Metadata.CHECK_LOCATION, p.getLocation());
            }
            Player target = Bukkit.getPlayer(args[0]);
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(target);
            Bukkit.getScheduler().runTask(Core.getInstance(), () -> p.setSpectatorTarget(target));
            return;
        }

        if (!hasReturn) {
            sender.sendMessage(ChatColor.RED + "No previous state to return to.");
            return;
        }

        p.teleport((Location) MetadataManager.removeMetadata(p, Metadata.CHECK_LOCATION));
        p.setGameMode(GameMode.valueOf(MetadataManager.removeMetadata(p, Metadata.CHECK_GM)));
    }
}
