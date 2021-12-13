package net.endrealm.libraries.api.commands;

import lombok.Data;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;

@Data
public abstract class ParameterHolder<T extends ParameterHolder> {
    private String[] allArgs;
    public abstract CommandParameter[] getParameters();
    protected abstract T clone();
}
