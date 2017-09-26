package org.makarov.task;

import org.makarov.automate.AutomateException;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class TaskOne {

    public static String function(DeterministicAutomate automate, String line, int index) {
        automate.init();
        for (int i = index; i < line.length(); i++) {
            try {
                String signal = String.valueOf(line.charAt(i));
                automate.nextState(signal);
                if (automate.isEnd()) {
                    if ((i - index) % 2 == 0) {
                        return "Выиграл Петя";
                    } else {
                        return "Выиграл Вася";
                    }
                }
            } catch (AutomateException exception) {
                return "Ошибка!";
            }
        }

        return "Ничья";
    }

    public static Pair<Boolean, Set<String>> function(NonDeterministicAutomate automate, String line, int index) {
        Set<String> lastState = new HashSet<>();

        automate.init();
        for (int i = index; i < line.length(); i++) {
            try {
                String signal = String.valueOf(line.charAt(i));
                automate.nextState(signal);
                lastState = automate.getCurrentState();
            } catch (AutomateException exception) {
                return new Pair<>(false, new HashSet<>());
            }
        }

        if (automate.isEnd()) {
            return new Pair<>(true, lastState);
        } else {
           return new Pair<>(false, lastState);
        }
    }
}
