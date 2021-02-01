package me.tks.wildteleport.messages;

import me.tks.wildteleport.wildteleport.WildTeleport;

public enum Messages {
    NO_PERMISSION(WildTeleport.messageFile.getMessageFile().getString("noPermission")),
    PLUGIN_NEEDS_NUMBER(WildTeleport.messageFile.getMessageFile().getString("needsNumber")),
    CORRECT_USAGE(WildTeleport.messageFile.getMessageFile().getString("correctUsage")),
    //PLAYER_NOT_EXISTING(WildTeleport.messageFile.getMessageFile().getString("noPlayer")),
    NEED_HELP(WildTeleport.messageFile.getMessageFile().getString("needHelp")),
    GUI_INVENTORY_NAME(WildTeleport.messageFile.getMessageFile().getString("guiName")),
    LIMIT_REACHED(WildTeleport.messageFile.getMessageFile().getString("limitReached")),
    TELEPORTED(WildTeleport.messageFile.getMessageFile().getString("onTeleport")),
    PAGE_NOT_EXISTING(WildTeleport.messageFile.getMessageFile().getString("pageLimit")),
    HOLD_ITEM(WildTeleport.messageFile.getMessageFile().getString("holdItem")),
    SET_PRICE(WildTeleport.messageFile.getMessageFile().getString("setPrice")),
    REMOVED_ALL(WildTeleport.messageFile.getMessageFile().getString("allCategoriesRemoved")),
    CANT_AFFORD(WildTeleport.messageFile.getMessageFile().getString("cantAfford")),
    DONT_MOVE(WildTeleport.messageFile.getMessageFile().getString("dontMove")),
    SET_DELAY(WildTeleport.messageFile.getMessageFile().getString("setDelay")),
    BLACKLISTED_WORLD(WildTeleport.messageFile.getMessageFile().getString("blacklistedWorld")),
    WORLD_NOT_BLACKLISTED(WildTeleport.messageFile.getMessageFile().getString("worldNotBlacklisted")),
    ADDED_BLACKLIST(WildTeleport.messageFile.getMessageFile().getString("addedBlacklist")),
    REMOVED_BLACKLIST(WildTeleport.messageFile.getMessageFile().getString("removedBlacklist")),
    NO_WORLDS_BLACKLISTED(WildTeleport.messageFile.getMessageFile().getString("noWorldsBlacklisted")),
    NO_COMMANDS_ALLOWED(WildTeleport.messageFile.getMessageFile().getString("noCommandsAllowed")),
    ALREADY_BLACKLISTED(WildTeleport.messageFile.getMessageFile().getString("alreadyBlacklisted")),
    WORLD_NOT_EXISTING(WildTeleport.messageFile.getMessageFile().getString("worldNotExisting")),
    TRUE_OR_FALSE(WildTeleport.messageFile.getMessageFile().getString("trueOrFalse")),
    IN_COOLDOWN(WildTeleport.messageFile.getMessageFile().getString("inCooldown")),
    CHANGED_COOLDOWN(WildTeleport.messageFile.getMessageFile().getString("changedCooldown")),
    CATEGORY_NOT_EXISTING(WildTeleport.messageFile.getMessageFile().getString("categoryNotExisting")),
    CATEGORY_REMOVED(WildTeleport.messageFile.getMessageFile().getString("categoryRemoved")),
    CATEGORY_CREATED(WildTeleport.messageFile.getMessageFile().getString("categoryCreated")),
    NO_CATEGORIES(WildTeleport.messageFile.getMessageFile().getString("noCategories")),
    CHANGED_CATEGORY_ITEM(WildTeleport.messageFile.getMessageFile().getString("changedCategoryItem")),
    CATEGORY_ALREADY_EXISTS(WildTeleport.messageFile.getMessageFile().getString("categoryAlreadyExists")),
    CHANGED_CATEGORY_NAME(WildTeleport.messageFile.getMessageFile().getString("changedCategoryName")),
    CHANGED_CATEGORY_LORE(WildTeleport.messageFile.getMessageFile().getString("changedCategoryLore")),
    UPDATED_RADIUS(WildTeleport.messageFile.getMessageFile().getString("updatedRadius"));


    private final String msg;

    /**
     * Constructor for message enum.
     * @param msg String containing the message.
     */
    Messages(String msg) {
        this.msg = msg.replace('&', '§');
    }

    /**
     * Getter for a message.
     * @return String containing the message.
     */
    public String getMessage() {
        return this.msg.replace('&', '§');
    }
}
