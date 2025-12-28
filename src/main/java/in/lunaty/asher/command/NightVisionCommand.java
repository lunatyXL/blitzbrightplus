package in.lunaty.asher.command;

import in.lunaty.asher.BlitzBrightPlus;
import in.lunaty.asher.manager.NightVisionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightVisionCommand implements CommandExecutor {

    private final BlitzBrightPlus plugin;
    private final NightVisionManager nvManager;

    public NightVisionCommand(BlitzBrightPlus plugin, NightVisionManager nvManager) {
        this.plugin = plugin;
        this.nvManager = nvManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("blitzbright.reload")) {
                sender.sendMessage(color(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(color(plugin.getConfig().getString("messages.prefix") 
                    + plugin.getConfig().getString("messages.reload")));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(color(plugin.getConfig().getString("messages.player-only")));
            return true;
        }

        Player player = (Player) sender;
        
        if (!player.hasPermission("blitzbright.use")) {
            player.sendMessage(color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        nvManager.toggleNightVision(player);
        return true;
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg != null ? msg : "");
    }
}