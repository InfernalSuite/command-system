package core.api.command.libs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.endrealm.libraries.api.commands.ParameterHolder;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.parameters.IntegerParameter;
import net.endrealm.libraries.api.commands.parameters.StringParameter;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestParameterHolder extends ParameterHolder<TestParameterHolder> {

    private StringParameter arg0 = new StringParameter();
    private IntegerParameter arg1 = new IntegerParameter();


    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] {
                arg0,
                arg1
        };
    }

    @Override
    protected TestParameterHolder clone() {
        return new TestParameterHolder();
    }
}
