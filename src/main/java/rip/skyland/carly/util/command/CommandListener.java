package rip.skyland.carly.util.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.data.CommandData;
import rip.skyland.carly.util.command.data.ParamData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class CommandListener implements Listener {

    private CommandHandler handler;

    CommandListener(CommandHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(getCommandData(event.getMessage()) != null) {
            executeCommand(event.getPlayer(), this.getCommandData(event.getMessage()), event.getMessage(), event);
        }
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        if(getCommandData(event.getCommand()) != null) {
            executeCommand(event.getSender(), this.getCommandData(event.getCommand()), event.getCommand(), null);
        }
    }

    private CommandData getCommandData( String command) {
        AtomicReference<CommandData> data1 = new AtomicReference<>(null);

        this.handler.getCommands()
                .stream()
                .filter(key -> Arrays.stream(key.getNames()).filter(name -> {
                    String alias = name + " ";
                    String message = command.replace("/", "").replace(handler.getFallbackPrefix(), "") + " ";

                    return message.toLowerCase().startsWith(alias.toLowerCase());
                }).findFirst().orElse(null) != null)
                .findFirst().ifPresent(data -> {
            if(data.getMethodData().getMethod().getParameters()[0].getType().equals(Player.class))
                return;

            data1.set(data);
        });

        return data1.get();
    }

    private void executeCommand(CommandSender sender, CommandData data, String message, Event event) {
        if(event != null)
            ((Cancellable) event).setCancelled(true);

        if (!data.getPermission().equalsIgnoreCase("") && !sender.hasPermission(data.getPermission())) {
            sender.sendMessage(CC.translate("&cNo permission."));
            return;
        }

        String alias = Objects.requireNonNull(Arrays.stream(data.getNames()).filter(namez -> message.replace("/", "").toLowerCase().startsWith(namez.toLowerCase()))).findFirst().orElse(null);
        String[] args = new String[]{};
        int nameLength = Objects.requireNonNull(Arrays.stream(data.getNames()).filter(namez -> message.replace("/", "").toLowerCase().startsWith(namez.toLowerCase())).findFirst().orElse(null)).length() + 1;

        if (message.length() > nameLength) {
            args = message.substring(nameLength + (sender instanceof Player ? 1 : 0)).split(" ");
        }

        List<Object> transformedParameters = new ArrayList<>();
        List<ParamData> parameters = data.getMethodData().getParameterData();

        transformedParameters.add(sender);

        String[] finalArgs = args;
        AtomicBoolean shouldReturn = new AtomicBoolean(false);
        IntStream.range(0, parameters.size()).forEach(index -> {
            if(!shouldReturn.get()) {
                ParamData parameter = parameters.get(index);
                AtomicReference<String> passedParameter = new AtomicReference<>((index < finalArgs.length ? finalArgs[index] : parameter.getValue()).trim());

                if (index >= finalArgs.length && (parameter.getValue().isEmpty())) {
                    assert alias != null;
                    sender.sendMessage(ChatColor.RED + "Usage: " + data.getUsage(alias));
                    shouldReturn.set(true);
                    return;
                }

                if(parameters.size()-1 == index && finalArgs.length > index) {
                    IntStream.range(index+1, finalArgs.length).forEach(i -> passedParameter.set(passedParameter.get() + " " + finalArgs[i]));
                } else if (parameters.size() == transformedParameters.size() && !passedParameter.get().trim().equals(parameter.getValue().trim())) {
                    passedParameter.set((finalArgs[index] + " ").trim());
                }


                Object result = handler.transformParameter(sender, passedParameter.get(), parameter.getParameterClass());
                if (result == null) {
                    shouldReturn.set(true);
                    return;
                }

                transformedParameters.add(result);
            }
        });


        if(shouldReturn.get())
            return;

        try {
            data.getMethodData().getMethod().invoke(data.getInstance(), transformedParameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            assert alias != null;
            sender.sendMessage(ChatColor.RED + "Usage: " + data.getUsage(alias));
        }
    }

}