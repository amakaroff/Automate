package org.makarov.util.operations;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.translators.Translator;
import org.makarov.util.AutomateReflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class AutomateOperationsUtils {

    private static final Pattern pattern = Pattern.compile("\\d+");

    public static Map<String, Set<String>> joinMap(Map<String, Set<String>> first, Map<String, Set<String>> second, Collection<String> alphabet) {
        if (first == null) {
            first = new HashMap<>();
        }

        if (second == null) {
            second = new HashMap<>();
        }

        Map<String, Set<String>> joinedMap = new HashMap<>();
        for (String signal : alphabet) {
            Set<String> newState = new HashSet<>();

            Set<String> tempFirstState = getState(first.get(signal));
            if (tempFirstState.size() > 1 || !tempFirstState.contains(null)) {
                newState.addAll(tempFirstState);
            }

            Set<String> tempSecondState = getState(second.get(signal));
            if (tempSecondState.size() > 1 || !tempSecondState.contains(null)) {
                newState.addAll(tempSecondState);
            }

            joinedMap.put(signal, newState);
        }

        return joinedMap;
    }

    public static <T> String getAlwaysSymbol(AutomateReflection<T> first, AutomateReflection<T> second) {
        String firstAlwaysSymbol = first.getAlwaysSymbol();
        String secondAlwaysSymbol = second.getAlwaysSymbol();

        if (Translator.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                !Translator.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return secondAlwaysSymbol;
        } else if (!Translator.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                Translator.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return firstAlwaysSymbol;
        } else if (Translator.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                Translator.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return Translator.ALWAYS_SYMBOL;
        }
        throw new AutomateException("Two automates is can't have different always symbols!");
    }

    public static Set<String> getState(Set<String> set) {
        if (set == null) {
            return new HashSet<>();
        } else {
            return set;
        }
    }

    @SuppressWarnings("unchecked")
    public static Collection<String> toStringsCollection(Object object) {
        if (object instanceof Collection) {
            return (Collection<String>) object;
        }

        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T geneticCastHack(Object object) {
        return (T) object;
    }

    public static <T> String getNextState(AutomateReflection<T> automate) {
        Set<String> strings = automate.getTransitions().keySet();
        int nextState = 0;
        for (String state : strings) {
            if (isNumber(state)) {
                int numberState = Integer.valueOf(state);
                if (numberState > nextState) {
                    nextState = numberState;
                }
            }
        }

        return String.valueOf(nextState + 1);
    }

    public static boolean isNumber(String line) {
        return pattern.matcher(line).matches();
    }
}
