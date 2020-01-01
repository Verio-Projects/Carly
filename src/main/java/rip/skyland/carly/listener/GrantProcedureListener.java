package rip.skyland.carly.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.rank.grants.GrantProcedure;
import rip.skyland.carly.util.CC;

public class GrantProcedureListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(Core.INSTANCE.getHandlerManager().getRankHandler().getProcedureByUuid(player.getUniqueId()) != null) {
            GrantProcedure procedure = Core.INSTANCE.getHandlerManager().getRankHandler().getProcedureByUuid(player.getUniqueId());

            Profile target = CoreAPI.INSTANCE.getProfileByUuid(procedure.getUuid());

            if(event.getMessage().equalsIgnoreCase("cancel")) {
                Core.INSTANCE.getHandlerManager().getRankHandler().getGrantProcedures().remove(procedure);
                player.sendMessage(CC.translate("&cYou have cancelled granting " + target.getDisplayName()));
            }

            if(procedure.getReason().isEmpty()) {
                procedure.setReason(event.getMessage());
                player.sendMessage(CC.translate("&ePlease type a duration for this grant, \"perm\" for permanent or type &ccancel &eto cancel."));
                return;
            }

            if(procedure.getDuration().isEmpty()) {
                procedure.setDuration(event.getMessage());

                procedure.finishProcedure();
            }
        }
    }

}
