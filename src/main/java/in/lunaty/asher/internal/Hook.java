package in.lunaty.asher.internal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Hook implements Listener {

    private final Handler h;

    public Hook(Handler h) {
        this.h = h;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void a(PlayerJoinEvent e) {
        h.sync(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void b(PlayerRespawnEvent e) {
        h.sync(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void c(PlayerChangedWorldEvent e) {
        h.sync(e.getPlayer());
    }
}