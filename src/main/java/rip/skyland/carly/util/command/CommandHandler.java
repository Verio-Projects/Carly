package rip.skyland.carly.util.command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.JavaUtils;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;
import rip.skyland.carly.util.command.annotation.adapter.impl.*;
import rip.skyland.carly.util.command.data.CommandData;
import rip.skyland.carly.util.command.data.MethodData;
import rip.skyland.carly.util.command.data.ParamData;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.IntStream;

@Getter
public class CommandHandler {

    private List<CommandData> commands;
    private Map<Class<?>, CommandTypeAdapter> parameterTypes;
    private String fallbackPrefix;

    public CommandHandler(JavaPlugin plugin, String fallbackPrefix) {
        commands = new ArrayList<>();
        parameterTypes = new HashMap<>();

        this.fallbackPrefix = fallbackPrefix;

        Bukkit.getPluginManager().registerEvents(new CommandListener(this), plugin);

        JavaUtils.mapOf(boolean.class, new BooleanTypeAdapter(), CC.class, new ChatColorTypeAdapter(), float.class, new FloatTypeAdapter(), double.class, new DoubleTypeAdapter(), int.class, new IntegerTypeAdapter(), OfflinePlayer.class, new OfflinePlayerTypeAdapter(), Player.class, new PlayerTypeAdapter(), Profile.class, new ProfileTypeAdapter(), Material.class, new MaterialTypeAdapter()).forEach(this::registerParameterType);
    }

    private void registerParameterType(Class<?> transforms, CommandTypeAdapter parameterType) {
        parameterTypes.put(transforms, parameterType);
    }

    private void registerCommand(Object object) {
        Arrays.stream(object.getClass().getMethods()).filter(method -> method.getAnnotation(Command.class) != null).forEach(method -> {
            Command command = method.getAnnotation(Command.class);
            List<ParamData> paramDataList = new ArrayList<>();

            IntStream.range(1, method.getParameters().length).forEach(i -> {
                Parameter parameter = method.getParameters()[i];
                if (parameter.getAnnotation(Param.class) != null) {
                    paramDataList.add(new ParamData(parameter.getAnnotation(Param.class), parameter.getAnnotation(Param.class).name(), method.getParameterTypes()[i]));
                } else {
                    paramDataList.add(new ParamData(null, parameter.getName(), method.getParameterTypes()[i]));
                }
            });

            commands.add(new CommandData(object, command.names(), command.permission(), new MethodData(method, paramDataList), command.usage()));
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

    Object transformParameter(CommandSender sender, String parameter, Class<?> transformTo) {
        if (transformTo.equals(String.class))
            return parameter;

        return parameterTypes.get(transformTo).transform(sender, parameter);
    }

}
