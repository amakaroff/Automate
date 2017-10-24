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
                automate.nextState(line.charAt(i));
            } catch (AutomateException exception) {
                return "Symbol is undefined!";
            }
        }

        if (automate.isEnd()) {
            return "Line is true!";
        } else {
            return "Line is false!";
        }
    }

    public static Pair<Boolean, Set<String>> function(NonDeterministicAutomate automate, String line, int index) {
        Set<String> lastState = new HashSet<>();

        automate.init();
        for (int i = index; i < line.length(); i++) {
            try {
                automate.nextState(line.charAt(i));
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
