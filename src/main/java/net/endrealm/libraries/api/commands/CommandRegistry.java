package net.endrealm.libraries.api.commands;

import lombok.Data;
import net.endrealm.libraries.api.commands.annotations.Command;
import net.endrealm.libraries.api.commands.annotations.Param;
import net.endrealm.libraries.api.commands.injectors.IntegerInjector;
import net.endrealm.libraries.api.commands.injectors.StringInjector;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.providers.IntegerProvider;
import net.endrealm.libraries.api.commands.providers.ParameterProvider;
import net.endrealm.libraries.api.commands.providers.StringProvider;
import net.endrealm.libraries.api.commands.variables.Injector;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.api.commands.variables.VariableUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Data
public final class CommandRegistry {

    private final List<AnnotationCommand> commands;
    private final List<ParameterProvider> parameterProviders;
    private final List<DefaultCommand> defaultCommands;
    private final List<Injector> injectors;
    private final CommandFactory commandFactory;

    public CommandRegistry(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        this.parameterProviders = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.injectors = new ArrayList<>();
        this.defaultCommands = new ArrayList<>();

        addParameterResolver(new StringProvider());
        addParameterResolver(new IntegerProvider());

        addInjector(new StringInjector());
        addInjector(new IntegerInjector());
    }

    public void register(CommandContainer commandContainer) {
        for(Method method : commandContainer.getClass().getMethods()) { //TODO: get inherited methods
            register(method, commandContainer);
        }
    }

    public void addInjector(Injector injector) {
        injectors.add(injector);
    }

    private void register(Method method, Object commandContainer) {
        Command[] commands = method.getAnnotationsByType(Command.class);

        for(Command command : commands) {
            register(method, commandContainer, command);
        }
    }

    private void register(Method method, Object commandContainer, Command command) {

        AnnotationCommand annotationCommand = new AnnotationCommand(commandContainer, method, command, this);

        commandFactory.createCommand(command.registryName(), annotationCommand.createParameterHolder(this))
                .addAlias(command.aliases())
                .addPerms(command.permissions())
                .setParentCommand(command.parent().equals("") ? null : command.parent())
                .setPermitsMoreParams(command.permitsMoreParams())
                .setExecutor(annotationCommand::handle)
                .register();
        this.commands.add(annotationCommand);
    }

    public CommandParameter getParameterByClass(Parameter parameter, List<Variable> variableList) {

        variableList = new ArrayList<>(variableList);
        Param param = parameter.getAnnotation(Param.class);
        if (param != null) {
            variableList = VariableUtils.readVariables(param, variableList);
        }

        //Find the nearest solution use a subclass parameter
        for(ParameterProvider entry : parameterProviders) {
            //noinspection unchecked
            if(entry.accepts(parameter))
                return entry.build(parameter, variableList);
        }

        throw new RuntimeException("Class "+parameter.getType().getName() + " does not have a mapping parameter!");
    }

    public void whitelistDefaultCommand(String permission, String... aliases) {
        if(aliases == null || aliases.length == 0)
            return;
        defaultCommands.add(new DefaultCommand(aliases, permission));
    }


    public void addParameterResolver(ParameterProvider parameterProvider) {
        parameterProviders.add(parameterProvider);
    }
}
