package org.makarov.automate.translators;

import java.util.List;

public interface Translator {

    String ALWAYS_SYMBOL = "\\.";

    String EMPTY_SYMBOL = "\\?";

    String SPACE_SYMBOL = "\\s";

    String LETTER_SYMBOL = "\\w";

    String NUMBER_SYMBOL = "\\d";

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
