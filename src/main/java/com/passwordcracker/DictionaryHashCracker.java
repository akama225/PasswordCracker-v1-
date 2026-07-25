package com.passwordcracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Stratégie de cassage par dictionnaire.
 * Parcourt une liste de mots, calcule leur hash MD5 et le compare
 * au hash recherché.
 */
public class DictionaryHashCracker implements HashCracker {

    private String dictionaryPath;

    public DictionaryHashCracker(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    @Override
    public String crack(String hash) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryPath))) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                if (word.isEmpty()) {
                    continue;
                }
                if (Md5Utils.hash(word).equalsIgnoreCase(hash)) {
                    return word;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le dictionnaire: " + dictionaryPath, e);
        }
        return null;
    }
}
