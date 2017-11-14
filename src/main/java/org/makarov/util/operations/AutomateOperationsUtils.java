package org.makarov.util.operations;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.translators.constants.RegexConstants;
import org.makarov.util.AutomateReflection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutomateOperationsUtils {

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

        if (RegexConstants.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                !RegexConstants.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return secondAlwaysSymbol;
        } else if (!RegexConstants.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                RegexConstants.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return firstAlwaysSymbol;
        } else if (RegexConstants.ALWAYS_SYMBOL.equals(firstAlwaysSymbol) &&
                RegexConstants.ALWAYS_SYMBOL.equals(secondAlwaysSymbol)) {
            return RegexConstants.ALWAYS_SYMBOL;
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
}
