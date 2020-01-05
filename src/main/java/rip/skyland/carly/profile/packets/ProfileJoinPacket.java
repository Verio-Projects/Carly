package rip.skyland.carly.profile.packets;

import lombok.AllArgsConstructor;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class ProfileJoinPacket implements RedisPacket {

    private UUID uuid;

    @Override
    public void onReceive() {

    }

    @Override
    public void onSend() {

    }
}
