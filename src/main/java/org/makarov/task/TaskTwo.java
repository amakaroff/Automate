package org.makarov.task;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;
import org.makarov.automate.Translator;
import org.makarov.task.translators.RealTranslator;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class TaskTwo {

    public static String findAllNumbers(Automate<String> automate, String line) {
        int index = 0;
        List<String> numbers = new ArrayList<>();

        while (index < line.length()) {
            int currentIndex = Functions.function(automate, line, index).getValue();
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
