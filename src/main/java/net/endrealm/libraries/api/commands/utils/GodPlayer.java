package net.endrealm.libraries.api.commands.utils;

import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;
import net.endrealm.libraries.api.lang.Language;

public class GodPlayer implements CommandExecutor {
    @Override
    public boolean hasPermissions(String... permissions) {
        return true;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Language getLanguage() {
        return Language.ENGLISH_US;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}
