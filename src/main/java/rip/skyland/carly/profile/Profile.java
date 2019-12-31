package rip.skyland.carly.profile;

import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.rank.grants.IGrant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Profile {

    private UUID uuid;
    private String playerName;
    private List<IGrant> grants;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.grants = new ArrayList<>();
    }

}