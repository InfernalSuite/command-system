package net.endrealm.libraries.api.commands.annotations;


import net.endrealm.libraries.api.commands.variables.VariableUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Inject {

    /**
     * Supports variable definition syntax defined at
     * {@link VariableUtils#calculateValue(String, List)}
     *
     * @return keys of the variables defined in the command
     * that are used to select this resource.
     */
    String[] using() default {};
}
