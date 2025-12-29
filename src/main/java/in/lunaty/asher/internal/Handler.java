package in.lunaty.asher.internal;

import in.lunaty.asher.BlitzBrightPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Handler {

    private final BlitzBrightPlus core;
    private final NamespacedKey nk;
    private final PotionEffect ef;

    // RAM Cache (Optimization)
    private final Map<UUID, Boolean> c = new ConcurrentHashMap<>();

    public Handler(BlitzBrightPlus core) {
        this.core = core;
        this.nk = new NamespacedKey(core, "bbp_val");
        this.ef = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false, false);
    }

    // Load data (Called on Join)
    public void init(Player p) {
        boolean v = p.getPersistentDataContainer().has(nk, PersistentDataType.BYTE);
        c.put(p.getUniqueId(), v);
    }

    // Clear data (Called on Quit)
    public void drop(Player p) {
        c.remove(p.getUniqueId());
    }

    // Cleanup (Called on Disable)
    public void shutdown() {
        c.clear();
    }

    // Toggle Logic
    public void exec(Player p) {
        boolean curr = c.getOrDefault(p.getUniqueId(), false);

        if (curr) {
            // Enable
            p.getPersistentDataContainer().remove(nk);
            c.put(p.getUniqueId(), false);
            apply(p, true);
            msg(p, "enabled");
        } else {
            // Disable
            p.getPersistentDataContainer().set(nk, PersistentDataType.BYTE, (byte) 1);
            c.put(p.getUniqueId(), true);
            apply(p, false);
            msg(p, "disabled");
        }
    }

    // Sync Logic (Delayed Apply)
    public void sync(Player p) {
        long d = core.getConfig().getLong("apply-delay-ticks", 20L);
        Bukkit.getScheduler().runTaskLater(core, () -> {
            if (!p.isOnline()) return;
            // Check RAM cache
            if (!c.getOrDefault(p.getUniqueId(), false)) {
                apply(p, true);
            }
        }, d);
    }

    // Internal Apply
    private void apply(Player p, boolean on) {
        if (on) {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.addPotionEffect(ef);
            p.setPlayerTime(6000L, false); // Lock time to Noon
        } else {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.resetPlayerTime(); // Reset to Server Time
        }
    }

    private void msg(Player p, String k) {
        String pf = core.getConfig().getString("messages.prefix");
        String m = core.getConfig().getString("messages." + k);
        if (m != null) p.sendMessage(ChatColor.translateAlternateColorCodes('&', pf + m));
    }
}