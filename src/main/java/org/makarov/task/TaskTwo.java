package org.makarov.task;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class TaskTwo {

    public static Pair<Boolean, Integer> function(Automate<String> automate, String line, int index) {
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        automate.init();
        for (int i = index; i < line.length(); i++) {
            try {
                String signal = translate(line.charAt(i));
                automate.nextState(signal);
                tempCount++;
                if (automate.isEnd()) {
                    allCount += tempCount;
                    tempCount = 0;
                    isEnd = true;
                }
            } catch (AutomateException exception) {
                break;
            }
        }

        return new Pair<>(isEnd, allCount);
    }

    private static String translate(char character) {
        if (Character.isDigit(character)) {
            return "N";
        }

        if (character == '+' || character == '-') {
            return "S";
        }

        if (character == '.') {
            return "P";
        }

        if (character == 'e' || character == 'E') {
            return "E";
        }

        return "";
    }


    public static String findAllNumbers(Automate<String> automate, String line) {
        int index = 0;
        List<String> numbers = new ArrayList<>();

        while (index < line.length()) {
            int currentIndex = function(automate, line, index).getValue();
            if (currentIndex == 0) {
                index++;
            } else {
                numbers.add(line.substring(index, index+currentIndex));
                index += currentIndex;
            }
        }

        return numbers.toString();
    }
}
