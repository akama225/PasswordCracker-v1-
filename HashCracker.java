/**
 * Interface commune à toutes les stratégies de cassage de hash.
 */
public interface HashCracker {
    String crack(String hash);
}