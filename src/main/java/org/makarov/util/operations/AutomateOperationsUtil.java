package org.makarov.util.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutomateOperationsUtil {

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

    public static Set<String> getState(Set<String> set) {
        if (set == null) {
            Set<String> newState = new HashSet<>();
            newState.add(null);
            return newState;
        } else {
            return set;
        }
    }
}
