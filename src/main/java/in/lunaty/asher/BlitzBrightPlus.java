package in.lunaty.asher;

import in.lunaty.asher.command.NightVisionCommand;
import in.lunaty.asher.listener.PlayerConnectionListener;
import in.lunaty.asher.manager.NightVisionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BlitzBrightPlus extends JavaPlugin {

    private NightVisionManager nvManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.nvManager = new NightVisionManager(this);

        getCommand("fb").setExecutor(new NightVisionCommand(this, nvManager));

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this, nvManager), this);
    }

    @Override
    public void onDisable() {
        // Cleanup if necessary (rarely needed for logic this simple, but good practice :))
        this.nvManager = null;
    }
}