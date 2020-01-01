package rip.skyland.carly.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.rank.grants.GrantProcedure;
import rip.skyland.carly.util.CC;

@Getter
@AllArgsConstructor
public class PlayerListener implements Listener {

    private ProfileHandler profileHandler;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        Profile profile = profileHandler.createProfile(player.getUniqueId());
        profile.setPlayerName(player.getName());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfileByUuid(player.getUniqueId());

        profileHandler.unloadProfile(profile);
    }

    @EventHandler()
    public void onChat(AsyncPlayerChatEvent event) {
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

        if(!event.isCancelled()) {
            event.setFormat(CC.translate(profile.getRank().getPrefix() + profile.getPlayerName() + "&7: &f") + event.getMessage().replace("%", "%%"));
        }
    }

}
