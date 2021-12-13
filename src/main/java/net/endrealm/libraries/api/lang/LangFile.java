package net.endrealm.libraries.api.lang;

import net.endrealm.libraries.utils.ChatColor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class LangFile {
    protected Language lang;

    private HashMap<String, String> translations;

    public LangFile(Language lang) {
        this.lang = lang;
        translations = new HashMap<>();
    }

    public String retrieveText(String key) {
        if (translations.containsKey(key))
            return translations.get(key);
        return key;
    }

    public void load(File languageFile) {
        File langFile = languageFile;

        // Languagefile found
        if (langFile.exists()) {
            System.out.println("Loading lang: " + langFile.getPath());
            try {
                List<String> lines = Files.readAllLines(langFile.toPath(), StandardCharsets.ISO_8859_1);

                for (String line : lines) {
                    try {
                        String[] text = line.split("=");
                        if (text.length >= 2) {

                            String fullText = line.substring(text[0].length() + 1);

                            translations.put(text[0], ChatColor.translateAlternateColorCodes('&', fullText));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.err.println("Failed loading lang: " + langFile.getPath());
    }

}
