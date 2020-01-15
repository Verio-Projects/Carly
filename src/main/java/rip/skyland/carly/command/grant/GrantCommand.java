package rip.skyland.carly.command.grant;

import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.GrantProcedure;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;
import rip.skyland.carly.rank.grants.impl.TemporaryGrant;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.TimeUtil;
import rip.skyland.carly.util.WoolColor;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;
import rip.skyland.carly.util.menu.button.Button;
import rip.skyland.carly.util.menu.pagination.PaginatedMenu;

import java.util.ArrayList;
import java.util.List;

public class GrantCommand {

    @Command(names="setrank", permission="core.setrank")
    public void performSetRank(CommandSender sender, @Param(name = "player") String target, @Param(name = "rank") String rankName) {
        Profile profile = CoreAPI.INSTANCE.getProfileByNameAndCreate(target);

        if(profile == null) {
            sender.sendMessage(CC.translate("&cThat player does not exist"));
            return;
        }

        Rank rank = Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(rankName);
        if(rank == null) {
            sender.sendMessage(CC.translate("&cThat rank does not exist"));
            return;
        }


        profile.getGrants().stream().filter(grant -> grant.getRank().getWeight() > rank.getWeight() && grant instanceof PermanentGrant).forEach(grant -> grant.setActive(false));
        profile.getGrants().stream().filter(grant -> grant.getRank().getWeight() > rank.getWeight() && grant instanceof TemporaryGrant).forEach(grant -> profile.getGrants().remove(grant));

        Core.INSTANCE.getHandlerManager().getProfileHandler().addGrant(new PermanentGrant(rank, profile.getUuid(), "set rank", sender.getName(), System.currentTimeMillis(), true), profile);
        sender.sendMessage(CC.translate(Locale.SET_RANK.getAsString().replace("%player%", profile.getPlayerName()).replace("%rank%", rank.getDisplayName())));
    }

    @Command(names = "grant", permission = "core.grant")
    public void performGrant(CommandSender sender, @Param(name = "player") String target, @Param(name = "rank", value = "not set") String rankName, @Param(name="reason", value="console grant") String reason, @Param(name="duration", value="perm") String duration) {
       Profile profile = CoreAPI.INSTANCE.getProfileByNameAndCreate(target);

       if(profile == null) {
           sender.sendMessage(CC.translate("&cThat player does not exist"));
           return;
       }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            Core.INSTANCE.getMenuHandler().createMenu(new PaginatedMenu(player) {
                @Override
                public String getTitle() {
                    return Locale.GRANT_MENU_TITLE.getAsString();
                }

                @Override
                public List<Button> getButtons() {
                    List<Button> buttons = new ArrayList<>();
                    List<Rank> ranks = Core.INSTANCE.getHandlerManager().getRankHandler().getRanks();
                    for (int i = 0; i < ranks.size(); i++) {
                        Rank rank = ranks.get(i);

                        List<String> lore = new ArrayList<>();
                        Locale.GRANT_MENU_ITEM.getAsStringList().forEach(string -> lore.add(string.replace("%rank%", rank.getDisplayName()).replace("%player%", profile.getDisplayName())));

                        buttons.add(new Button(i, Material.WOOL, rank.getDisplayName(), CC.translate(lore), WoolColor.getWoolColor(rank.getColor()), player -> {
                            if (!rank.getName().equalsIgnoreCase("Default")) {

                                player.closeInventory();
                                new GrantProcedure(rank, profile.getUuid(), player.getUniqueId(), CoreAPI.INSTANCE.getProfileByPlayer(player).getDisplayName());

                                player.sendMessage(CC.translate(Locale.GRANT_SET_REASON.getAsString()));
                            }
                        }));
                    }

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
            });

        } else {
            Rank rank = Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(rankName);

            if (rank == null) {
                sender.sendMessage(CC.translate(Locale.ERROR_RANK_DOES_NOT_EXIST.getAsString()));
                return;
            }

            IGrant grant;

            if(duration.equalsIgnoreCase("perm")) {
                grant = new PermanentGrant(rank, profile.getUuid(), reason, "&4CONSOLE", System.currentTimeMillis(), true);
            } else {
                grant = new TemporaryGrant(rank, profile.getUuid(), reason, "&4CONSOLE", System.currentTimeMillis(), (System.currentTimeMillis()+ TimeUtil.parseTime(duration)), true);
            }

            Core.INSTANCE.getHandlerManager().getProfileHandler().addGrant(grant, profile);
            sender.sendMessage(CC.translate(Locale.GRANT_FINISHED.getAsString()));
        }
    }

    @Command(names = "grants", permission = "core.grants")
    public void performGrants(Player player, @Param(name = "player") String target) {
        Profile profile = CoreAPI.INSTANCE.getProfileByNameAndCreate(target);

        if(profile == null) {
            player.sendMessage(CC.translate("&cThat player does not exist"));
            return;
        }
        Core.INSTANCE.getMenuHandler().createMenu(new PaginatedMenu(player) {
            @Override
            public String getTitle() {
                return Locale.GRANTS_MENU_TITLE.getAsString().replace("%player%", profile.getPlayerName());
            }

            @Override
            public List<Button> getButtons() {
                List<Button> buttons = new ArrayList<>();

                for (int i = profile.getGrants().size() - 1; i >= 0; i--) {
                    IGrant grant = profile.getGrants().get(i);
                    String expirationString = !grant.isActive() ? "Expired" : (grant instanceof PermanentGrant ? "Never" : TimeUtil.millisToRoundedTime(((TemporaryGrant) grant).getExpirationTime()));

                    List<String> lore = new ArrayList<>();
                    Locale locale = grant.isActive() ? Locale.GRANTS_MENU_ITEM_ACTIVE : Locale.GRANTS_MENU_ITEM_INACTIVE;

                    locale.getAsStringList().forEach(string -> lore.add(string.replace("%rank%", grant.getRank().getDisplayName())
                            .replace("%granter%", grant.getGranterName())
                            .replace("%expiration%", expirationString)
                            .replace("%grantDate%", TimeUtil.unixToDate(grant.getGrantTime()))
                            .replace("%reason%", grant.getReason())));

                    buttons.add(new Button(i, Material.WOOL, "&c#" + DigestUtils.sha256Hex(grant.getRank().getUuid().toString()).substring(0, 8), lore, WoolColor.getWoolColor(grant.isActive() ? CC.GREEN : CC.RED), player -> {
                        if (!grant.getRank().getName().equalsIgnoreCase("Default")) {
                            grant.setActive(!grant.isActive());
                            this.updatePage();
                        }
                    }));
                }

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
        });

    }
}
