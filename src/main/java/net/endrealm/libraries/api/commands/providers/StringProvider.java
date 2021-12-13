package net.endrealm.libraries.api.commands.providers;

import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.parameters.StringParameter;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.utils.ParameterUtils;

import java.lang.reflect.Parameter;
import java.util.List;

public class StringProvider implements ParameterProvider {
    @Override
    public CommandParameter build(Parameter parameter, List<Variable> variableList) {
        return ParameterUtils.applySettings(parameter, new StringParameter());
    }

    @Override
    public boolean accepts(Parameter parameter) {
        return parameter.getType() == String.class;
    }
}
