package net.endrealm.libraries.api.commands.injectors;

import net.endrealm.libraries.api.commands.variables.Injector;
import net.endrealm.libraries.api.commands.variables.Variable;

import java.util.List;

public class StringInjector implements Injector<String> {
    @Override
    public String inject(List<Variable> vars, Class<String> stringClass) {
        String value = "";

        for(Variable variable : vars)
            if(variable.getKey().equalsIgnoreCase("value"))
                value = variable.getValue();

        return value;
    }

    @Override
    public boolean supports(Class<?> tClass) {
        return tClass == String.class;
    }
}
