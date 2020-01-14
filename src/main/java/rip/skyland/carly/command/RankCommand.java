package rip.skyland.carly.command;

import com.sun.jdi.connect.Connector;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.Locale;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.RankHandler;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

import java.util.UUID;

@AllArgsConstructor
public class RankCommand {

    private RankHandler handler;

    @Command(names = "rank", permission = "core.rank")
    public void executeHelp(CommandSender sender) {
        CC.translate(Locale.RANK_HELP.getAsStringList()).forEach(sender::sendMessage);
    }

    @Command(names = "rank create", permission = "core.rank.create")
    public void executeCreate(CommandSender sender, String name) {
        if (handler.getRankByName(name) != null) {
            sender.sendMessage(CC.translate(Locale.RANK_ALREADY_EXISTS.getAsString()));
            return;
        }

        handler.createRank(name, UUID.randomUUID(), true);
        sender.sendMessage(CC.translate(Locale.RANK_CREATED.getAsString().replace("%rank%", name)));
    }

    @Command(names = "rank delete", permission = "core.rank.create")
    public void executeDelete(CommandSender sender, String name) {
        if (handler.getRankByName(name) == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        sender.sendMessage(CC.translate(Locale.RANK_DELETED.getAsString().replace("%rank%", handler.getRankByName(name).getName())));
        handler.deleteRank(handler.getRankByName(name));

    }

    @Command(names = "rank prefix", permission = "core.rank.prefix")
    public void executePrefix(CommandSender sender, String name, String prefix) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        rank.setPrefix(prefix);
        sender.sendMessage(CC.translate(Locale.RANK_SET_PREFIX.getAsString().replace("%rank%", rank.getName()).replace("%prefix%", prefix)));
        handler.saveRank(rank);
    }

    @Command(names = "rank suffix", permission = "core.rank.suffix")
    public void executeSuffix(CommandSender sender, String name, String suffix) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        rank.setSuffix(suffix);
        sender.sendMessage(CC.translate(Locale.RANK_SET_SUFFIX.getAsString().replace("%rank%", rank.getName()).replace("%suffix%", suffix)));
        handler.saveRank(rank);
    }

    @Command(names = "rank weight", permission = "core.rank.weight")
    public void executeWeight(CommandSender sender, String name, int weight) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        rank.setWeight(weight);
        sender.sendMessage(CC.translate(Locale.RANK_SET_WEIGHT.getAsString().replace("%rank%", rank.getName()).replace("%weight%", weight + "")));
        handler.saveRank(rank);
    }

    @Command(names = "rank permission", permission = "core.rank.permission")
    public void executePermission(CommandSender sender, String name, @Param(name = "add|remove") String type, String permission) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        switch (type.toLowerCase()) {
            case "add": {
                handler.addPermission(rank, permission);
                sender.sendMessage(CC.translate(Locale.RANK_ADD_PERMISSION.getAsString().replace("%rank%", rank.getName()).replace("%permission%", permission)));
            }
            break;

            case "remove": {
                handler.removePermission(rank, permission);
                sender.sendMessage(CC.translate(Locale.RANK_REMOVE_PERMISSION.getAsString().replace("%rank%", rank.getName()).replace("%permission%", permission)));
            }
            break;

            default: {
                sender.sendMessage(CC.translate("&cUsage: /rank permission <name> <add|remove> <permission>"));
            }
        }

        handler.saveRank(rank);
    }

    @Command(names = "rank color", permission = "core.rank.color")
    public void executeColor(CommandSender sender, String name,CC color, boolean bold, boolean italic) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        rank.setColor(color);
        rank.setBold(bold);
        rank.setItalic(italic);

        sender.sendMessage(CC.translate(Locale.RANK_SET_COLOR.getAsString().replace("%rank%", rank.getName())
                .replace("%color%", color + color.toString()).replace("%bold%", bold + "").replace("%italic%", italic + "")));

        handler.saveRank(rank);
    }

    @Command(names="rank inherits", permission="core.rank.inherits")
    public void executeInheritance(CommandSender sender, String name, @Param(name="add|remove") String type, String inherit) {
        Rank rank = handler.getRankByName(name);
        Rank inheritRank = handler.getRankByName(inherit);

        if (rank == null || inheritRank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        switch(type.toLowerCase()) {
            case "add": {
                handler.addInheritance(rank, inheritRank.getUuid());
                sender.sendMessage(CC.translate(Locale.RANK_ADD_INHERITANCE.getAsString()));
            } break;

            case "remove": {
                handler.removeInheritance(rank, inheritRank.getUuid());
                sender.sendMessage(CC.translate(Locale.RANK_REMOVE_INHERITANCE.getAsString()));
            } break;

            default: {
                sender.sendMessage(CC.translate("&c/rank inherits <add|remove> <inherit>"));
            }
        }
    }

    @Command(names = "rank info", permission = "core.rank.info")
    public void executeInfo(CommandSender sender, String name) {
        Rank rank = handler.getRankByName(name);

        if (rank == null) {
            sender.sendMessage(CC.translate(Locale.RANK_DOES_NOT_EXIST.getAsString()));
            return;
        }

        CC.translate(Locale.RANK_INFO.getAsStringList()).forEach(string -> sender.sendMessage(CC.translate(string.replace("%rank%", rank.getName())
                .replace("%prefix%", rank.getPrefix())
                .replace("%suffix%", rank.getSuffix())
                .replace("%weight%", rank.getWeight() + "")
                .replace("%color%", rank.getColor().name())
                .replace("%uuid%", rank.getUuid().toString())
                .replace("%permissionsAmount%", rank.getPermissions().size() + "")
                .replace("%permissions%", String.join(", ", rank.getPermissions()))
                .replace("%default%", rank.getName().equalsIgnoreCase("Default") + ""))));

    }
}
