package org.makarov.task.translators;

import java.util.List;

public interface Translator {

    List<String> getTranslateElements(String character);

    default void fillListOfSymbols(List<String> list, char startChar, char endChar) {
        char currentChar = startChar;

        while (currentChar != endChar) {
            list.add(String.valueOf(currentChar));
            currentChar++;
        }
        list.add(String.valueOf(currentChar));
    }
}
