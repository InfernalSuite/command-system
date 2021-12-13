package net.endrealm.libraries.api.commands.parameters;

import lombok.Data;
import net.endrealm.libraries.api.lang.Language;
import net.endrealm.libraries.api.lang.LanguageManager;

@Data
public abstract class CommandParameter<T> {

    private final Class<T> tClass;
    private T value;
    private String name = "";
    private boolean optional;

    protected CommandParameter(Class<T> tClass, String name, boolean optional) {
        this.tClass = tClass;
        this.name = name;
        this.optional = optional;
    }

    public abstract ParameterValidationResult isValid(String raw);
    public abstract void deserialize(String raw);
    public T getValue() {
        return value;
    }
    public abstract String[] values(String raw);

    public final Class<T> getTypeClass() {
        return tClass;
    }

    public abstract String getTranslation(LanguageManager languageManager, Language language, ParameterValidationResult parameterValidationResult);

    public abstract CommandParameter clone();
}
