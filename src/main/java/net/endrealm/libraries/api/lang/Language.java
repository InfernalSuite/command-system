package net.endrealm.libraries.api.lang;

public enum Language {
    GERMAN("de_DE", "Deutsch"), ENGLISH_US("en_US", "English");
    private String fileName;
    private String displayName;

    Language(String lang, String displayName) {
        this.fileName = lang;
        this.displayName = displayName;
    }

    public static Language getLanguageFromString(String lang) {

        for (Language language : values())
            if (language.getFileName().startsWith(lang))
                return language;

        return ENGLISH_US;
    }

    public static Language fromFileName(String name) {
        for (Language language : values())
            if (name.startsWith(language.getFileName()))
                return language;
        return null;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
