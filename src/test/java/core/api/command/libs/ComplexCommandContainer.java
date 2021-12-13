package core.api.command.libs;

import core.api.command.libs.injection.ComplexInvocationTarget;
import net.endrealm.libraries.api.commands.CommandContainer;
import net.endrealm.libraries.api.commands.annotations.CmdVar;
import net.endrealm.libraries.api.commands.annotations.Command;
import net.endrealm.libraries.api.commands.annotations.Inject;

public class ComplexCommandContainer implements CommandContainer {

    @Command(registryName = "command1", aliases = "command1", variables = @CmdVar(key = "number", val = "1"))
    @Command(registryName = "command2", aliases = "command2", variables = @CmdVar(key = "number", val = "2"))
    public boolean command1(

            @Inject ComplexInvocationTarget complexInvocationTarget,
            @Inject(using = "value:==$number") Integer commandNr
    ) {
        complexInvocationTarget.add(commandNr);
        return true;
    }


    public int command1Called;
    public int command2Called;

}
