package me.tks.wildteleport.utils;

import me.tks.wildteleport.messages.Messages;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerUtils {

    /**
     * Creates an OfflinePlayer from player name with message.
     * @param sender sender that requested
     * @param name name provided by sender
     * @return a new OfflinePlayer or null if it doesn't exist
     */
    public static OfflinePlayer getOfflinePlayerFromName(Player sender, String name) {
        OfflinePlayer p;

        try {
            //noinspection deprecation
            p = Bukkit.getOfflinePlayer(name);

            if (p.hasPlayedBefore()) {
                return p;
            }

        } catch (NullPointerException ignored) {
        }
        //sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_EXISTING.getMessage());
        return null;
    }

    /**
     * Checks if a player has a permission and sends a message if not.
     * @param player player to check
     * @param permission permission to check for
     * @return Boolean true if player has permission
     */
    public static boolean hasPermissionWithMessage(CommandSender sender, String permission) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (player.hasPermission(permission)) return true;

        player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION.getMessage());
        return false;
    }

    /**
     * Converts an array of player input into a single string.
     * @param string Input by player
     * @param index Starting index in array
     * @return a String containing all strings starting from the index separated with a space
     */
    public static String playerArrayInputToString(String[] string, int index) {

        ArrayList<String> newStrings = new ArrayList<>();

        for (int i = 0; i < string.length; i++) {

            if (i > index) {
                newStrings.add(string[i]);
            }

        }

        return String.join(" ", newStrings);
    }

    /**
     * Gets the item from a player's main hand and checks if it's null with message.
     * @param player player that requested
     * @return a new ItemStack or null if it's air.
     */
    public static ItemStack getNotNullInMainHand(Player player) {

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + Messages.HOLD_ITEM.getMessage());
            return null;
        }

        player.getInventory().setItemInMainHand(item);
        item.setAmount(1);

        return item;
    }

    /**
     * Converts a string given by a player into a new world with message.
     * @param player player that requested
     * @param worldName world name provided by player
     * @return a new world or null if it doesn't exist
     */
    public static World getWorldFromString(CommandSender player, String worldName) {

        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            return world;
        }
        player.sendMessage(ChatColor.RED + Messages.WORLD_NOT_EXISTING.getMessage());
        return null;
    }


}
