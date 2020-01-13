package rip.skyland.carly.command;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.handler.impl.ServerHandler;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;
import rip.skyland.carly.util.database.redis.packet.impl.StaffChatPacket;

import java.util.stream.IntStream;

@AllArgsConstructor
public class ChatCommands {

    private ServerHandler handler;

    @Command(names={"cc", "clearchat"}, permission="core.chat.clear")
    public void executeClearChat(CommandSender sender) {
        Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasPermission("core.bypass.clearchat")).forEach(player -> IntStream.range(0, 100).forEach(i -> player.sendMessage("")));

        Bukkit.broadcastMessage(CC.translate(Locale.CHAT_CLEARED.getAsString()));
    }

    @Command(names={"mc", "mutechat"}, permission="core.chat.mute")
    public void executeMuteChat(CommandSender sender) {
        if(handler.isChatMuted()) {
            Bukkit.broadcastMessage(CC.translate(Locale.CHAT_UNMUTE.getAsString()));
            handler.setChatMuted(false);
        } else {
            Bukkit.broadcastMessage(CC.translate(Locale.CHAT_MUTE.getAsString()));
            handler.setChatMuted(true);
        }
    }

    @Command(names={"slow", "slowchat"}, permission="core.chat.slow")
    public void executeSlowChat(CommandSender sender, int duration) {
        Bukkit.broadcastMessage(CC.translate(Locale.CHAT_SLOW.getAsString()));
        handler.setChatSlowDuration(duration*1000);
    }

    @Command(names={"unslow", "unslowchat"}, permission="core.chat.unslow")
    public void executeUnslowChat(CommandSender sender) {
        if(handler.getChatSlowDuration() == 0) {
            sender.sendMessage(CC.translate(Locale.CHAT_UNSLOW_ALREADY_UNSLOWED.getAsString()));
            return;
        }

        Bukkit.broadcastMessage(CC.translate(Locale.CHAT_UNSLOW.getAsString()));
        handler.setChatSlowDuration(0);
    }

    @Command(names={"staffchat", "sc"}, permission="core.staff")
    public void executeStaffChat(Player player, @Param(name="message...") String message) {
        Core.INSTANCE.sendPacket(new StaffChatPacket(player.getUniqueId(), message, Locale.SERVER_NAME.getAsString()));
    }

}
