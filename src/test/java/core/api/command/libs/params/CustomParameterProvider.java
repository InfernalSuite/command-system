package core.api.command.libs.params;

import core.api.command.libs.CustomClass;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.providers.ParameterProvider;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.utils.ParameterUtils;
import java.lang.reflect.Parameter;
import java.util.List;

public class CustomParameterProvider implements ParameterProvider {
    @Override
    public CommandParameter build(Parameter parameter, List<Variable> variableList) {
        return ParameterUtils.applySettings(parameter, new CustomParameter());
    }

    @Override
    public boolean accepts(Parameter parameter) {
        return parameter.getType() == CustomClass.class;
    }
}
