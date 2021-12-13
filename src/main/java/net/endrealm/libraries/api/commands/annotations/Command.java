package net.endrealm.libraries.api.commands.annotations;


import net.endrealm.libraries.api.commands.variables.VariableUtils;

import java.lang.annotation.*;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Command.Commands.class)
public @interface Command {
    String registryName();
    String[] aliases();
    String[] permissions() default {};
    String parent() default "";
    boolean permitsMoreParams() default false;

    /**
     * Supports variable definition syntax defined at
     * {@link VariableUtils#calculateValue(String, List)}
     */
    CmdVar[] variables() default {};


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Commands {
        Command[] value();
    }
}
