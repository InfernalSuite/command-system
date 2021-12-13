package net.endrealm.libraries.api.commands.injectors;

import net.endrealm.libraries.api.commands.variables.Injector;
import net.endrealm.libraries.api.commands.variables.Variable;

import java.util.List;

public class IntegerInjector implements Injector<Integer> {
    @Override
    public Integer inject(List<Variable> vars, Class<Integer> integerClass) {
        int value = 0;
        String unparsedValue = "0";

        for(Variable variable : vars)
            if(variable.getKey().equalsIgnoreCase("value"))
                unparsedValue = variable.getValue();

        try {
            value = Integer.parseInt(unparsedValue);
        } catch (NumberFormatException ignored) { }

        return value;
    }

    @Override
    public boolean supports(Class<?> tClass) {
        return tClass == Integer.class || tClass == int.class;
    }
}
