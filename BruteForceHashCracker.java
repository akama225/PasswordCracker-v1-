public class BruteForceHashCracker implements HashCracker {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    private long attempts;
    private long executionTimeMs;

    @Override
    public String crack(String hash) {
        String normalizedHash = hash.toLowerCase();
        attempts = 0;
        long startTime = System.currentTimeMillis();

        String result = null;
        for (int length = 1; length <= MAX_LENGTH && result == null; length++) {
            char[] candidate = new char[length];
            result = tryCombinations(candidate, 0, length, normalizedHash);
        }

        executionTimeMs = System.currentTimeMillis() - startTime;
        return result;
    }

    private String tryCombinations(char[] candidate, int index, int length, String targetHash) {
        if (index == length) {
            attempts++;
            String word = new String(candidate);
            String wordHash = Md5Utils.hash(word);
            if (wordHash.equals(targetHash)) {
                return word;
            }
            return null;
        }

        for (char c : ALPHABET.toCharArray()) {
            candidate[index] = c;
            String result = tryCombinations(candidate, index + 1, length, targetHash);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public long getAttempts() {
        return attempts;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}
