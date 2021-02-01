package me.tks.wildteleport.wildteleport;

import com.google.gson.Gson;
import me.tks.wildteleport.dependencies.VaultPlugin;
import me.tks.wildteleport.messages.Messages;
import me.tks.wildteleport.utils.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class Category {

    private String name;
    private ArrayList<String> lore;
    private int radius;
    private double moneyPrice;
    private ItemStack itemPrice;
    private ItemStack guiItem;

    public Category(String name, int radius) {
        this.name = name.toLowerCase();
        this.radius = radius;
        moneyPrice = 0;
        itemPrice = null;
        guiItem = new ItemStack(Material.GRASS_BLOCK, 1);
        lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
        lore.add(ChatColor.AQUA + "Teleport to a random location");
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");
        lore.add(ChatColor.AQUA + "Radius: " + radius);
        lore.add(ChatColor.AQUA + "Money price: None");
        lore.add(ChatColor.AQUA + "Item price: None");
        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------");

        ItemMeta meta = guiItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1));
        meta.setLore(lore);
        guiItem.setItemMeta(meta);
    }

    public Category(String name, ArrayList<String> lore, int radius, double moneyPrice, ItemStack itemPrice, ItemStack guiItem) {
        this.name = name;
        this.lore = lore;
        this.radius = radius;
        this.moneyPrice = moneyPrice;
        this.itemPrice = itemPrice;
        this.guiItem = guiItem;

        ItemMeta meta = guiItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1));
        meta.setLore(lore);
        guiItem.setItemMeta(meta);

    }

    public static Category createCategory(CommandSender player, String name, String stringRadius) {

        int radius;

        try {
            radius = Integer.parseInt(stringRadius);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return null;
        }

        return new Category(name, radius);
    }

    public String getName() {
        return name;
    }

    public int getRadius() {
        return radius;
    }

    public ItemStack getGuiItem() {
        return this.guiItem;
    }

    public void setName(String name) {
        this.name = name;
        updateItemStack();
        WildTeleport.gui.updateGui();
    }

    public void updateItemStack() {
        if (moneyPrice != 0) {
            lore.set(4, ChatColor.AQUA + "Money price: " + moneyPrice);
        }
        else {
            lore.set(4, ChatColor.AQUA + "Money price: None");
        }

        if (itemPrice != null) {

            String str;

            if (itemPrice.getType().equals(Material.AIR)) {
                str = ChatColor.AQUA + "Item price: None";
            }
            else {
                str = ChatColor.AQUA + "Item price: " + itemPrice.getAmount() + " " +
                        itemPrice.getType().name().toLowerCase().replaceAll("_", " ") + "(s)";
            }

            lore.set(5, str);
        }
        ItemMeta meta = guiItem.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1));
        guiItem.setItemMeta(meta);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        lore.set(3, ChatColor.AQUA + "Radius: " + radius);
        updateItemStack();
        WildTeleport.gui.updateGui();
    }

    public void setMoneyPrice(double moneyPrice) {
        this.moneyPrice = moneyPrice;
        updateItemStack();
        WildTeleport.gui.updateGui();
    }

    public void setItemPrice(ItemStack itemPrice) {
        this.itemPrice = itemPrice;
        updateItemStack();
        WildTeleport.gui.updateGui();
    }

    public void setGuiItem(Player player) {
        ItemStack newItem = PlayerUtils.getNotNullInMainHand(player);

        if (newItem == null) return;

        guiItem.setType(newItem.getType());
        WildTeleport.gui.updateGui();
        player.sendMessage(ChatColor.GREEN + Messages.CHANGED_CATEGORY_ITEM.getMessage());
    }

    public String toJson() {
        HashMap<String, Object> properties = new HashMap<>();

        Gson gson = new Gson();

        properties.put("name", "" + name);
        properties.put("radius", "" + radius);
        properties.put("moneyPrice", "" + moneyPrice);
        if (itemPrice != null)
            properties.put("itemPrice", "" + gson.toJson(serialize(itemPrice)));

        if (guiItem != null) {
            properties.put("guiItem", "" + gson.toJson(serialize(guiItem)));
            if (guiItem.getType().equals(Material.PLAYER_HEAD)) {
                SkullMeta meta = (SkullMeta) guiItem.getItemMeta();
                if (meta != null && meta.hasOwner()) {
                    properties.put("skullOwner", meta.getOwningPlayer().getUniqueId().toString());
                }
            }
        }

        properties.put("lore", lore);

        return gson.toJson(properties);
    }

    public static Category fromJson(String properties) {
        Gson gson = new Gson();

        HashMap<String, Object> map = gson.fromJson(properties, HashMap.class);

        String name = (String) map.get("name");
        int radius = Integer.parseInt((String) map.get("radius"));

        ItemStack guiItem;

        if (map.get("guiItem") == null)
            guiItem = new ItemStack(Material.GRASS_BLOCK, 1);
        else {
            try {
                guiItem = ItemStack.deserialize(gson.fromJson((String) map.get("guiItem"), Map.class));
            }
            catch (Exception e) {
                guiItem = deserialize(gson.fromJson((String) map.get("guiItem"), String.class));
            }
        }

        if (guiItem.getType().equals(Material.PLAYER_HEAD)) {
            if (map.get("skullOwner") != null) {

                SkullMeta meta = (SkullMeta) guiItem.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString((String) map.get("skullOwner"))));
                guiItem.setItemMeta(meta);
            }

        }

        ItemStack itemPrice = null;
        if (map.get("itemPrice") != null) {
            try {
                itemPrice = ItemStack.deserialize(gson.fromJson((String) map.get("itemPrice"), Map.class));
            }
            catch (Exception e) {
                itemPrice = deserialize(gson.fromJson((String) map.get("itemPrice"), String.class));
            }

        }

            double moneyPrice = Double.parseDouble((String) map.get("moneyPrice"));
        ArrayList<String> lore = (ArrayList<String>) map.get("lore");

        return new Category(name, lore, radius, moneyPrice, itemPrice, guiItem);
    }

    private static String getEnchants(ItemStack i){
        List<String> e = new ArrayList<>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for(Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    public static String serialize(ItemStack i){
        String[] parts = new String[6];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        if (i.getItemMeta() != null)
        parts[3] = i.getItemMeta().getDisplayName();
        if (i.getData() != null)
        parts[4] = String.valueOf(i.getData().getData());
        parts[5] = getEnchants(i);
        return StringUtils.join(parts, ";");
    }

    public static ItemStack deserialize(String p){
        String[] a = p.split(";");
        ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
        i.setDurability((short) Integer.parseInt(a[2]));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(a[3]);
        i.setItemMeta(meta);
        MaterialData data = i.getData();
        data.setData((byte) Integer.parseInt(a[4]));
        i.setData(data);
        if (a.length > 5) {
            String[] parts = a[5].split(",");
            for (String s : parts) {
                String label = s.split(":")[0];
                String amplifier = s.split(":")[1];
                Enchantment type = Enchantment.getByName(label);
                if (type == null)
                    continue;
                int f;
                try {
                    f = Integer.parseInt(amplifier);
                } catch(Exception ex) {
                    continue;
                }
                i.addEnchantment(type, f);
            }
        }
        return i;
    }

    public boolean hasPaid(Player player) {

        if (moneyPrice == 0 && (itemPrice == null || itemPrice.getAmount() == 0)) return true;

        if (moneyPrice != 0) {

            if (VaultPlugin.getEconomy().getBalance(player) < moneyPrice) return false;

        }

        if (itemPrice != null && itemPrice.getAmount() != 0) {

            Inventory inv = player.getInventory();

            if (itemPrice == null) return true;

            int amount = 0;

            for (ItemStack item : inv.getContents()) {

                if (item != null && item.getType().equals(itemPrice.getType()) && Objects.equals(item.getItemMeta(), itemPrice.getItemMeta())) {
                    amount += item.getAmount();
                }
            }

            if (amount < itemPrice.getAmount()) return false;
        }

        if (moneyPrice != 0) {
            VaultPlugin.getEconomy().withdrawPlayer(player, moneyPrice);
        }
        if (itemPrice != null && itemPrice.getAmount() != 0) {
            player.getInventory().remove(itemPrice);
        }

        return true;
    }

    public void setLore(String loreString) {
        lore.set(1, ChatColor.AQUA + loreString);
        updateItemStack();
        WildTeleport.gui.updateGui();
    }
}
