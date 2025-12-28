package in.lunaty.asher.manager;

import in.lunaty.asher.BlitzBrightPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionManager {

    private final BlitzBrightPlus plugin;
    private final NamespacedKey disabledKey;
    private final PotionEffect nightVisionEffect;

    public NightVisionManager(BlitzBrightPlus plugin) {
        this.plugin = plugin;
        this.disabledKey = new NamespacedKey(plugin, "bbp_disabled");
        
        // Infinite duration, hidden particles, no icon basically looks like magic 
        this.nightVisionEffect = new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                0,
                false,
                false,
                false
        );
    }

    /**
     * Toggles the Night Vision state for a player.
     */
    public void toggleNightVision(Player player) {
        if (isOptedOut(player)) {
            enable(player);
        } else {
            disable(player);
        }
    }

    public void enable(Player player) {
        player.getPersistentDataContainer().remove(disabledKey);
        
        refreshEffect(player);
        
        player.sendMessage(color(plugin.getConfig().getString("messages.prefix") 
                + plugin.getConfig().getString("messages.enabled")));
    }

    public void disable(Player player) {
        // Add the "disabled" tag to persistent storage
        player.getPersistentDataContainer().set(disabledKey, PersistentDataType.BYTE, (byte) 1);
        
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        
        player.sendMessage(color(plugin.getConfig().getString("messages.prefix") 
                + plugin.getConfig().getString("messages.disabled")));
    }

    /**
     * Applies Night Vision if the player hasn't opted out.
     * Uses a scheduler to prevent Join/WorldChange desync.
     */
    public void applyWithDelay(Player player) {
        long delay = plugin.getConfig().getLong("apply-delay-ticks", 20L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline() && !isOptedOut(player)) {
                refreshEffect(player);
            }
        }, delay);
    }

    /**
     * Internal helper to force-apply the effect.
     */
    private void refreshEffect(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.addPotionEffect(nightVisionEffect);
    }

    private boolean isOptedOut(Player player) {
        return player.getPersistentDataContainer().has(disabledKey, PersistentDataType.BYTE);
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg != null ? msg : "");
    }
}