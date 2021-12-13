package net.endrealm.libraries.api.commands;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.parameters.ParameterValidationResult;
import net.endrealm.libraries.api.commands.utils.CommandRegistrationListener;
import net.endrealm.libraries.api.commands.utils.GodPlayer;
import net.endrealm.libraries.api.lang.Language;
import net.endrealm.libraries.api.lang.LanguageManager;
import net.endrealm.libraries.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Getter(AccessLevel.PRIVATE)
public final class CommandManager {

    private final List<CommandWrapper> commandList;
    private final List<CommandBase> commandQueue;
    private final List<CommandRegistrationListener> commandRegistrationListeners;
    private final LanguageManager languageManager;

    public CommandManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
        this.commandList = new ArrayList<>();
        this.commandQueue = new ArrayList<>();
        this.commandRegistrationListeners = new ArrayList<>();
    }

    void register(CommandBase commandBase) {
        if(commandBase.getParentCommand() == null) {
            this.commandList.add(new CommandWrapper(commandBase));
            for(CommandRegistrationListener commandRegistrationListener : commandRegistrationListeners) {
                commandRegistrationListener.register(commandBase);
            }
        } else
            this.commandQueue.add(commandBase);
        runQueue();
    }

    private void runQueue() {
        while(true) {
            if (!processQueue()) break;
        }
    }

    private CommandWrapper deepSearch(String targetName) {

        for(CommandWrapper command : commandList) {
            CommandWrapper target = deepSearch(command, targetName);
            if(target != null)
                return target;
        }

        return null;
    }

    private CommandWrapper deepSearch(CommandWrapper wrapper, String targetName) {
        if(wrapper.getCommandBase().getName().equals(targetName))
            return wrapper;

        for(CommandWrapper child : wrapper.getChildren()) {
            CommandWrapper target = deepSearch(child, targetName);
            if(target != null)
                return target;
        }

        return null;
    }

    private boolean processQueue() {
        boolean edited = false;

        Iterator<CommandBase> iterator = commandQueue.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            CommandBase commandBase = iterator.next();
            CommandWrapper parent = deepSearch(commandBase.getParentCommand());

            if(parent == null)
                continue;

            edited = true;
            parent.addChild(new CommandWrapper(commandBase));
            commandQueue.remove(commandBase);

            break;
        }

        return edited;
    }

    public List<CommandWrapper> getCommandChain(String[] args) {
        return getCommandChain(new GodPlayer(), args);
    }

    public List<CommandWrapper> getCommandChain(CommandExecutor executor,String[] args) {
        List<List<CommandWrapper>> paths = new ArrayList<>();

        for(CommandWrapper wrapper : commandList) {
            List<List<CommandWrapper>> commandLists = wrapper.findPaths(executor, args);
            if(commandLists.size() == 0)
                continue;
            paths.addAll(commandLists);
        }

        List<CommandWrapper> bestCommand = null;

        for(List<CommandWrapper> wrapperList : paths) {
            if(bestCommand == null || bestCommand.size() < wrapperList.size()) {
                bestCommand = wrapperList;
            }
        }

        return bestCommand;
    }

    @SuppressWarnings("unchecked")
    public void execute(CommandExecutor executor, String[] fullArgs) {
        List<CommandWrapper> commandChain = getCommandChain(fullArgs);

        if(commandChain == null) {
            executor.sendMessage(languageManager.getTranslation(executor.getLanguage(), "commands.api.error.not_found"));
            return;
        }

        for(CommandWrapper wrapper : commandChain) {
            if(!executor.hasPermissions(wrapper.getCommandBase().getPermissions())) {
                executor.sendMessage(languageManager.getTranslation(executor.getLanguage(), "commands.api.error.not_authorized"));
                return;
            }
        }

        // execute
        String[] args = ArrayUtils.preReduceArray(fullArgs, commandChain.size());
        CommandWrapper commandWrapper = commandChain.get(commandChain.size()-1);
        CommandBase command = commandWrapper.getCommandBase();
        ParameterHolder parameterHolder = command.getParameterHolder().clone();
        CommandParameter[] parameters = parameterHolder.getParameters();
        int paramMinLength = 0;

        for(CommandParameter parameter : parameters)
            if(!parameter.isOptional())
                paramMinLength++;

        if(args.length < paramMinLength || (!command.isPermitsMoreParams() && args.length > parameters.length)) {
            sendCommandHelp(executor, commandChain, parameters);
            return;
        }

        for(int i = 0; i < args.length &&  i < parameters.length; i++) {
            String argument = args[i];

            CommandParameter parameter = parameters[i];

            ParameterValidationResult result = parameter.isValid(argument);

            if(!result.isSuccessful()) {
                executor.sendMessage("§c"+parameter.getTranslation(languageManager, executor.getLanguage(), result));
                return;
            }

            parameter.deserialize(argument);
        }

        parameterHolder.setAllArgs(args);
        boolean success = command.getParameterExecutor().execute(executor, parameterHolder);

        if(!success) {
            sendCommandHelp(executor, commandChain, parameters);
        }
    }

    //TODO: add descriptions and better formats
    private void sendCommandHelp(CommandExecutor executor, List<CommandWrapper> commandChain, CommandParameter[] parameters) {

        executor.sendMessage("§c"+buildCommand(commandChain, parameters, languageManager, executor.getLanguage()));

        CommandWrapper cmd = commandChain.get(commandChain.size() - 1);

        var children = cmd.getChildren().stream()
                .filter(commandWrapper -> executor.hasPermissions(commandWrapper.getCommandBase().getPermissions()))
                .collect(toList());
        if (children.size() <= 0) {
            return;
        }
        executor.sendMessage(languageManager.getTranslation(executor.getLanguage(), "commands.api.sub_commands"));
        children.stream().map(child -> "  §6\u2ba1 §a" + child.getCommandBase().getAliases()[0]).forEach(executor::sendMessage);
    }

    private String buildCommand(List<CommandWrapper> commandChain, CommandParameter[] parameters, LanguageManager languageManager, Language language) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("/");
        for(CommandWrapper commandWrapper : commandChain) {
            stringBuilder.append(commandWrapper.getCommandBase().getAliases()[0]);
            stringBuilder.append(" ");
        }
        for(CommandParameter parameter : parameters) {
            if(parameter.isOptional())
                stringBuilder.append("[");
            stringBuilder.append(parameter.getTranslation(languageManager, language, null));
            if(parameter.isOptional())
                stringBuilder.append("]");
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    public List<String> getCompletions(CommandExecutor executor, String[] args) {
        List<CommandWrapper> commandChain = getCommandChain(executor, args);
        List<String> completions = new ArrayList<>();

        String startWith = args.length > 0 ? args[args.length-1] : "";

        List<CommandWrapper> loopTarget = null;

        if(commandChain == null) {
            loopTarget = commandList;
        } else {
            CommandWrapper lastCommand = commandChain.get(commandChain.size()-1);
            if(lastCommand.getCommandBase().hasAlias(startWith)) {
                loopTarget = lastCommand.getChildren();
                startWith = "";
            } else if(args.length > 1 && lastCommand.getCommandBase().hasAlias(args[args.length-2])) {
                loopTarget = lastCommand.getChildren();
            } else {
                loopTarget = new ArrayList<>();
            }
        }

        for(CommandWrapper wrapper : loopTarget) {
            if(executor.hasPermissions((String[]) wrapper.getCommandBase().getPermissions())) {
                for (String alias : wrapper.getCommandBase().getAliases()) {
                    if(alias.startsWith(startWith))
                        completions.add(alias);
                }
            }
        }

        final int MAX_COMPLETIONS = 20;

        // Following add parameter completions
        // Comments follow example:
        // command: arg0 arg1 param0 param1 => command chain size == 2 and args.length == 4
        outerIf:
        if(commandChain != null && completions.size() < MAX_COMPLETIONS) {

            int argIndex = commandChain.size(); // => command chain size == 2
            int paramArgIndex = args.length - argIndex; // 4-2 => paramArgIndex == 2

            if(paramArgIndex <= 0) // Check if we are accessing a parameter or if we are still tabbing the command
                break outerIf;

            paramArgIndex--; // shift index left

            CommandWrapper lastCommand = commandChain.get(commandChain.size()-1); // Select last sub command
            ParameterHolder parameterHolder = lastCommand.getCommandBase().getParameterHolder(); // Grab parameter holder
            CommandParameter[] parameters = parameterHolder.getParameters(); //Grab list of parameters

            if(parameters.length <= paramArgIndex) // Check if parameter index is in bounds of parameter list
                break outerIf;

            CommandParameter parameter = parameters[paramArgIndex]; // Select parameter
            String[] values = parameter.values(args[args.length-1]); // Get autocompletion recommendations

            for(int i = 0; i < values.length && completions.size() < MAX_COMPLETIONS; i++) { // Add completions until max size is reached
                completions.add(values[i]);
            }
        }

        return completions;
    }

    public void addRegistrationListener(CommandRegistrationListener commandRegistrationListener) {
        this.commandRegistrationListeners.add(commandRegistrationListener);
    }

    public boolean isLowLevelCommand(CommandExecutor executor, String command) {
        for(CommandWrapper wrapper : commandList) {

            if(executor.hasPermissions(wrapper.getCommandBase().getPermissions())) {
                for (String alias : wrapper.getCommandBase().getAliases()) {
                    if(alias.equals(command))
                        return true;
                }
            }
        }

        return false;
    }

    public List<CommandWrapper> getBaseCommands(CommandExecutor executor) {
        List<CommandWrapper> baseCommands = new ArrayList<>();

        for(CommandWrapper wrapper : commandList) {
            if(executor.hasPermissions(wrapper.getCommandBase().getPermissions()))
                baseCommands.add(wrapper);
        }

        return baseCommands;
    }
}
