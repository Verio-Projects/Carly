package rip.skyland.carly.util.database.redis.packet;

import rip.skyland.carly.util.database.IPacket;

public interface RedisPacket extends IPacket {

    void onReceive();
    void onSend();

}
