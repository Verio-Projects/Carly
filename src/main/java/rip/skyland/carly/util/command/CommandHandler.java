package rip.skyland.carly.util.command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;
import rip.skyland.carly.util.command.annotation.adapter.impl.*;
import rip.skyland.carly.util.command.data.CommandData;
import rip.skyland.carly.util.command.data.MethodData;
import rip.skyland.carly.util.command.data.ParamData;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class CommandHandler {

    @Getter
    private List<CommandData> commands;
    private Map<Class<?>, CommandTypeAdapter> parameterTypes;

    public CommandHandler(JavaPlugin plugin) {
        commands = new ArrayList<>();
        parameterTypes = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new CommandListener(this), plugin);

        Map.of(boolean.class, new BooleanTypeAdapter(), CC.class, new ChatColorTypeAdapter(), float.class, new FloatTypeAdapter(), double.class, new DoubleTypeAdapter(), int.class, new IntegerTypeAdapter(), OfflinePlayer.class, new OfflinePlayerTypeAdapter(), Player.class, new PlayerTypeAdapter(), World.class, new WorldTypeAdapter())
            .forEach(this::registerParameterType);
    }

    private void registerParameterType(Class<?> transforms, CommandTypeAdapter parameterType) {
        parameterTypes.put(transforms, parameterType);
    }

    public void registerCommand(Object object) {
        Arrays.stream(object.getClass().getMethods()).filter(method -> method.getAnnotation(Command.class) != null).forEach(method -> {
            Command command = method.getAnnotation(Command.class);
            List<ParamData> paramDataList = new ArrayList<>();

            IntStream.range(0, method.getParameterAnnotations().length).forEach(i ->
                Arrays.stream(method.getParameterAnnotations()[i])
                        .filter(annotation -> annotation instanceof Param)
                        .forEach(annotation -> paramDataList.add(new ParamData((Param) annotation, method.getParameterTypes()[i])))
            );

            commands.add(new CommandData(object, command.names(),  command.permission(), new MethodData(method, paramDataList), command.usage()));
            commands.sort(Comparator.comparingInt(cmd -> {

                List<String> stringList = Arrays.asList(cmd.getNames());
                stringList.sort(Comparator.comparingInt(String::length));

                return stringList.get(0).length();
            }));

            Collections.reverse(commands);
        });
    }

    public void registerCommands(Object... objects) {
        Arrays.stream(objects).forEach(this::registerCommand);
    }

    public void registerCommand(Method method, Command command) {

    }

    Object transformParameter(CommandSender sender, String parameter, Class<?> transformTo) {
        if (transformTo.equals(String.class))
            return parameter;

        return parameterTypes.get(transformTo).transform(sender, parameter);
    }

}
