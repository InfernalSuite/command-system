package net.endrealm.libraries.api.commands;

import lombok.Data;
import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;
import net.endrealm.libraries.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public final class CommandWrapper {

    private final CommandBase commandBase;
    private final List<CommandWrapper> children;

    public CommandWrapper(CommandBase commandBase) {
        this.commandBase = commandBase;
        this.children = new ArrayList<>();
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public void addChild(CommandWrapper child) {
        this.children.add(child);
    }

    public List<List<CommandWrapper>> findPaths(CommandExecutor executor, String[] args) {
        if(args.length == 0 || !hasLabel(args[0]) || !executor.hasPermissions(commandBase.getPermissions()))
            return new ArrayList<>();
        List<List<CommandWrapper>> paths = new ArrayList<>();

        paths.add(new ArrayList<>(Collections.singletonList(this)));

        String[] redArgs = ArrayUtils.preReduceArray(args);
        for(CommandWrapper child : children) {
            List<List<CommandWrapper>> childList = child.findPaths(executor, redArgs);

            if(childList.size() == 0)
                continue;
            for(List<CommandWrapper> wrapperList : childList) {
                wrapperList.add(0, this);
            }

            paths.addAll(childList);
        }

        return paths;
    }

    private boolean hasLabel(String label) {
        return commandBase.getAliasesRaw().contains(label);
    }
}
