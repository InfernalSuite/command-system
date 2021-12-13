package net.endrealm.libraries.api.commands;

import net.endrealm.libraries.api.commands.DynamicParameterHolder.DynamicParameter;
import net.endrealm.libraries.api.commands.annotations.*;
import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.variables.Injector;
import net.endrealm.libraries.api.commands.variables.Variable;
import net.endrealm.libraries.api.commands.variables.VariableUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationCommand {

    private final Object container;
    private final Method method;
    private final Command command;
    private final CommandRegistry commandRegistry;

    public AnnotationCommand(Object container, Method method, Command command, CommandRegistry commandRegistry) {
        this.container = container;
        this.method = method;
        this.command = command;
        this.commandRegistry = commandRegistry;
    }

    public boolean handle(CommandExecutor executor, DynamicParameterHolder parameterHolder){
        Parameter[] parameters = method.getParameters();
        Object[] paramObjects = new Object[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            //Check if current parameter takes in a sender
            if(parameter.getAnnotation(Sender.class) != null) {
                paramObjects[i] = executor;
                continue;
            }

            if(parameter.getAnnotation(Inject.class) != null) {
                paramObjects[i] = injectParam(parameter.getAnnotation(Inject.class), parameter.getType());
                continue;
            }

            //Check if current parameter takes in a sender
            if(parameter.getAnnotation(AllArgs.class) != null) {
                paramObjects[i] = parameterHolder.getAllArgs();
                continue;
            }

            paramObjects[i] = resolveParameter(parameter, parameterHolder.getDynamicParameters());
        }
        try {
            Object object = method.invoke(container, paramObjects);

            if(object instanceof Boolean)
                return (boolean) object;

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Object injectParam(Inject inject, Class<?> type) {

        List<Variable> commandVariables = VariableUtils.readVariables(command, new ArrayList<>());

        for(Injector injector : commandRegistry.getInjectors()) {
            if(!injector.supports(type))
                continue;


            return injector.inject(VariableUtils.readVariables(inject, commandVariables), type);
        }

        return null;
    }

    public DynamicParameterHolder createParameterHolder(CommandRegistry registry) {
        List<DynamicParameter> dynamicParameters = new ArrayList<>();
        List<Variable> variableList = VariableUtils.readVariables(command, new ArrayList<>());

        for(Parameter parameter : method.getParameters()) {

            if(parameter.getAnnotation(Sender.class) != null)
                continue;

            if(parameter.getAnnotation(Inject.class) != null) {
                continue;
            }

            if(parameter.getAnnotation(AllArgs.class) != null)
                continue;

            CommandParameter parameter1 = registry.getParameterByClass(parameter, variableList);
            dynamicParameters.add(new DynamicParameter(parameter.getName(), parameter1));
        }

        return new DynamicParameterHolder(dynamicParameters.toArray(new DynamicParameter[0]));
    }

    private Object resolveParameter(Parameter parameter, DynamicParameter[] dynamicParameters) {

        for(DynamicParameter dynamicParameter : dynamicParameters) {
            if(dynamicParameter.getDescription().equals(parameter.getName())) {

                CommandParameter commandParameter = dynamicParameter.getParameter();

                if(commandParameter.getValue() == null) {
                    Param param = parameter.getAnnotation(Param.class);
                    if(param != null) {

                        if(!param.defaultValue().equals("")) {
                            commandParameter.deserialize(param.defaultValue());
                        }
                    }
                }

                return dynamicParameter.getParameter().getValue();
            }
        }

        return null;
    }
}
