package net.endrealm.libraries.api.commands.interfaces;

import net.endrealm.libraries.api.lang.Language;

public interface CommandExecutor {
    boolean hasPermissions(String... permissions);
    void sendMessage(String message);
    Language getLanguage();
    boolean isPlayer();
}
