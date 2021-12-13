package core.api.command.libs;

import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;
import net.endrealm.libraries.api.lang.Language;

public class LoggerCommandExecutor implements CommandExecutor {
    private final boolean privileged;

    public LoggerCommandExecutor(boolean privileged) {

        this.privileged = privileged;
    }

    @Override
    public boolean hasPermissions(String... permissions) {
        if(permissions.length == 0) return true;
        System.out.println("Checking for permissions: " + String.join(", ", permissions));

        return privileged;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
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
