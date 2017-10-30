package org.makarov.task.translators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Aleksei Makarov on 26.10.2017.
 */
public class BasicTranslator {

    private static Map<String, List<String>> translators = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        List<String> letters = new ArrayList<>();
        fillListOfSymbols(letters, 'a', 'z');
        fillListOfSymbols(letters, 'A', 'Z');
        translators.put("\\w", letters);

        List<String> numbers = new ArrayList<>();
        fillListOfSymbols(numbers, '0', '9');
        translators.put("\\d", numbers);

        List<String> spaces = new ArrayList<>();
        spaces.add(" ");
        spaces.add("\t");
        spaces.add("\r");
        spaces.add("\n");
        translators.put("\\s", spaces);

        List<String> openBracket = new ArrayList<>();
        openBracket.add("(");
        translators.put("\\(", openBracket);

        List<String> closeBracket = new ArrayList<>();
        closeBracket.add(")");
        translators.put("\\)", closeBracket);

        List<String> verticalStick = new ArrayList<>();
        verticalStick.add("|");
        translators.put("\\|", verticalStick);

        List<String> star = new ArrayList<>();
        star.add("*");
        translators.put("\\*", star);

        List<String> slash = new ArrayList<>();
        slash.add("\\");
        translators.put("\\\\", slash);
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
        return translators.get(character);
    }
}
