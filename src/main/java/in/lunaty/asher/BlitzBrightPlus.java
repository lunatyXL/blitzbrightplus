package in.lunaty.asher;

import in.lunaty.asher.internal.Bridge;
import in.lunaty.asher.internal.Handler;
import in.lunaty.asher.internal.Hook;
import org.bukkit.plugin.java.JavaPlugin;

public class BlitzBrightPlus extends JavaPlugin {

    private Handler h;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.h = new Handler(this);
        
        // Register generic bridge and hook
        getCommand("fb").setExecutor(new Bridge(this, h));
        getServer().getPluginManager().registerEvents(new Hook(h), this);
    }

    @Override
    public void onDisable() {
        this.h = null;
    }
}