package org.makarov.util.parser;

import org.makarov.automate.Automate;

import java.util.ArrayList;
import java.util.List;

public class RegexParser {

    public static Automate parseRegex(String regex) {
        int index = 0;
        List<StringBuilder> builders = new ArrayList<>();


        while (index < regex.length()) {
            char character = regex.charAt(index);
            if (isSpace(character)) {
                index++;
                continue;
            } else if (character == '/') {
                StringBuilder builder = new StringBuilder();
            }
        }

        return null;
    }

    private static boolean isSpace(char symbol) {
        return symbol == ' ' || symbol == '\n' || symbol == '\t' || symbol == '\r';
    }
}
