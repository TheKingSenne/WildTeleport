package me.tks.wildteleport.wildteleport;

import me.tks.wildteleport.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickEvents implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!inv.equals(WildTeleport.gui.getInventory())) return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();

        if (clicked == null) return;
        if (clicked.getItemMeta() == null) return;
        clicked.getItemMeta().getDisplayName();
        if (clicked.getItemMeta().getDisplayName().length() < 3) return;

        Player player = (Player) e.getWhoClicked();
        String name = clicked.getItemMeta().getDisplayName().substring(2).toLowerCase();

        Category category = WildTeleport.cL.fromName(name);
        if (category == null) return;

        if (WildTeleport.pC.hasCooldown(player)) {
            player.sendMessage(ChatColor.RED + Messages.IN_COOLDOWN.getMessage().replaceAll("PCOOLDOWNP", "" + WildTeleport.pC.getPlayerCooldown(player)));
            return;
        }

        // Check if random teleport is allowed
        if (WildTeleport.pC.isBlacklisted(player.getWorld())) {
            player.sendMessage(ChatColor.RED + Messages.BLACKLISTED_WORLD.getMessage());
            return;
        }

        if (!category.hasPaid(player)) {
            player.sendMessage(ChatColor.RED + Messages.CANT_AFFORD.getMessage());
            return;
        }

        TeleportManager.teleportToRandomLocation(player, category.getRadius());

    }


}
