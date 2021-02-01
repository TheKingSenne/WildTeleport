package me.tks.wildteleport.wildteleport;

import com.google.gson.Gson;
import me.tks.wildteleport.messages.Messages;
import me.tks.wildteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryList {

    private ArrayList<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public CategoryList(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Category> getCategories() {
        categories = (ArrayList<Category>) categories.stream()
                .sorted(Comparator.comparingInt(Category::getRadius))
                .collect(Collectors.toList());
        return this.categories;
    }

    public void addCategory(CommandSender sender, String name, String radius) {

        if (categories.size() == 5) {
            sender.sendMessage(ChatColor.RED + Messages.LIMIT_REACHED.getMessage());
            return;
        }

        if (fromName(name.toLowerCase()) != null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_ALREADY_EXISTS.getMessage());
            return;
        }

        Category category = Category.createCategory(sender, name, radius);

        categories.add(category);

        if (WildTeleport.gui == null)
            WildTeleport.gui = new Gui();

        WildTeleport.gui.updateGui();
        sender.sendMessage(ChatColor.GREEN + Messages.CATEGORY_CREATED.getMessage());
    }

    public void removeCategory(CommandSender sender, String name) {

        Category category = fromName(name);

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        categories.remove(category);
        WildTeleport.gui.updateGui();
        sender.sendMessage(ChatColor.GREEN + Messages.CATEGORY_REMOVED.getMessage());
    }

    public void changeGuiItem(Player sender, String name) {

        Category category = fromName(name.toLowerCase());

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        category.setGuiItem(sender);
    }

    public void changeMoneyPrice(CommandSender sender, String name, String priceString) {

        name = name.toLowerCase();

        Category category = fromName(name);

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        double price;

        try {
            price = Double.parseDouble(priceString);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        category.setMoneyPrice(price);
        sender.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    public static CategoryList read() {
        CategoryList cL = new CategoryList();

        try {
            File file = new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "categories.json");

            if (file.createNewFile())
                Bukkit.getLogger().info("[WildTeleport] A new categories.json file has been created.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream(new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "categories.json"));

            Scanner sc = new Scanner(fileIn);

            if (sc.hasNextLine()) {

                String json = sc.nextLine();

                if (!json.isEmpty()) {
                    cL = CategoryList.fromJson(json);
                }


            }

            fileIn.close();
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return cL;
    }

    public void write() {
        try {

            FileWriter fW = new FileWriter(new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "categories.json"));

            fW.write(this.toJson());

            fW.close();
        }
        catch (Exception e) {
            Bukkit.getLogger().info("Error: categories.json could not be accessed");
            e.printStackTrace();
        }
    }

    public String toJson() {

        HashMap<String, Object> properties = new HashMap<String, Object>();
        Gson gson = new Gson();

        properties.put("mapSize", categories.size());

        int i = 1;

        for (Category category : categories) {
            properties.put("category" + i, category.toJson());
            i++;
        }

        return gson.toJson(properties);
    }

    public static CategoryList fromJson(String properties) {

        Gson gson = new Gson();

        Map<String, Object> map = gson.fromJson(properties, Map.class);

        double sizeDouble = (double) map.get("mapSize");
        int size = (int) sizeDouble;

        if (size == 0) return new CategoryList();

        ArrayList<Category> index = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            index.add(Category.fromJson((String) map.get("category" + i)));
        }

        return new CategoryList(index);
    }

    public Category fromName(String name) {

        for (Category category : categories) {
            if (category.getName().equals(name))
                return category;
        }
        return null;
    }

    public void changeItemPrice(Player player, String name, String amountString) {

        name = name.toLowerCase();

        Category category = fromName(name);

        if (category == null) {
            player.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(amountString);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        ItemStack itemPrice = PlayerUtils.getNotNullInMainHand(player);

        if (itemPrice == null) return;

        itemPrice.setAmount(amount);

        category.setItemPrice(itemPrice);

        player.sendMessage(ChatColor.GREEN + Messages.SET_PRICE.getMessage());
    }

    public void changeName(CommandSender sender, String oldName, String newName) {

        Category test = fromName(newName);

        if (test != null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_ALREADY_EXISTS.getMessage());
            return;
        }

        Category category = fromName(oldName);

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        category.setName(newName.toLowerCase());
        sender.sendMessage(ChatColor.GREEN + Messages.CHANGED_CATEGORY_NAME.getMessage());
    }

    public void changeLore(CommandSender sender, String name, String loreString) {
        name = name.toLowerCase();

        Category category = fromName(name);

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        category.setLore(loreString.replaceAll("&", "§"));
        sender.sendMessage(ChatColor.GREEN + Messages.CHANGED_CATEGORY_LORE.getMessage());
    }

    public void changeRadius(CommandSender sender, String name, String radiusString) {

        name = name.toLowerCase();

        Category category = fromName(name);

        if (category == null) {
            sender.sendMessage(ChatColor.RED + Messages.CATEGORY_NOT_EXISTING.getMessage());
            return;
        }

        int radius;

        try {
            radius = Integer.parseInt(radiusString);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        category.setRadius(radius);
        sender.sendMessage(ChatColor.GREEN + Messages.UPDATED_RADIUS.getMessage());
    }

    public void removeAll() {
        categories.clear();
        WildTeleport.gui.updateGui();
    }
}
