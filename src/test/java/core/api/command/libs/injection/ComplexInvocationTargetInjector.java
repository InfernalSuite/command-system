package core.api.command.libs.injection;

import net.endrealm.libraries.api.commands.variables.Injector;
import net.endrealm.libraries.api.commands.variables.Variable;

import java.util.List;

public class ComplexInvocationTargetInjector implements Injector<ComplexInvocationTarget> {
    private final ComplexInvocationTarget complexInvocationTarget;

    public ComplexInvocationTargetInjector(ComplexInvocationTarget complexInvocationTarget) {

        this.complexInvocationTarget = complexInvocationTarget;
    }

    @Override
    public ComplexInvocationTarget inject(List<Variable> vars, Class<ComplexInvocationTarget> complexInvocationTargetClass) {
        return complexInvocationTarget;
    }

    @Override
    public boolean supports(Class<?> tClass) {
        return ComplexInvocationTarget.class == tClass;
    }
}
