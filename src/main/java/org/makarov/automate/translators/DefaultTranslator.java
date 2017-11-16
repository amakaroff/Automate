package org.makarov.automate.translators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTranslator implements Translator {

    private static Map<String, List<String>> translators = new HashMap<>();

    public DefaultTranslator() {
        init();
    }

    private void init() {
        List<String> letters = new ArrayList<>();
        fillListOfSymbols(letters, 'a', 'z');
        fillListOfSymbols(letters, 'A', 'Z');
        translators.put(Translator.LETTER_SYMBOL, letters);

        List<String> numbers = new ArrayList<>();
        fillListOfSymbols(numbers, '0', '9');
        translators.put(Translator.NUMBER_SYMBOL, numbers);

        List<String> spaces = new ArrayList<>();
        spaces.add(" ");
        spaces.add("\t");
        spaces.add("\r");
        spaces.add("\n");
        translators.put(Translator.SPACE_SYMBOL, spaces);
    }

    @Override
    public List<String> getTranslateElements(String character) {
        return translators.get(character);
    }
}
