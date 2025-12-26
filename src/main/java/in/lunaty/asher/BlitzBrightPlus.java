package in.lunaty.asher;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlitzBrightPlus extends JavaPlugin implements Listener, CommandExecutor {

    private NamespacedKey disabledKey;
    
    // -1 duration is interpreted as "infinite" by the client
    private static final PotionEffect BRIGHTNESS = new PotionEffect(
            PotionEffectType.NIGHT_VISION, 
            PotionEffect.INFINITE_DURATION, 
            0, 
            false, 
            false, 
            false
    );

    @Override
    public void onEnable() {
        this.disabledKey = new NamespacedKey(this, "bbp_disabled");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fb").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        boolean isCurrentlyDisabled = player.getPersistentDataContainer().has(disabledKey, PersistentDataType.BYTE);

        if (isCurrentlyDisabled) {
            // Enable
            player.getPersistentDataContainer().remove(disabledKey);
            player.addPotionEffect(BRIGHTNESS);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l⚡ &aNight Vision Enabled"));
        } else {
            // Disable
            player.getPersistentDataContainer().set(disabledKey, PersistentDataType.BYTE, (byte) 1);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l⚡ &cNight Vision Disabled"));
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        applyNightVision(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        // slight delay might be needed on some server cores, but usually instant works for 1.21 and above
        applyNightVision(event.getPlayer());
    }

    private void applyNightVision(Player player) {
        if (!player.getPersistentDataContainer().has(disabledKey, PersistentDataType.BYTE)) {
            player.addPotionEffect(BRIGHTNESS);
        }
    }
}