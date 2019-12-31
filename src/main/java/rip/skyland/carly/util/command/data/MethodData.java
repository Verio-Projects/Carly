package rip.skyland.carly.util.command.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@AllArgsConstructor
public class MethodData {

    private Method method;
    private List<ParamData> parameterData;

}