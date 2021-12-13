package net.endrealm.libraries.api.commands;

public final class CommandFactory {

    private final CommandManager commandManager;

    public CommandFactory(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public <T extends ParameterHolder> CommandBase<T> createCommand(String name, T parameterHolder) {
        return new CommandBase<>(name, this, parameterHolder);
    }

    public void register(CommandBase commandBase) {
        this.commandManager.register(commandBase);
    }
}
