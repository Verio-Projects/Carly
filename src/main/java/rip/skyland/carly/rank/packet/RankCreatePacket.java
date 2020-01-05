package rip.skyland.carly.rank.packet;

import lombok.AllArgsConstructor;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class RankCreatePacket implements RedisPacket {

    private UUID uuid;
    private String name;

    @Override
    public void onReceive() {
        if(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name) != null || Core.INSTANCE.getHandlerManager().getRankHandler().getRankByUuid(uuid) != null)
            return;

        Core.INSTANCE.getHandlerManager().getRankHandler().createRank(name, uuid, false);
    }

    @Override
    public void onSend() {

    }
}
