package rip.skyland.carly.command.punishments;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.punishments.IPunishment;
import rip.skyland.carly.punishments.PunishmentHandler;
import rip.skyland.carly.punishments.PunishmentType;
import rip.skyland.carly.punishments.impl.PermanentPunishment;
import rip.skyland.carly.punishments.impl.TemporaryPunishment;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.TimeUtil;
import rip.skyland.carly.util.WoolColor;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;
import rip.skyland.carly.util.menu.Menu;
import rip.skyland.carly.util.menu.button.Button;
import rip.skyland.carly.util.menu.pagination.PaginatedMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class HistoryCommand {

    private PunishmentHandler handler; // initialize here so it doesnt have to pull it from Core#INSTANCE#getHandlerManager every time

    @Command(names={"history", "c"}, permission="core.history")
    public void execute(Player player, @Param(name="player") String targetName) {
        Profile profile = CoreAPI.INSTANCE.getProfileByNameAndCreate(targetName);

        if(profile == null) {
            player.sendMessage(CC.translate("&cThat player does not exist"));
            return;
        }

        Core.INSTANCE.getMenuHandler().createMenu(getMainMenu(player, profile));
    }

    private Menu getMainMenu(Player player, Profile target) {
        return new Menu(player) {
            @Override
            public String getTitle() {
                return target.getPlayerName() + "'s Punishments";
            }

            @Override
            public List<Button> getButtons() {
                return Arrays.asList(
                        getMainMenuButton(2, PunishmentType.BAN, target, this),
                        getMainMenuButton(5, PunishmentType.MUTE, target, this)
                );
            }

            @Override
            public void onClose() {
                Core.INSTANCE.getMenuHandler().destroyMenu(this);
            }

            @Override
            public int size() {
                return 9;
            }
        };
    }

    private PaginatedMenu getPunishmentMenu(Player player, PunishmentType type, Profile target) {
        return new PaginatedMenu(player) {
            @Override
            public String getTitle() {
                return target.getPlayerName() + "'s Punishments";
            }

            @Override
            public List<Button> getButtons() {
                List<Button> buttons = new ArrayList<>();
                List<IPunishment> punishments = handler.getPunishments().stream().filter(punishment -> punishment.getPunishmentType().equals(type) && punishment.getTargetUuid().equals(target.getUuid())).collect(Collectors.toList());

                IntStream.range(0, punishments.size()).forEach(i -> {
                    IPunishment punishment = punishments.get(i);

                    String expirationString = !punishment.isActive() ? "Expired" : (punishment instanceof PermanentPunishment ? "Never" : TimeUtil.millisToRoundedTime(((TemporaryPunishment) punishment).getExpiration()));


                    List<String> strings = new ArrayList<>();

                    Locale locale = punishment.isActive() ? Locale.HISTORY_PUNISHMENT_BUTTON : Locale.HISTORY_PUNISHMENT_BUTTON_INACTIVE;
                    locale.getAsStringList().forEach(string -> strings.add(string
                            .replace("%reason%", punishment.getReason().replace("-s", ""))
                            .replace("%punisher%", punishment.getPunisher())
                            .replace("%expiration%", expirationString)
                            .replace("%unpunishReason%", punishment.getUnpunishReason())
                            .replace("%date%", TimeUtil.unixToDate(punishment.getPunishDate()))));

                    buttons.add(new Button(i, Material.WOOL, CC.RED + "#" + DigestUtils.sha256Hex(punishment.getUuid().toString()).substring(0, 8), strings,
                            punishment.isActive() ? WoolColor.getWoolColor(CC.ORANGE) : WoolColor.getWoolColor(CC.RED), player -> {
                    }));
                });

                return buttons;
            }

            @Override
            public void onClose() {
                Core.INSTANCE.getMenuHandler().destroyMenu(this);
            }

            @Override
            public int size() {
                return 9;
            }
        };
    }

    private Button getMainMenuButton(int index, PunishmentType type, Profile profile, Menu originalMenu) {
        List<String> strings = new ArrayList<>();
        Locale.HISTORY_MAIN_BUTTON.getAsStringList().forEach(string -> strings.add(string
                .replace("%active%", "" + handler.getPunishments().stream().filter(punishment -> punishment.getTargetUuid().equals(profile.getUuid()) && punishment.getPunishmentType().equals(type) && punishment.isActive()).count())
                .replace("%inactive%", "" + handler.getPunishments().stream().filter(punishment -> punishment.getTargetUuid().equals(profile.getUuid()) &&  punishment.getPunishmentType().equals(type) && !punishment.isActive()).count())));

        return new Button(index, Material.WOOL, CC.ORANGE.toString() + type.name().charAt(0) + type.name().toLowerCase().substring(1), strings, WoolColor.getWoolColor(CC.ORANGE), player -> {
            player.closeInventory();
            Core.INSTANCE.getMenuHandler().createMenu(getPunishmentMenu(player, type, profile));
        });
    }

}
