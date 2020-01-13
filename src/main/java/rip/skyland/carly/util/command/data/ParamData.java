package rip.skyland.carly.util.command.data;

import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.util.command.annotation.Param;

@Getter
@Setter
public class ParamData {

    private Param param;
    private String name;
    private String value;
    private Class<?> parameterClass;

    public ParamData(Param annotation, String name, Class<?> parameterClass) {
        this.param = annotation;
        this.name = name;
        this.value = annotation == null || annotation.value().isEmpty() ? "" : annotation.value();
        this.parameterClass = parameterClass;
    }
}
