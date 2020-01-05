package rip.skyland.carly.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.handler.impl.ServerHandler;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.rank.grants.GrantProcedure;
import rip.skyland.carly.util.CC;

public class ChatListener implements Listener {

    @EventHandler()
    public void onChat(AsyncPlayerChatEvent event) {
        ProfileHandler profileHandler = Core.INSTANCE.getHandlerManager().getProfileHandler();

        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfileByUuid(player.getUniqueId());

        if(Core.INSTANCE.getHandlerManager().getRankHandler().getProcedureByUuid(player.getUniqueId()) != null) {
            event.setCancelled(true);
            GrantProcedure procedure = Core.INSTANCE.getHandlerManager().getRankHandler().getProcedureByUuid(player.getUniqueId());

            if(event.getMessage().equalsIgnoreCase("cancel")) {
                Core.INSTANCE.getHandlerManager().getRankHandler().getGrantProcedures().remove(procedure);
                player.sendMessage(CC.translate(Locale.GRANT_CANCELLED_GRANTING.getAsString()));
                return;
            }

            if(procedure.getReason().equalsIgnoreCase("Not set")) {
                procedure.setReason(event.getMessage());
                player.sendMessage(CC.translate(Locale.GRANT_SET_DURATION.getAsString()));
                return;
            }

            if(procedure.getDuration().equalsIgnoreCase("Not set")) {
                procedure.setDuration(event.getMessage());

                procedure.finishProcedure();
            }
            return;
        }

        ServerHandler serverHandler = Core.INSTANCE.getHandlerManager().getServerHandler();

        if(serverHandler.isChatMuted() && !player.hasPermission("core.bypass.mutechat")) {
            player.sendMessage(CC.translate(Locale.CHAT_MUTED.getAsString()));
            event.setCancelled(true);
            return;
        }

        if(serverHandler.getChatSlowDuration() > 0 && profile.getLastChat() > 0) {
            if((profile.getLastChat()+serverHandler.getChatSlowDuration())-System.currentTimeMillis() > 0) {
                player.sendMessage(CC.translate(Locale.CHAT_SLOWCHAT.getAsString()));
                event.setCancelled(true);
                return;
            }

        }

        if(!event.isCancelled()) {
            event.setFormat(CC.translate(profile.getRank().getPrefix() + profile.getPlayerName() + "&7: &f") + event.getMessage().replace("%", "%%"));
            profile.setLastChat(System.currentTimeMillis());
        }
    }

}
