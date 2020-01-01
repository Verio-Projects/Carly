package rip.skyland.carly.rank.grants;

import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;
import rip.skyland.carly.rank.grants.impl.TemporaryGrant;
import rip.skyland.carly.util.TimeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GrantProcedure {

    private Rank rank;
    private UUID uuid, granterUuid;
    private String granterName;

    public GrantProcedure(Rank rank, UUID target, UUID granterUuid, String granterName) {
        this.uuid = target;
        this.rank = rank;
        this.granterUuid = granterUuid;
        this.granterName = granterName;
    }

    private String reason = "Not set";
    private String duration = "Not set";

    private List<String> permanent = Arrays.asList(
            "permanent",
            "perm",
            "p"
    );

    public void finishProcedure() {
        if(!reason.equalsIgnoreCase("Not set") && !duration.equalsIgnoreCase("Not set")) {
            if(permanent.stream().anyMatch(string -> string.equalsIgnoreCase(duration))) {
                Core.INSTANCE.getHandlerManager().getProfileHandler().addGrant(new PermanentGrant(rank, uuid, reason, granterName, System.currentTimeMillis(), true), CoreAPI.INSTANCE.getProfileByUuid(uuid));
            } else {
                Core.INSTANCE.getHandlerManager().getProfileHandler().addGrant(new TemporaryGrant(rank, uuid, reason, granterName, System.currentTimeMillis(), System.currentTimeMillis()+ TimeUtil.parseTime(duration), true), CoreAPI.INSTANCE.getProfileByUuid(uuid));
            }
        }
    }



}
