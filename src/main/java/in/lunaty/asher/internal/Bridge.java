package in.lunaty.asher.internal;

import in.lunaty.asher.BlitzBrightPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bridge implements CommandExecutor {

    private final BlitzBrightPlus c;
    private final Handler h;

    public Bridge(BlitzBrightPlus c, Handler h) {
        this.c = c;
        this.h = h;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] a) {
        if (a.length > 0 && a[0].equalsIgnoreCase("reload")) {
            if (!s.hasPermission("blitzbright.reload")) return true;
            c.reloadConfig();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                c.getConfig().getString("messages.prefix") + c.getConfig().getString("messages.reload")));
            return true;
        }

        if (!(s instanceof Player)) return true;
        Player p = (Player) s;

        if (!p.hasPermission("blitzbright.use")) {
            String m = c.getConfig().getString("messages.no-permission");
            if (m != null) p.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        h.exec(p);
        return true;
    }
}