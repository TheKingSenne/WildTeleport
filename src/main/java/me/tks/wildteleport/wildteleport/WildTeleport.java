package me.tks.wildteleport.wildteleport;

import me.tks.wildteleport.dependencies.VaultPlugin;
import me.tks.wildteleport.messages.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WildTeleport extends JavaPlugin {

    // TO-DO
    // - Teleport delay

    public static Gui gui;
    public static CategoryList cL;
    public static MessageFile messageFile;
    public static PluginConfiguration pC;

    @Override
    public void onEnable() {

        // Set up custom messages
        messageFile = new MessageFile();
        messageFile.createMessageFile(this);
        messageFile.checkConfig();

        // Set up configuration
        pC = PluginConfiguration.read();
        pC.scheduleCooldowns();

        // Set up Category List and according GUI
        cL = CategoryList.read();
        gui = Gui.createGui();

        // Set up Vault
        VaultPlugin.setUp();

        // Register commands
        Commands cmds = new Commands();
        this.getCommand("randomteleport").setExecutor(cmds);
        this.getCommand("wildteleport").setExecutor(cmds);

        // Register events
        Bukkit.getServer().getPluginManager().registerEvents(new ClickEvents(), this);

        // Log successful boot
        Bukkit.getLogger().info("[WildTeleport] Plugin has been enabled.");
    }

    @Override
    public void onDisable() {

        // Save files
        if (cL != null)
        cL.write();
        if (pC != null)
        pC.write();
        if (gui != null)
        gui.closeGuis();

        // Log successful shutdown
        Bukkit.getLogger().info("[WildTeleport] Plugin has been disable.");
    }


}
