package rip.skyland.carly;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum Locale {

    MONGO_HOST("mongo.host", "127.0.0.1"),
    MONGO_PORT("mongo.port", 27017),
    MONGO_DATABASE("mongo.database", "carly"),
    MONGO_USERNAME("mongo.auth.username", ""),
    MONGO_PASSWORD("mongo.auth.password", ""),

    REDIS_HOST("redis.host", "127.0.0.1"),
    REDIS_PORT("redis.port", 6379),
    REDIS_PASSWORD("redis.auth.password", ""),


    RANK_HELP("rank.help", Arrays.asList(
            "&9&m" + StringUtils.repeat("-", 39),
            "&c/rank create <name>",
            "&c/rank delete <name>",
            "&c/rank prefix <name> <prefix>",
            "&c/rank suffix <name> <suffix>",
            "&c/rank weight <name> <weight>",
            "&c/rank setcolor <name> <color> <bold> <italic>",
            "&c/rank permission <name> <add|remove> <permission>",
            "&9&m" + StringUtils.repeat("-", 39)
    )),
    RANK_ALREADY_EXISTS("rank.error.already_exists", "&cThat rank already exists."),
    RANK_DOES_NOT_EXIST("rank.error.does_not_exist", "&cThat rank does not exist."),
    RANK_CREATED("rank.created_rank", "&eYou have created a new rank called &6\"%rank%\"."),
    RANK_DELETED("rank.deleted_rank", "&eYou have deleted the &6\"%rank%\" rank."),
    RANK_SET_PREFIX("rank.set_prefix", "&eYou have set the prefix of &6\"%rank%\" &eto &6\"%prefix%\""),
    RANK_SET_SUFFIX("rank.set_suffix", "&eYou have set the suffix of &6\"%rank%\" &eto &6\"%suffix%\""),
    RANK_SET_WEIGHT("rank.set_weight", "&eYou have set the weight of &6\"%rank%\" &eto &6\"%weight%\""),
    RANK_ADD_PERMISSION("rank.add_permission", "&eYou have added a permission to &6\"%rank%\" &f(\"%permission%\")"),
    RANK_REMOVE_PERMISSION("rank.add_permission", "&eYou have removed a permission from &6\"%rank%\" &f(\"%permission%\")"),
    RANK_SET_COLOR("rank.set_color", "&eYou have set the color of &6\"%rank%\" &eto %color%&e, &f%bold%&e, &f%italic%"),
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
            "&9&m" + StringUtils.repeat("-", 39)
            ));

    private Object value;

    Locale(String path, Object defaultValue) {
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
        return (List<String>) this.value;
    }

    public String getAsString() {
        return (String) this.value;
    }

    public boolean getAsBoolean() {
        return (boolean) this.value;
    }

    public int getAsInteger() {
        return (int) this.value;
    }

}
