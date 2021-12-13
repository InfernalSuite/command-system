package net.endrealm.libraries.api.commands;

import lombok.Data;

@Data
public class DefaultCommand {
    private final String[] aliases;
    private final String permission;

    public boolean matches(String message) {
        for(String alias : aliases) {
            if(message.startsWith(alias+" ") || message.equals(alias))
                return true;
        }
        return false;
    }
}
