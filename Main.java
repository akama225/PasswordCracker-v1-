import java.io.File;

public class Main {
    public static void main(String[] args) {
        String method = null;
        String hash = null;

        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            if ("-m".equals(args[i]) && i + 1 < args.length) {
                method = args[i + 1];
                i++;
            } else if ("-h".equals(args[i]) && i + 1 < args.length) {
                hash = args[i + 1];
                i++;
            }
        }

        // Validate method
        if (method == null) {
            System.err.println("Erreur : la méthode (-m) est requise. (Ex: -m BRUTE ou -m DICO)");
            System.exit(1);
        }

        // Validate hash
        if (hash == null) {
            System.err.println("Erreur : le hash (-h) est requis.");
            System.exit(1);
        }

        if (!hash.matches("^[a-fA-F0-9]{32}$")) {
            System.err.println("Erreur : le hash fourni est invalide. Il doit s'agir d'une chaîne hexadécimale de 32 caractères.");
            System.exit(1);
        }
        
        // Dictionary missing error check (specific to DICO)
        if ("DICO".equalsIgnoreCase(method)) {
            File dictFile = new File("dictionary.txt");
            if (!dictFile.exists() || !dictFile.isFile()) {
                System.err.println("Erreur : dictionnaire manquant (fichier 'dictionary.txt' introuvable).");
                System.exit(1);
            }
        }

        try {
            // Creation through the Factory (no direct instantiation of concrete strategies)
            HashCracker cracker = HashCrackerFactory.create(method);
            
            long startTime = System.currentTimeMillis();
            String result = cracker.crack(hash);
            long endTime = System.currentTimeMillis();

            if (result != null) {
                System.out.println("Password found: " + result);
            } else {
                System.out.println("Password not found");
            }
            
            System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
            
            if (cracker instanceof BruteForceHashCracker) {
                System.out.println("Tentatives : " + ((BruteForceHashCracker) cracker).getAttempts());
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Erreur d'exécution : " + e.getMessage());
            System.exit(1);
        }
    }
}
