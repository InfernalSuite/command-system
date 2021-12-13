package core.api.command.libs;

import net.endrealm.libraries.api.commands.CommandContainer;
import net.endrealm.libraries.api.commands.annotations.Command;
import net.endrealm.libraries.api.commands.annotations.IntSettings;
import net.endrealm.libraries.api.commands.annotations.Sender;
import net.endrealm.libraries.api.commands.interfaces.CommandExecutor;

public class SimpleCommandContainer implements CommandContainer {

    @Command(registryName = "command1", aliases = "command1")
    public boolean command1(

            String param1,

            @IntSettings(hasOuterBound = true, outerBound = 30)
            int param2
    ) {
        command1Called++;
        return true;
    }

    @Command(registryName = "command2", aliases = "command2", permissions = "command2", parent = "command1")
    public boolean command2(

            String param1,

            @IntSettings(hasOuterBound = true, outerBound = 30)
                    int param2
    ) {
        command2Called++;
        return true;
    }

    public int command1Called;
    public int command2Called;
    public int command4Called;

    @Command(registryName = "command3", aliases = "command3", permissions = "command3")
    public boolean command3(
            int param1,
            CustomClass param2
    ) {
        return true;
    }

    @Command(registryName = "command4", aliases = "command4")
    public boolean command4(@Sender CommandExecutor sender) {
        command4Called++;
        return true;
    }
}
