package net.endrealm.libraries.api.lang;

public class Translation {

    private String currentTranslation = "";

    Translation(String currentTranslation) {

        this.currentTranslation = currentTranslation;
    }

    public Translation replace(String key, String replacement) {
        currentTranslation = currentTranslation.replace( "{" + key + "}", replacement);
        return this;
    }

    @Override
    public String toString() {
        return currentTranslation;
    }
}
