package net.endrealm.libraries.api.commands.interfaces;

import net.endrealm.libraries.api.commands.ParameterHolder;

@FunctionalInterface
public interface ParameterExecutor<T extends ParameterHolder> {
    boolean execute(CommandExecutor executor, T parameterHolder);
}
