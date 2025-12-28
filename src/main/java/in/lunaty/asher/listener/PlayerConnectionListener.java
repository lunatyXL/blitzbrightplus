package in.lunaty.asher.listener;

import in.lunaty.asher.BlitzBrightPlus;
import in.lunaty.asher.manager.NightVisionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerConnectionListener implements Listener {

    private final NightVisionManager nvManager;

    public PlayerConnectionListener(BlitzBrightPlus plugin, NightVisionManager nvManager) {
        this.nvManager = nvManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        nvManager.applyWithDelay(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event) {
        nvManager.applyWithDelay(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        nvManager.applyWithDelay(event.getPlayer());
    }
}