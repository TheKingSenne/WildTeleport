package me.tks.wildteleport.messages;

public enum MessagePathAndDefault {
    NO_PERMISSION("noPermission", "Error: You do not have permission."), 
    PLUGIN_NEEDS_NUMBER("needsNumber", "Error: Please specify a number."), 
    CORRECT_USAGE("correctUsage", "Error: Correct usage is PUSAGEP."), 
    NEED_HELP("needHelp", "For help, please use /wt help"), 
    GUI_INVENTORY_NAME("guiName", "WildTeleport"), 
    LIMIT_REACHED("limitReached", "Error: You can't have more categories."), 
    TELEPORTED("onTeleport", "You have been teleported into the wild."), 
    PAGE_NOT_EXISTING("pageLimit", "Error: This page does not exist."), 
    HOLD_ITEM("holdItem", "Error: Please hold an item"), 
    SET_PRICE("setPrice", "You have changed the category price."), 
    REMOVED_ALL("allCategoriesRemoved", "You have removed all categories."),
    CANT_AFFORD("cantAfford", "Error: You can't afford to teleport."), 
    DONT_MOVE("dontMove", "You will be teleported in PSECONDSP seconds. Please do not move."), 
    SET_DELAY("setDelay", "You have changed the teleport delay."),
    BLACKLISTED_WORLD("blacklistedWorld", "Error: random teleport is disabled in this world."), 
    WORLD_NOT_BLACKLISTED("worldNotBlacklisted", "Error: PWORLDP is not blacklisted."), 
    ADDED_BLACKLIST("addedBlacklist", "Added PWORLDP to the blacklist."), 
    REMOVED_BLACKLIST("removedBlacklist", "Removed PWORLDP from the blacklist."), 
    NO_WORLDS_BLACKLISTED("noWorldsBlacklisted", "Error: there are no worlds blacklisted."),
    NO_COMMANDS_ALLOWED("noCommandsAllowed", "Error: you can't use any WildTeleport commands whilst teleporting."), 
    ALREADY_BLACKLISTED("alreadyBlacklisted", "Error: this world is already blacklisted."), 
    WORLD_NOT_EXISTING("worldNotExisting", "Error: this world doesn't exist."), 
    TRUE_OR_FALSE("trueOrFalse", "Error: please use true or false."), 
    IN_COOLDOWN("inCooldown", "Error: you have to wait PCOOLDOWNP more minutes."), 
    CHANGED_COOLDOWN("changedCooldown", "You have changed the cooldown."),
    CATEGORY_NOT_EXISTING("categoryNotExisting", "Error: this category doesn't exist."),
    CATEGORY_REMOVED("categoryRemoved", "You have successfully removed this category."),
    CATEGORY_CREATED("categoryCreated","You have successfully added a new category."),
    NO_CATEGORIES("noCategories", "Error: There are no teleports available."),
    CHANGED_CATEGORY_ITEM("changedCategoryItem", "You have successfully changed the category item."),
    CATEGORY_ALREADY_EXISTS("categoryAlreadyExists", "Error: This name is already in use."),
    CHANGED_CATEGORY_NAME("changedCategoryName", "You have successfully changed the category name."),
    CHANGED_CATEGORY_LORE("changedCategoryLore", "You have successfully updated the lore."),
    UPDATED_RADIUS("updatedRadius", "You have changed the radius.");


    private final String msg;
    private final String path;

    /**
     * Constructor for the message ennum.
     * @param path String containing path
     * @param msg String containing message
     */
    MessagePathAndDefault(String path, String msg) {
        this.msg = msg;
        this.path = path;
    }

    /**
     * Getter for the message path.
     * @return String containing path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for the default message.
     * @return String containing default message
     */
    public String getDefaultMessage() {
        return this.msg;
    }
}
