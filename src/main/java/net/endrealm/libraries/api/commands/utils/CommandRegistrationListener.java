package net.endrealm.libraries.api.commands.utils;

import net.endrealm.libraries.api.commands.CommandBase;

@FunctionalInterface
public interface CommandRegistrationListener {
    void register(CommandBase command);
}
