/**
 * Fabrique responsable de la création des objets HashCracker.
 * Centralise l'instanciation des stratégies concrètes : le programme
 * principal ne connaît jamais DictionaryHashCracker ni BruteForceHashCracker
 * directement, seulement l'interface HashCracker.
 */
public class HashCrackerFactory {

    private static final String DEFAULT_DICTIONARY_PATH = "dictionary.txt";

    public static HashCracker create(String method) {
        if (method == null) {
            throw new IllegalArgumentException("La méthode ne peut pas être null");
        }

        switch (method.toUpperCase()) {
            case "DICO":
                return new DictionaryHashCracker(DEFAULT_DICTIONARY_PATH);
            case "BRUTE":
                return new BruteForceHashCracker();
            default:
                throw new IllegalArgumentException("Méthode inconnue: " + method);
        }
    }
}
