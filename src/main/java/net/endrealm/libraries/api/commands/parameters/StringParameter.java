package net.endrealm.libraries.api.commands.parameters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.endrealm.libraries.api.lang.Language;
import net.endrealm.libraries.api.lang.LanguageManager;
import net.endrealm.libraries.api.lang.Replacement;

@EqualsAndHashCode(callSuper = true)
@Data
public class StringParameter extends CommandParameter<String> {

    public StringParameter() {
        this("arg", false);
    }
    public StringParameter(String name, boolean optional) {
        super(String.class, name, optional);
    }

    @Override
    public ParameterValidationResult isValid(String raw) {
        return new ParameterValidationResult(true);
    }

    @Override
    public void deserialize(String raw) {
        setValue(raw);
    }

    @Override
    public String[] values(String raw) {
        return new String[]{raw};
    }

    @Override
    public String getTranslation(LanguageManager languageManager, Language language, ParameterValidationResult parameterValidationResult) {
        return languageManager.getTranslation(language, "commands.api.parameter.string.info", new Replacement("name", getName()));
    }

    @Override
    public CommandParameter clone() {
        return new StringParameter(getName(), isOptional());
    }
}
