package me.tks.wildteleport.wildteleport;

import me.tks.wildteleport.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class TeleportManager {

    public static void teleportToRandomLocation(Player player, int radius) {

        Location nextLoc = getRandomLocation(player.getLocation(), radius);

        player.teleport(nextLoc);
        WildTeleport.pC.addCooldownPlayer(player);
        player.sendMessage(ChatColor.GREEN + Messages.TELEPORTED.getMessage());
    }

    public static Location getRandomLocation(Location location, int radius) {

        int currentX = location.getBlockX();
        int currentZ = location.getBlockZ();

        boolean negativeX = false;
        boolean negativeZ = false;

        int maxX = location.getBlockX() + radius;
        int maxZ = location.getBlockZ() + radius;

        if (currentX < 0) {
            negativeX = true;
            maxX = currentX - radius;
            maxX = -maxX;
        }
        if (currentZ < 0) {
            negativeZ = true;
            maxZ = location.getBlockZ() - radius;
            maxZ = -maxZ;
        }

        Random random = new Random();

        double border = location.getWorld().getWorldBorder().getSize()/2;
        double centerX = location.getWorld().getWorldBorder().getCenter().getX();
        double centerZ = location.getWorld().getWorldBorder().getCenter().getZ();

        double borderX = Math.abs(centerX) + border;
        double borderZ = Math.abs(centerZ) + border;

        int nextX =(int) borderX + 2;

        while (borderX < Math.abs(nextX)) {
            nextX = random.nextInt(maxX - Math.abs(currentX) + 1) + Math.abs(currentX);

            if (negativeX)
                nextX = -nextX;
        }

        int nextZ = (int) borderZ + 2;

        while (borderZ < Math.abs(nextZ)) {
            nextZ = random.nextInt(maxZ - Math.abs(currentZ) + 1) + Math.abs(currentZ);

            if (negativeZ)
                nextZ = -nextZ;
        }

        int nextY = location.getWorld().getHighestBlockYAt(nextX, nextZ) + 1;

        return new Location(location.getWorld(), nextX, nextY, nextZ);
    }

}
