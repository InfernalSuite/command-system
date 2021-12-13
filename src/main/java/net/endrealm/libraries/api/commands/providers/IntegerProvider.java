package net.endrealm.libraries.api.commands.providers;

import net.endrealm.libraries.api.commands.annotations.IntSettings;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.parameters.IntegerParameter;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.utils.ParameterUtils;

import java.lang.reflect.Parameter;
import java.util.List;

public class IntegerProvider implements ParameterProvider {
    @Override
    public CommandParameter build(Parameter parameter, List<Variable> variableList) {
        IntegerParameter integerParameter = ParameterUtils.applySettings(parameter, new IntegerParameter());

        IntSettings settings = parameter.getAnnotation(IntSettings.class);

        if(settings != null) {
            integerParameter.setHasInnerBound(settings.hasInnerBound());
            integerParameter.setHasOuterBound(settings.hasOuterBound());
            integerParameter.setInnerBound(settings.innerBound());
            integerParameter.setOuterBound(settings.outerBound());
        }

        return integerParameter;
    }

    @Override
    public boolean accepts(Parameter parameter) {
        return parameter.getType() == Integer.class || parameter.getType() == int.class;
    }
}
