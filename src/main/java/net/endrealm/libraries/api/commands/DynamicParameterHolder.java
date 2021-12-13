package net.endrealm.libraries.api.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DynamicParameterHolder extends ParameterHolder<DynamicParameterHolder> {

    private final DynamicParameter[] dynamicParameters;


    @Override
    public CommandParameter[] getParameters() {
        List<CommandParameter> parameters = new ArrayList<>();

        for(DynamicParameter dynamicParameter : dynamicParameters) {
            parameters.add(dynamicParameter.getParameter());
        }

        return parameters.toArray(new CommandParameter[0]);
    }

    @Override
    protected DynamicParameterHolder clone() {

        DynamicParameter[] clones = new DynamicParameter[dynamicParameters.length];

        for(int i = 0; i < clones.length; i++) {
            clones[i] = dynamicParameters[i].clone();
        }

        return new DynamicParameterHolder(clones);
    }

    @Data
    public static class DynamicParameter {
        private final String description;
        private final CommandParameter parameter;

        public DynamicParameter clone() {
            return new DynamicParameter(description, parameter.clone());
        }
    }
}
