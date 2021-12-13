package net.endrealm.libraries.utils;

import net.endrealm.libraries.api.commands.annotations.Param;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.api.commands.variables.VariableUtils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterUtils {
    public static <T extends CommandParameter> T applySettings(Parameter parameter, T commandParameter) {

        commandParameter.setName(parameter.getName());
        Param param = parameter.getAnnotation(Param.class);

        if(param != null) {
            commandParameter.setOptional(param.optional());
        }

        return commandParameter;
    }

    public static List<Variable> getVariables(Parameter parameter, List<Variable> variables) {
        List<Variable> variableList = new ArrayList<>();
        Param param = parameter.getAnnotation(Param.class);

        if(param != null) {
            variableList = VariableUtils.readVariables(param, variables);
        }

        return variableList;
    }
}
