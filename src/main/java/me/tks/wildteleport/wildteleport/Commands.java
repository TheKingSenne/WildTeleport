package me.tks.wildteleport.wildteleport;

import me.tks.wildteleport.messages.Messages;
import me.tks.wildteleport.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        String commandName = cmd.getName();

        // Random teleport
        if (commandName.equalsIgnoreCase("randomTeleport")) {

            // Check if sender is player
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error: you can't do that.");
                return true;
            }

            if (WildTeleport.gui == null || WildTeleport.cL.getCategories().isEmpty()) {
                sender.sendMessage(ChatColor.RED + Messages.NO_CATEGORIES.getMessage());
                return true;
            }

            // Open GUI
            WildTeleport.gui.openGui((Player) sender);

            return true;
        }

        // -------------- //
        // Admin commands //
        // -------------- //
        else if (commandName.equalsIgnoreCase("wildTeleport")) {

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
                return true;
            }

            if (!PlayerUtils.hasPermissionWithMessage(sender, "wildteleport.admin")) return true;

            // Help menu
            else if (args[0].equalsIgnoreCase("help")) {

                if (args.length > 2) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport help (page)"));
                    return true;
                }

                if (args.length == 1) {
                    displayHelpMenu(sender, 1 + "");
                    return true;
                }

                displayHelpMenu(sender, args[1]);
                return true;
            }

            // Set cooldown
            else if (args[0].equalsIgnoreCase("cooldown")) {

                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport cooldown <cooldownInMinutes>"));
                    return true;
                }

                WildTeleport.pC.setCooldown(sender, args[1]);

                return true;
            }

            // Blacklist worlds
            else if (args[0].equalsIgnoreCase("blacklist")) {

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport blacklist <add/remove/list> (worldName)"));
                    return true;
                }

                // Add
                if (args[1].equalsIgnoreCase("add")) {

                    if (args.length != 3) {
                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport blacklist add <worldName>"));
                        return true;
                    }

                    WildTeleport.pC.addBlacklistedWorld(sender, args[2]);

                    return true;
                }


                // Remove
                else if (args[1].equalsIgnoreCase("remove")) {

                    if (args.length != 3) {
                        sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport blacklist remove <worldName>"));
                        return true;
                    }

                    WildTeleport.pC.removeBlacklistedWorld(sender, args[2]);

                    return true;

                }

                // List
                else if (args[1].equalsIgnoreCase("list")) {

                    if (WildTeleport.pC.getBlacklistedWorlds().isEmpty()) {
                        sender.sendMessage(ChatColor.RED + Messages.NO_WORLDS_BLACKLISTED.getMessage());
                        return true;
                    }

                    WildTeleport.pC.listBlacklistedWorlds(sender);

                    return true;
                }

                // No command recognized
                sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport blacklist <add/remove/list> (worldName)"));
                return true;
            }

            // Create category
            else if (args[0].equalsIgnoreCase("addCategory")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport addCategory <name> <radius>"));
                    return true;
                }

                WildTeleport.cL.addCategory(sender, args[1], args[2]);
                return true;
            }

            // Remove category
            else if (args[0].equalsIgnoreCase("removeCategory")) {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport removeCategory <name>"));
                    return true;
                }

                WildTeleport.cL.removeCategory(sender, args[1]);
                return true;
            }

            // Set category item
            else if (args[0].equalsIgnoreCase("setItem")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Error: You can't do that.");
                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport setitem <category>"));
                    return true;
                }

                WildTeleport.cL.changeGuiItem((Player) sender, args[1]);
                return true;
            }

            // Set category money price
            else if (args[0].equalsIgnoreCase("moneyPrice")) {

                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport moneyprice <category> <price>"));
                    return true;
                }

                WildTeleport.cL.changeMoneyPrice(sender, args[1], args[2]);

                return true;
            }

            // Set category item price
            else if (args[0].equalsIgnoreCase("itemPrice")) {

                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport itemPrice <category> <amount>"));
                    return true;
                }

                WildTeleport.cL.changeItemPrice((Player) sender, args[1], args[2]);

                return true;
            }

            // Set category lore
            else if (args[0].equalsIgnoreCase("setLore")) {

                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport setLore <category> <lore>"));
                    return true;
                }

                WildTeleport.cL.changeLore(sender, args[1], PlayerUtils.playerArrayInputToString(args, 1));

                return true;
            }

            // Set category name
            else if (args[0].equalsIgnoreCase("changeName")) {

                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport changeName <category> <newName>"));
                    return true;
                }

                WildTeleport.cL.changeName(sender, args[1], args[2]);
                return true;
            }

            // Set radius
            else if (args[0].equalsIgnoreCase("setRadius")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/WildTeleport setRadius <category> <radius>"));
                    return true;
                }

                WildTeleport.cL.changeRadius(sender, args[1], args[2]);

                return true;
            }

            // Remove all categories
            else if (args[0].equalsIgnoreCase("removeAll")) {

                if (args.length != 1) {
                    sender.sendMessage(ChatColor.RED + Messages.CORRECT_USAGE.getMessage().replaceAll("PUSAGEP", "/wt removeAll"));
                    return true;
                }

                WildTeleport.cL.removeAll();
                sender.sendMessage(ChatColor.GREEN + Messages.REMOVED_ALL.getMessage());
                return true;
            }

            // Command not found
            sender.sendMessage(ChatColor.RED + Messages.NEED_HELP.getMessage());
            return true;
        }
        return true;
    }

    public void displayHelpMenu(CommandSender sender, String pageString) {

        int page;

        try {
            page = Integer.parseInt(pageString);
        }
        catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + Messages.PLUGIN_NEEDS_NUMBER.getMessage());
            return;
        }

        if (page > 2 || page < 1) {
            sender.sendMessage(ChatColor.RED + Messages.PAGE_NOT_EXISTING.getMessage());
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "--------------------" + ChatColor.RESET + ChatColor.YELLOW + "[WildTeleport]" + ChatColor.RESET + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "---------------------");

        if (page == 1) {
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport itemPrice <category> <amount>" + ChatColor.GRAY + " - " + "Change the item price of a category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport moneyPrice <category> <price>" + ChatColor.GRAY + " - " + "Change the money price of a category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport addCategory <name> <radius>" + ChatColor.GRAY + " - " + "Add a new category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport removeCategory <name>" + ChatColor.GRAY + " - " + "Remove a category.");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport changeName <category> <newName>" + ChatColor.GRAY + " - " + "Change the name of a category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport setRadius <category> <radius>" + ChatColor.GRAY + " - " + "Change the radius of a category");
        }
        else {
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport setItem <category>" + ChatColor.GRAY + " - " + "Set the GUI-item of a category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport cooldown <cooldown>" + ChatColor.GRAY + " - " + "Set the general cooldown");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport blacklist <add|remove|list> (world)" + ChatColor.GRAY + " - " + "Manage the world blacklist");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport setLore <category> <lore>" + ChatColor.GRAY + " - " + "Set the lore of a category");
            sender.sendMessage(ChatColor.GOLD + " »" + ChatColor.YELLOW + " /WildTeleport removeAll" + ChatColor.GRAY + " - " + "Remove all categories");
        }
    }

}
