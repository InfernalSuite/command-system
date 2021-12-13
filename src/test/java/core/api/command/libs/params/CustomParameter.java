package core.api.command.libs.params;

import core.api.command.libs.CustomClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.endrealm.libraries.api.commands.parameters.CommandParameter;
import net.endrealm.libraries.api.commands.parameters.ParameterValidationResult;
import net.endrealm.libraries.api.lang.Language;
import net.endrealm.libraries.api.lang.LanguageManager;
import net.endrealm.libraries.api.lang.Replacement;

import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class CustomParameter extends CommandParameter<CustomClass> {

    public CustomParameter() {
        this("arg", false);
    }
    public CustomParameter(String name, boolean optional) {
        super(CustomClass.class, name, optional);
    }

    @Override
    public ParameterValidationResult isValid(String raw) {

        try {
            CustomClass.valueOf(raw);
            return new ParameterValidationResult(true);
        } catch (Exception e) {
            return new ParameterValidationResult(false);
        }
    }

    @Override
    public void deserialize(String raw) {
        setValue(CustomClass.valueOf(raw));
    }

    @Override
    public String[] values(String raw) {
        return Arrays.stream(CustomClass.values()).map(Enum::toString).filter(s -> s.startsWith(raw)).toArray(String[]::new);
    }

    @Override
    public String getTranslation(LanguageManager languageManager, Language language, ParameterValidationResult parameterValidationResult) {
        return languageManager.getTranslation(language, "commands.api.parameter.custom.info", new Replacement("name", getName()));
    }

    @Override
    public CommandParameter clone() {
        return new CustomParameter(getName(), isOptional());
    }
}
