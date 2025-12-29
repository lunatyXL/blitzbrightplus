package in.lunaty.asher.internal;

import in.lunaty.asher.BlitzBrightPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Handler {

    private final BlitzBrightPlus core;
    private final NamespacedKey nk;
    private final PotionEffect ef;

    public Handler(BlitzBrightPlus core) {
        this.core = core;
        this.nk = new NamespacedKey(core, "bbp_val");
        this.ef = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false, false);
    }

    public void exec(Player p) {
        if (check(p)) {
            p.getPersistentDataContainer().remove(nk);
            update(p);
            msg(p, "enabled");
        } else {
            p.getPersistentDataContainer().set(nk, PersistentDataType.BYTE, (byte) 1);
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            msg(p, "disabled");
        }
    }

    public void sync(Player p) {
        long d = core.getConfig().getLong("apply-delay-ticks", 20L);
        Bukkit.getScheduler().runTaskLater(core, () -> {
            if (p.isOnline() && check(p)) {
                update(p);
            }
        }, d);
    }

    private void update(Player p) {
        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        p.addPotionEffect(ef);
    }

    private boolean check(Player p) {
        return p.getPersistentDataContainer().has(nk, PersistentDataType.BYTE);
    }

    private void msg(Player p, String k) {
        String pf = core.getConfig().getString("messages.prefix");
        String m = core.getConfig().getString("messages." + k);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', pf + m));
    }
}