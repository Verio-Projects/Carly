package rip.skyland.carly;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public enum Locale {

    SERVER_NAME("server.id", "dev-1"),

    MONGO_HOST("mongo.host", "127.0.0.1"),
    MONGO_PORT("mongo.port", 27017),
    MONGO_DATABASE("mongo.database", "carly"),
    MONGO_USERNAME("mongo.auth.username", ""),
    MONGO_PASSWORD("mongo.auth.password", ""),

    REDIS_HOST("redis.host", "127.0.0.1"),
    REDIS_PORT("redis.port", 6379),
    REDIS_PASSWORD("redis.auth.password", ""),
    REDIS_CHANNEL("redis.channel", "carly"),


    RANK_HELP("rank.help", Arrays.asList(
            "&9&m" + StringUtils.repeat("-", 39),
            "&c/rank create <name>",
            "&c/rank delete <name>",
            "&c/rank prefix <name> <prefix>",
            "&c/rank suffix <name> <suffix>",
            "&c/rank weight <name> <weight>",
            "&c/rank color <name> <color> <bold> <italic>",
            "&c/rank permission <name> <add|remove> <permission>",
            "&c/rank inherits <name> <add|remove> <inherit>",
            "&c/rank info <name>",
            "&9&m" + StringUtils.repeat("-", 39)
    )),
    RANK_ALREADY_EXISTS("rank.error.already_exists", "&cThat rank already exists."),
    RANK_DOES_NOT_EXIST("rank.error.does_not_exist", "&cThat rank does not exist."),
    RANK_CREATED("rank.created_rank", "&aYou have created a rank."),
    RANK_DELETED("rank.deleted_rank", "&cYou have deleted a rank."),
    RANK_SET_PREFIX("rank.set_prefix", "&eYou have set the prefix of a rank"),
    RANK_SET_SUFFIX("rank.set_suffix", "&eYou have set the suffix of a rank"),
    RANK_SET_WEIGHT("rank.set_weight", "&eYou have set the weight of a rank"),
    RANK_ADD_PERMISSION("rank.add_permission", "&eYou have added a permission to a rank"),
    RANK_REMOVE_PERMISSION("rank.add_permission", "&eYou have removed a permission from a rank"),
    RANK_SET_COLOR("rank.set_color", "&eYou have set the color of a rank"),
    RANK_ADD_INHERITANCE("rank.add_inheritance", "&eYou have added an inheritance to a rank"),
    RANK_REMOVE_INHERITANCE("rank.remove_inheritance", "&eYou have removed an inheritance from a rank"),
    RANK_INFO("rank.info", Arrays.asList(
            "&9&m" + StringUtils.repeat("-", 39),
            "&a&l%rank% &7- &c%default%",
            "",
            "&6-> &eColor: %color%",
            "&6-> &ePrefix: %prefix%",
            "&6-> &eSuffix: %suffix%",
            "&6-> &eUUID: &c%uuid%",
            "&6-> &eWeight: &c%weight%",
            "",
            "&6-> &ePermissions &6(%permissionsAmount%)&e: &c%permissions%",
            "&6-> &eInheritances &6(%inheritsAmount%)&e: &c%inheritances%",
            "&9&m" + StringUtils.repeat("-", 39)
            )),

    GRANTS_MENU_TITLE("grant.grants_menu.title", "%player%'s Grants"),
    GRANTS_MENU_ITEM_INACTIVE("grant.grants_menu.item.inactive", Arrays.asList(
            "&7&m--------------------------",
            "&eRank: %rank%",
            "&eGranted By: %granter%",
            "&eExpires In: %expiration%",
            "&eGrant Date: &f%grantDate%",
            "&eReason: &f%reason%",
            "",
            "&eClick to un-expire this grant",
            "&7&m--------------------------"
    )),
    GRANTS_MENU_ITEM_ACTIVE("grant.grants_menu.item.active", Arrays.asList(
            "&7&m--------------------------",
            "&eRank: %rank%",
            "&eGranted By: %granter%",
            "&eExpires In: %expiration%",
            "&eGrant Date: &f%grantDate%",
            "&eReason: &f%reason%",
            "",
            "&aClick to expire this grant",
            "&7&m--------------------------"
    )),

    GRANT_SET_REASON("grant.set_reason", "&ePlease type a reason for this grant to be added, or type &ccancel &eto cancel."),
    GRANT_SET_DURATION("grant.set_duration", "&ePlease type a duration for this grant, \"perm\" for permanent or type &ccancel &eto cancel."),
    GRANT_CANCELLED_GRANTING("grant.cancelled_granting", "&cYou have cancelled granting"),
    GRANT_FINISHED("grant.finished_grant", "&aYou have finished granting"),
    GRANT_MENU_TITLE("grant.menu.title", "Choose a Rank"),
    GRANT_MENU_ITEM("grant.menu.item", Arrays.asList(
            "&7&m--------------------------",
            "&9Click to grant %player% &9the %rank% &9rank.",
            "&7&m--------------------------"
    )),

    MUTED("punishment.muted", "&cYou are currently muted for: &f%reason%"),
    PUNISHMENT_BAN_KICK("punishment.ban.kick_message", "&cYou have been banned off the SkylandRIP Network\n \n \n&cVisit skyland.rip/appeal to appeal for an unban."),
    PUNISHMENT_MUTE_SILENT("punishment.mute.silent_broadcast", "&7[Silent] %player% &ahas been muted by %punisher%&a."),
    PUNISHMENT_BAN_SILENT("punishment.ban.silent_broadcast", "&7[Silent] %player% &ahas been banned by %punisher%&a."),
    PUNISHMENT_BAN("punishment.ban.broadcast", "%player% &ahas been banned by %punisher%&a."),
    PUNISHMENT_MUTE("punishment.mute.broadcast", "%player% &ahas been muted by %punisher%&a."),
    PUNISHMENT_UNMUTE_SILENT("punishment.mute.undo_silent_broadcast", "&7[Silent] %player% &ahas been unmuted by %punisher%&a."),
    PUNISHMENT_UNBAN_SILENT("punishment.ban.undo_silent_broadcast", "&7[Silent] %player% &ahas been unbanned by %punisher%&a."),
    PUNISHMENT_UNBAN("punishment.ban.undo_broadcast", "%player% &ahas been unbanned by %punisher%&a."),
    PUNISHMENT_UNMUTE("punishment.mute.undo_broadcast", "%player% &ahas been unmuted by %punisher%&a."),

    HISTORY_MAIN_BUTTON("punishment.history.main_button", Collections.singletonList("&7%active% active punishments, %inactive% inactive punishments")),
    HISTORY_PUNISHMENT_BUTTON("punishment.history.punishment_button.active", Arrays.asList(
            "",
            "&6Reason: &f%reason%",
            "&6Punisher: &f%punisher%",
            "&6Expiration: &f%expiration%",
            "",
            "&6Punishment Date: &f%date%"
    )),

    HISTORY_PUNISHMENT_BUTTON_INACTIVE("punishment.history.punishment_button.inactive", Arrays.asList(
            "",
            "&6Reason: &f%reason%",
            "&6Punisher: &f%punisher%",
            "&6Expiration: &f%expiration%",
            "",
            "&6Punishment Date: &f%date%",
            "&6Unpunish Reason: &f%unpunishReason%"
    )),

    CHAT_MUTED("chat.mutechat", "&cThe chat is currently temporarily muted."),
    CHAT_CLEARED("chat.cleared", "&dThe public chat has been cleared."),
    CHAT_MUTE("chat.mute", "&dThe public chat has been muted."),
    CHAT_UNMUTE("chat.unmute", "&dThe public chat has been unmuted."),
    CHAT_SLOW("chat.slow", "&dThe public chat has been slowed"),
    CHAT_UNSLOW("chat.unslow", "&dThe public chat has been unslowed"),
    CHAT_UNSLOW_ALREADY_UNSLOWED("chat.unslow_already_unslowed", "&cThe chat is already unslowed."),
    CHAT_SLOWCHAT("chat.slowchat", "&cYou are currently still on chat cooldown"),

    PROFILE_CONNECT("profile.connect", "&9[Staff] &b%playerName% &ajoined &bthe network (%server%)"),
    PROFILE_SWITCH_SERVER("profile.switch_server", "&9[Staff] &b%playerName% &bjoined %newServer% (from %previousServer%)"),
    PROFILE_DISCONNECT("profile.disconnect", "&9[Staff] &b%playerName% &cleft &bthe network (from %server%)"),

    STAFF_CHAT("chat.staffchat", "&7[%server%] &5%playerName%: &d%message%"),

    CHANGE_GAMEMODE("general.change_gamemode", "&6You have set your gamemode to &f%gamemode%"),
    CHANGE_GAMEMODE_TARGET("general.change_gamemode_target", "&6You have set &f%player%'s &6gamemode to &f%gamemode%"),
    SET_RANK("rank.set_rank", "&eYou have set &f%player%'s &erank to %rank%"),

    PING("general.ping", "&e%player%&e's Ping: &c%ping%");

    private Object value;

    @Getter
    private Map<String, String> replacements;

    Locale(String path, Object defaultValue) {
        this.replacements = new HashMap<>();

        if(Core.INSTANCE.getPlugin().getConfig().contains(path)) {
            // set value of to value in config.yml
            this.value = Core.INSTANCE.getPlugin().getConfig().get(path);
        } else {
            // set value to default and add a field inside of it inside of the config.yml
            Core.INSTANCE.getPlugin().getConfig().set(path, defaultValue);
            this.value = defaultValue;
        }

        Core.INSTANCE.getPlugin().saveConfig();
    }

    public List<String> getAsStringList() {
        List<String> toReturn = new ArrayList<>((List<String>) this.value);
        for(int index = 0; index < toReturn.size(); index++) {
            String toReplace = toReturn.get(index);
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                toReplace = toReplace.replace(entry.getKey(), entry.getValue());
            }

            toReturn.remove(index);
            toReturn.add(index, toReplace);
        }


        return (List<String>) this.value;
    }

    public String getAsString() {
        String toReturn = (String) this.value;
        for(Map.Entry<String, String> entry : replacements.entrySet()) {
            toReturn = toReturn.replace(entry.getKey(), entry.getValue());
        }

        return toReturn;
    }

    public int getAsInteger() {
        return (int) this.value;
    }

}