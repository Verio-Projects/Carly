package rip.skyland.carly.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.skyland.carly.Core;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.profile.ProfileHandler;

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
        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfileByUuid(player.getUniqueId());

        profileHandler.unloadProfile(profile);
    }

}
