package net.endrealm.libraries.api.commands;

import lombok.Getter;
import lombok.ToString;
import net.endrealm.libraries.api.commands.interfaces.ParameterExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public final class CommandBase<T extends ParameterHolder> {

    private final String name;
    private final CommandFactory commandFactory;
    private final T parameterHolder;
    private final List<String> aliases;
    private final List<String> permissions;

    private ParameterExecutor<T> parameterExecutor;
    private String parentCommand;
    private boolean permitsMoreParams;

    public CommandBase(String name, CommandFactory commandFactory, T parameterHolder) {
        this.name = name;
        this.commandFactory = commandFactory;
        this.parameterHolder = parameterHolder;
        this.aliases = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }

    public CommandBase<T> addAlias(String... aliases) {

        if(aliases == null || aliases.length == 0)
            return this;

        this.aliases.addAll(new ArrayList<>(Arrays.asList(aliases)));
        return this;
    }

    public CommandBase<T> addPerms(String... permissions) {

        if(permissions == null || permissions.length == 0)
            return this;

        this.permissions.addAll(new ArrayList<>(Arrays.asList(permissions)));
        return this;
    }

    public CommandBase<T> setExecutor(ParameterExecutor<T> parameterExecutor) {
        this.parameterExecutor = parameterExecutor;
        return this;
    }

    public CommandBase<T> setParentCommand(String parentCommand) {
        this.parentCommand = parentCommand;
        return this;
    }

    public CommandBase<T> setPermitsMoreParams(boolean permitsMoreParams) {
        this.permitsMoreParams = permitsMoreParams;
        return this;
    }

    public CommandFactory register() {
        if(getAliases().length == 0)
            throw new RuntimeException("You need to add at least one alias. Name is the registryName not the command name");

        commandFactory.register(this);
        return commandFactory;
    }

    public String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

    public String[] getPermissions() {
        return permissions.toArray(new String[0]);
    }

    public boolean hasAlias(String alias) {
        for(String ali : aliases)
            if(ali.equals(alias))
                return true;
        return false;
    }

    public List<String> getAliasesRaw() {
        return aliases;
    }

    public List<String> getPermissionsRaw() {
        return permissions;
    }


}
