package net.endrealm.libraries.api.commands.parameters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.endrealm.libraries.api.lang.Language;
import net.endrealm.libraries.api.lang.LanguageManager;
import net.endrealm.libraries.api.lang.Replacement;

@EqualsAndHashCode(callSuper = true)
@Data
public class IntegerParameter extends CommandParameter<Integer> {

    private boolean hasInnerBound;
    private boolean hasOuterBound;
    private int innerBound;
    private int outerBound;

    public IntegerParameter() {
        this(false, false, 0, 0, "arg", false);
    }

    public IntegerParameter(boolean hasInnerBound, boolean hasOuterBound, int innerBound, int outerBound, String parameterName, boolean optional) {
        super(Integer.class, parameterName, optional);
        this.hasInnerBound = hasInnerBound;
        this.hasOuterBound = hasOuterBound;
        this.innerBound = innerBound;
        this.outerBound = outerBound;
    }

    @Override
    public ParameterValidationResult isValid(String raw) {
        try {
            int number = Integer.parseInt(raw);

            if(hasInnerBound && number < innerBound)
                return new IntegerValidationResult(false, true, false, true);
            if(hasOuterBound && number > outerBound)
                return new IntegerValidationResult(false, true, true, false);

            return new IntegerValidationResult(true, true, false, false);
        } catch (NumberFormatException ex) {
            //Invalid number
            return new IntegerValidationResult(false, false, false, false);
        }
    }

    @Override
    public void deserialize(String raw) {
        setValue(Integer.parseInt(raw));
    }

    @Override
    public String[] values(String raw) {
        return new String[]{raw};
    }

    @Override
    public String getTranslation(LanguageManager languageManager, Language language, ParameterValidationResult parameterValidationResult) {

        if(parameterValidationResult == null) {
            if(hasInnerBound && hasOuterBound)
                return languageManager.getTranslation(language, "commands.api.parameter.integer.info_full",
                        new Replacement("name", getName()),
                        new Replacement("inner", innerBound),
                        new Replacement("outer", outerBound));
            if(hasInnerBound)
                return languageManager.getTranslation(language, "commands.api.parameter.integer.info_inner",
                        new Replacement("name", getName()),
                        new Replacement("inner", innerBound));
            if(hasOuterBound)
                return languageManager.getTranslation(language, "commands.api.parameter.integer.info_outer",
                        new Replacement("name", getName()),
                        new Replacement("outer", outerBound));

            return languageManager.getTranslation(language, "commands.api.parameter.integer.info",
                    new Replacement("name", getName()));
        }

        if(!(parameterValidationResult instanceof IntegerValidationResult))
            throw new RuntimeException("Invalid ParameterValidationResult passed to Integer parameter");
        IntegerValidationResult result = (IntegerValidationResult) parameterValidationResult;

        if(!result.isValidInt())
            return languageManager.getTranslation(language, "commands.api.parameter.integer.invalid");
        if(result.isViolatedInnerBound())
            return languageManager.getTranslation(language, "commands.api.parameter.integer.violated_inner", new Replacement("inner", innerBound));
        if(result.isViolatedOuterBound())
            return languageManager.getTranslation(language, "commands.api.parameter.integer.violated_outer", new Replacement("outer", outerBound));
        return null;
    }

    @Override
    public CommandParameter clone() {
        return new IntegerParameter(hasInnerBound, hasOuterBound, innerBound, outerBound, getName(), isOptional());
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private class IntegerValidationResult extends ParameterValidationResult {

        private final boolean validInt;
        private final boolean violatedOuterBound;
        private final boolean violatedInnerBound;

        public IntegerValidationResult(boolean successful, boolean validInt, boolean violatedOuterBound, boolean violatedInnerBound) {
            super(successful);
            this.validInt = validInt;
            this.violatedOuterBound = violatedOuterBound;
            this.violatedInnerBound = violatedInnerBound;
        }
    }
}
