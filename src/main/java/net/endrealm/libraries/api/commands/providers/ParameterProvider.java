package net.endrealm.libraries.api.commands.providers;

import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.variables.Variable;

import java.lang.reflect.Parameter;
import java.util.List;

public interface ParameterProvider {

    CommandParameter build(Parameter parameter, List<Variable> variableList);
    boolean accepts(Parameter parameter);
}
