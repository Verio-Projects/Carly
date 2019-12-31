package rip.skyland.carly.util.command.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandData {

    Object instance;
    String[] names;
    String permission;
    private MethodData methodData;
    private String usage;

    public String getUsage(String alias) {
        if(!usage.equals(""))
            return "/" + alias.trim().toLowerCase() + " " + usage;

        StringBuilder builder = new StringBuilder();

        methodData.getParameterData().forEach(data -> {
            boolean required = data.getValue().isEmpty();
            builder.append(required ? "<" : "[").append(data.getParam().name()).append(required ? ">" : "]").append(" ");
        });

        return "/" + alias.trim().toLowerCase()  + " " + builder.toString().trim().toLowerCase();
    }
}
