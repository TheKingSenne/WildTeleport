package me.tks.wildteleport.wildteleport;

import com.google.gson.Gson;
import me.tks.wildteleport.messages.Messages;
import me.tks.wildteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PluginConfiguration {

    private final ArrayList<World> blacklistedWorlds;
    private int cooldown;
    private HashMap<String, Integer> playerCooldown;

    public PluginConfiguration() {
        blacklistedWorlds = new ArrayList<>();
        playerCooldown = new HashMap<>();
        cooldown = 0;
    }

    public PluginConfiguration(ArrayList<World> worlds, int cooldown) {
        blacklistedWorlds = worlds;
        playerCooldown = new HashMap<>();
        this.cooldown = cooldown;
    }

    public PluginConfiguration(ArrayList<World> worlds, HashMap<String, Integer> playerCooldown, int cooldown) {
        blacklistedWorlds = worlds;
        this.playerCooldown = playerCooldown;
        this.cooldown = cooldown;
    }

    /**
     * Checks if a world is blacklisted.
     * @param world world to check
     * @return Boolean true if world is blacklisted
     */
    public boolean isBlacklisted(World world) {
        return blacklistedWorlds.contains(world);
    }

    /**
     * Adds a blacklisted world with messages.
     * @param player player that requested
     * @param worldName world name given by player
     */
    public void addBlacklistedWorld(CommandSender player, String worldName) {

        World world = PlayerUtils.getWorldFromString(player, worldName);

        if (world == null) return;

        if (blacklistedWorlds.contains(world)) {
            player.sendMessage(ChatColor.RED + Messages.ALREADY_BLACKLISTED.getMessage().replaceAll("PWORLDP", world.getName()));
            return;
        }

        blacklistedWorlds.add(world);
        player.sendMessage(ChatColor.GREEN + Messages.ADDED_BLACKLIST.getMessage().replaceAll("PWORLDP", world.getName()));
    }

    /**
     * Removes a blacklisted world with message.
     * @param player player that requested
     * @param worldName world name given by player
     */
    public void removeBlacklistedWorld(CommandSender player, String worldName) {

        World world = PlayerUtils.getWorldFromString(player, worldName);

        if (world == null) return;

        if (!blacklistedWorlds.contains(world)) {
            player.sendMessage(ChatColor.RED + Messages.WORLD_NOT_BLACKLISTED.getMessage().replaceAll("PWORLDP", world.getName()));
            return;
        }

        blacklistedWorlds.remove(world);
        player.sendMessage(ChatColor.GREEN + Messages.REMOVED_BLACKLIST.getMessage().replaceAll("PWORLDP", world.getName()));
    }

    /**
     * Getter for blacklisted worlds as ArrayList of Strings.
     * @return ArrayList of world names
     */
    public ArrayList<String> getBlacklistedWorlds() {

        ArrayList<String> temp = new ArrayList<>();

        for (World world : this.blacklistedWorlds) {
            temp.add(world.getName());
        }


        return temp;
    }

    public void setCooldown(CommandSender sender, String cooldownString) {

        int cooldown =0;

        try {
            cooldown = Integer.parseInt(cooldownString);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        this.cooldown = cooldown;
        sender.sendMessage(ChatColor.GREEN + Messages.CHANGED_COOLDOWN.getMessage());
    }

    public void addCooldownPlayer(Player player) {
        if (cooldown == 0) return;
        playerCooldown.put(player.getUniqueId().toString(), cooldown);
    }

    /**
     * Lists the currently blacklisted worlds to a player.
     * @param player player that requested
     */
    public void listBlacklistedWorlds(CommandSender player) {

        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.YELLOW + "[Blacklist]"
                + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----");

        player.sendMessage(ChatColor.GOLD + "» " + ChatColor.YELLOW + String.join(ChatColor.GOLD + "\n » " + ChatColor.YELLOW, getBlacklistedWorlds()));
    }
    
    public boolean hasCooldown(Player player) {
        return cooldown != 0 && playerCooldown.containsKey(player.getUniqueId().toString()) && playerCooldown.get(player.getUniqueId().toString()) > 0;
    }

    public int getPlayerCooldown(Player player) {
        return playerCooldown.get(player.getUniqueId().toString());
    }

    public void scheduleCooldowns() {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(WildTeleport.getProvidingPlugin(WildTeleport.class),
                () -> WildTeleport.pC.updateCooldowns(), 0L, 1200 * 1);

    }

    public void updateCooldowns() {

        ArrayList<String> noCooldown = new ArrayList<>();

        for (String playerUuid : playerCooldown.keySet()) {

            int currentCooldown = playerCooldown.get(playerUuid);

            if (currentCooldown <= 0) {
                noCooldown.add(playerUuid);
                continue;
            }

            currentCooldown -= 1;
            playerCooldown.put(playerUuid, currentCooldown);
        }

        for (String str : noCooldown) {
            playerCooldown.remove(str);
        }

    }

    public static PluginConfiguration read() {

        PluginConfiguration pC = new PluginConfiguration();

        try {
            File file = new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "configuration.json");

            if (file.createNewFile())
                Bukkit.getLogger().info("[WildTeleport] A new configuration.json file has been created.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream(new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "configuration.json"));

            Scanner sc = new Scanner(fileIn);

            if (sc.hasNextLine()) {

                String json = sc.nextLine();

                if (!json.isEmpty()) {
                    pC = PluginConfiguration.fromJson(json);
                }


            }

            fileIn.close();
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return pC;
    }

    public void write() {
        try {

            FileWriter fW = new FileWriter(new File(WildTeleport.getPlugin(WildTeleport.class).getDataFolder(), "configuration.json"));

            fW.write(this.toJson());

            fW.close();
        }
        catch (Exception e) {
            Bukkit.getLogger().info("Error: configuration.json could not be accessed");
            e.printStackTrace();
        }
    }

    /**
     * Converts the current plugin configuration to json format.
     * @return a String containing the json format
     */
    public String toJson() {
        HashMap<String, Object> properties = new HashMap<>();
        Gson gson = new Gson();

        properties.put("cooldown", cooldown);

        properties.put("worldSize", blacklistedWorlds.size());

        for (int i = 0; i < blacklistedWorlds.size(); i++) {
            properties.put("world" + i, blacklistedWorlds.get(i).getName());
        }

        properties.put("mapSize", playerCooldown.size());

        int i = 1;

        for (String playerUuid : playerCooldown.keySet()) {
            properties.put("uuid" + i, playerUuid);
            properties.put("cooldown" + i, playerCooldown.get(playerUuid));
            i++;
        }

        return gson.toJson(properties);
    }

    /**
     * Convertss a json formatted string to a PluginConfiguration.
     * @param properties the json String
     * @return a new PluginConfiguration
     */
    public static PluginConfiguration fromJson(String properties) {

        Gson gson = new Gson();

        Map<String, Object> map = gson.fromJson(properties, Map.class);

        int size = 0;

        if (map.get("worldSize") != null) {
            double sizeDouble = (double) map.get("worldSize");
            size = (int) sizeDouble;
        }

        ArrayList<String> worldNames = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            worldNames.add((String) map.get("world" + i));
        }

        ArrayList<World> worlds = new ArrayList<>();

        for (String name : worldNames) {
            worlds.add(Bukkit.getWorld(name));
        }

        double cooldown = (double) map.get("cooldown");

        double sizeDouble = (double) map.get("mapSize");

        int mapSize = (int) sizeDouble;

        if (mapSize == 0) return new PluginConfiguration(worlds, (int) cooldown);

        HashMap<String, Integer> index = new HashMap<>();

        for (int i = 1; i <= mapSize; i++) {

            double cooldownDouble = (double) map.get("cooldown" + i);

            index.put((String) map.get("uuid" + i), (int) cooldownDouble);
        }

        return new PluginConfiguration(worlds, index, (int) cooldown);

    }

}
