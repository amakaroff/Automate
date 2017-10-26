package org.makarov.task.translators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 26.10.2017.
 */
public class BasicTranslator {

    private static Map<String, List<String>> translations = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        List<String> letters = new ArrayList<>();
        fillListOfSymbols(letters, 'a', 'z');
        fillListOfSymbols(letters, 'A', 'Z');
        translations.put("\\w", letters);

        List<String> numbers = new ArrayList<>();
        fillListOfSymbols(numbers, '0', '9');
        translations.put("\\d", numbers);

        List<String> spaces = new ArrayList<>();
        spaces.add(" ");
        spaces.add("\t");
        spaces.add("\r");
        spaces.add("\n");
        translations.put("\\s", spaces);

        List<String> openBracket = new ArrayList<>();
        openBracket.add("(");
        translations.put("\\(", openBracket);

        List<String> closeBracket = new ArrayList<>();
        closeBracket.add(")");
        translations.put("\\)", closeBracket);

        List<String> verticalStick = new ArrayList<>();
        verticalStick.add("|");
        translations.put("\\|", verticalStick);

        List<String> star = new ArrayList<>();
        star.add("*");
        translations.put("\\*", star);
    }

    private static void fillListOfSymbols(List<String> list, char startChar, char endChar) {
        char currentChar = startChar;

        while (currentChar != endChar) {
            list.add(String.valueOf(currentChar));
            currentChar++;
        }
        list.add(String.valueOf(currentChar));
    }

    public static List<String> getTranslationList(String character) {
        return translations.get(character);
    }
}
