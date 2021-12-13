package net.endrealm.libraries.api.commands.variables;


import java.util.List;

public interface Injector<T> {
    T inject(List<Variable> vars, Class<T> tClass);
    boolean supports(Class<?> tClass);
}
