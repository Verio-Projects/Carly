package rip.skyland.carly.handler.impl;

import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.handler.IHandler;

@Getter
@Setter
public class ServerHandler implements IHandler {

    private boolean chatMuted;
    private long chatSlowDuration;

    @Override
    public void load() {
        this.chatMuted = false;
        this.chatSlowDuration = 0L;
    }

    @Override
    public void unload() {

    }
}