package net.endrealm.libraries.api.lang;

import java.io.File;
import java.util.HashMap;

public class LanguageManager {

    private final File directory;
    private final HashMap<Language, LangFile> languages;

    private static LanguageManager instance;

    public static LanguageManager getInstance() {
        return instance;
    }

    public LanguageManager(File directory) {
        instance = this;
        this.directory = directory;
        languages = new HashMap<>();
        initMap();
        loadLanguages();

    }

    private void initMap() {
        for (Language lang : Language.values())
            languages.put(lang, new LangFile(lang));

    }

    public String getTranslation(Language lang, String key, Replacement... replacements) {
        String text = languages.get(lang).retrieveText(key);
        if (replacements != null)
            for (Replacement replacement : replacements)
                if (replacement != null)
                    text = replacement.replace(text);

        return text;
    }

    public Translation translate(Language language, String key) {
        return new Translation(getTranslation(language, key));
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    private void loadLanguages() {
        File langFolder = directory;


        if (!langFolder.exists()) {
            langFolder.mkdir();
            System.out.println("Created language folder");
        }

        if(!langFolder.isDirectory()) {
            System.err.println("The language folder "+langFolder.getAbsolutePath()+ " is not a folder!");
            return;
        }

        System.out.println("Using "+langFolder.getAbsolutePath()+" as language folder!");

        for (File file : langFolder.listFiles()) {

            if (!file.isDirectory())
                continue;

            Language lang = Language.fromFileName(file.getName());

            if (lang != null)
                loadLangFiles(languages.get(lang), file);

        }

    }

    @SuppressWarnings("ConstantConditions")
    private void loadLangFiles(LangFile langFile, File currentDirectory) {
        for (File file : currentDirectory.listFiles()) {
            if (file.isDirectory())
                loadLangFiles(langFile, file);
            else
                langFile.load(file);
        }
    }

}
