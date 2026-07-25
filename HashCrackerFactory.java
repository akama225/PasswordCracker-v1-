public class HashCrackerFactory {
    public static HashCracker create(String method) {
        if ("BRUTE".equalsIgnoreCase(method)) {
            return new BruteForceHashCracker();
        } else if ("DICO".equalsIgnoreCase(method)) {
            return new DictionaryHashCracker("dictionary.txt");
        } else {
            throw new IllegalArgumentException("Méthode inconnue: " + method);
        }
    }
}
