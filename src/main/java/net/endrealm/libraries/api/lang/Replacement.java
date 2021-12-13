package net.endrealm.libraries.api.lang;

public class Replacement {
    private String replace;
    private String replacement;

    public Replacement(String replace, String replacement) {
        this.replace = "{" + replace + "}";
        this.replacement = replacement;
    }

    public Replacement(String replace, int replacement) {
        this(replace, replacement+"");
    }

    public String replace(String input) {
        return input.replace(replace, replacement);
    }

}
