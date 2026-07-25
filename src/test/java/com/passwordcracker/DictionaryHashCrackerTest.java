package com.passwordcracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DictionaryHashCrackerTest {

    private static final String DICTIONARY_PATH = "src/main/resources/dictionary.txt";

    @Test
    void shouldFindPasswordInDictionary() {
        DictionaryHashCracker cracker = new DictionaryHashCracker(DICTIONARY_PATH);
        String hash = Md5Utils.hash("test");
        assertEquals("test", cracker.crack(hash));
    }

    @Test
    void shouldFindAnotherPasswordInDictionary() {
        DictionaryHashCracker cracker = new DictionaryHashCracker(DICTIONARY_PATH);
        String hash = Md5Utils.hash("admin");
        assertEquals("admin", cracker.crack(hash));
    }

    @Test
    void shouldReturnNullWhenHashNotFound() {
        DictionaryHashCracker cracker = new DictionaryHashCracker(DICTIONARY_PATH);
        assertNull(cracker.crack("0000000000000000000000000000000"));
    }
}
