package me.tks.wildteleport.wildteleport;

import me.tks.wildteleport.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Gui {

    private final Inventory guiInv;

    public Gui() {

        guiInv = Bukkit.createInventory(null, 27, ChatColor.GOLD + Messages.GUI_INVENTORY_NAME.getMessage());

        ItemStack separator = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = separator.getItemMeta();
        meta.setDisplayName(" ");
        separator.setItemMeta(meta);

        for (int i = 0; i < 9; i++) {
            guiInv.setItem(i, separator);
            guiInv.setItem(i + 18, separator);
        }

        ArrayList<Category> categories = WildTeleport.cL.getCategories();

        // If there are no categories, stop here
        if (categories == null) return;

        int size = categories.size();
        int i;

        // Determine GUI slots
        if (size == 1) {
            i = 13;
        }
        else if (size <= 3) {
            i = 12;
        }
        else {
            i = 11;
        }

        // Add category items
        for (Category category : categories) {
            guiInv.setItem(i, category.getGuiItem());
            i++;
        }
        
    }

    public Inventory getInventory() {
        return this.guiInv;
    }

    public void updateGui() {

        // Close all GUI's to prevent duping
        this.closeGuis();

        // Clear all previous category items
        for (int i = 0; i < 9; i++) {
            guiInv.setItem(i + 9, null);
        }

        ArrayList<Category> categories = WildTeleport.cL.getCategories();

        // If there are no categories, stop here
        if (categories == null) return;

        int size = categories.size();
        int i;

        // Determine GUI slots
        if (size == 1) {
            i = 13;
        }
        else if (size <= 3) {
            i = 12;
        }
        else {
            i = 11;
        }

        // Add category items
        for (Category category : categories) {
            guiInv.setItem(i, category.getGuiItem());
            i++;
        }

    }

    public void openGui(Player player) {
        player.openInventory(guiInv);
    }

    public static Gui createGui() {

        Gui gui = null;

        CategoryList cL = WildTeleport.cL;

        if (cL.getCategories().isEmpty()) return gui;

        gui = new Gui();

        return gui;
    }

    public void closeGuis() {
        // Close all GUI's to prevent duping

        ArrayList<Player> players = new ArrayList<>();

        for (HumanEntity player : guiInv.getViewers()) {
            players.add((Player) player);
        }
        for (Player player : players) {
            player.closeInventory();
        }
    }
}
