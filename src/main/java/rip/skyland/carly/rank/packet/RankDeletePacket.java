package rip.skyland.carly.rank.packet;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import rip.skyland.carly.Core;
import rip.skyland.carly.rank.RankHandler;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class RankDeletePacket implements RedisPacket {

    private UUID rank;

    @Override
    public void onReceive() {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByUuid(rank) != null, "tried deleting non-existing rank");
        RankHandler rankHandler = Core.INSTANCE.getHandlerManager().getRankHandler();

        rankHandler.getRankByUuid(rank).delete();
        rankHandler.getRanks().remove(rankHandler.getRankByUuid(rank));
    }

    @Override
    public void onSend() {}
}
