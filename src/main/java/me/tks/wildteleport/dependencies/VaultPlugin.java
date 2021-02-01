package me.tks.wildteleport.dependencies;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultPlugin {

    private static Permission permission = null;
    private static Economy economy = null;

    /**
     * Loads all vault properties.
     */
    public static void setUp() {
        // Check if vault is installed
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {

            // Log that vault couldn't be found
            Bukkit.getLogger().info(ChatColor.RED + "[WildTeleport] Error: this plugin requires Vault! Errors may occur.");
        }
        // Set up the vault economy and permissions
        else {
            VaultPlugin.setupPermissions();
            if (!VaultPlugin.setupEconomy()) {
                Bukkit.getLogger().info(ChatColor.RED + "[WildTeleport] Error: vault could not find any economy plugins.");
            }
            else {
                // Log that vault has been enabled
                Bukkit.getLogger().info("[WildTeleport] Successfully hooked into Vault!");
            }

        }
    }

    /**
     * Sets up the permissions for vault.
     * @return Boolean true if successful
     */
    public static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    /**
     * Sets up the vault economy.
     * @return Boolean true if successful
     */
    public static boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Getter for the vault permissions.
     * @return the permissions
     */
    public static Permission getPermissions() {
        return permission;
    }

    /**
     * Getter for the vault economy.
     * @return current economy
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Checks if a player can afford a certain price.
     * @param player player that requested
     * @param amount amount of money to check for
     * @return Boolean true if player can afford
     */
    public static boolean hasEnoughMoney(Player player, double amount) {
        return economy.getBalance(player) >= amount;
    }
}
