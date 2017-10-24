package org.makarov.task;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;
import org.makarov.automate.Translator;
import org.makarov.task.translators.RealTranslator;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class TaskTwo {

    public static Pair<Boolean, Integer> function(Automate<String> automate, String line, int index) {
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        automate.init();
        if (automate.isEnd()) {
            return new Pair<>(true, allCount);
        }

        Translator translator = new RealTranslator();

        for (int i = index; i < line.length(); i++) {
            try {
                String signal = translator.translate(line.charAt(i));
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

        if (line.length() == 0) {
            isEnd = true;
        }

        return new Pair<>(isEnd, allCount);
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
