package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomateRenamer {

    @SuppressWarnings("unchecked")
    public static void renameStates(Automate first, Automate second) {
        if (first == null || second == null) {
            return;
        }

        AutomateReflection firstReflection = new AutomateReflection(first);
        AutomateReflection secondReflection = new AutomateReflection(second);

        Set firstSet = firstReflection.getTransitions().keySet();
        Set secondSet = secondReflection.getTransitions().keySet();

        if (firstSet.size() > secondSet.size()) {
            renamingStates(firstReflection, secondReflection);
        } else {
            renamingStates(secondReflection, firstReflection);
        }
    }

    @SuppressWarnings("unchecked")
    public static void renameAutomate(Automate automate) {
        if (automate == null) {
            return;
        }
        automate.init();
        AutomateReflection reflection = new AutomateReflection(automate);
        int index = 1;
        Map transitions = reflection.getTransitions();
        Set<String> states = reflection.getTransitions().keySet();

        Map<String, String> changes = new HashMap<>();
        Set<String> automateStates = new HashSet<>(transitions.keySet());
        for (String state : automateStates) {
            String newState = String.valueOf(index);

            if (states.contains(newState)) {
                String tempState = findTempState(transitions.keySet());
                renameTransition(reflection, newState, tempState);
                changes.put(newState, tempState);
            }

            if (changes.containsKey(state)) {
                String tempState = changes.get(state);
                changes.remove(state);
                state = tempState;
            }

            renameTransition(reflection, state, newState);
            index++;
        }

        if (!changes.isEmpty()) {
            String newState = String.valueOf(index - 1);
            String string = changes.get(newState);
            renameTransition(reflection, string, newState);
        }
    }

    private static String findTempState(Set<String> states) {
        int index = 1;
        while (states.contains(String.valueOf(index))) {
            index++;
        }

        return String.valueOf(index);
    }

    private static void renameTransition(AutomateReflection reflection, String oldState, String newState) {
        renameTransitions(reflection, oldState, newState);
        renameState(reflection, oldState, newState);
        renameBeginState(reflection, oldState, newState);
        renameEndState(reflection, oldState, newState);
    }

    private static void renamingStates(AutomateReflection first, AutomateReflection second) {
        int firstSize = first.getTransitions().keySet().size();
        int secondSize = second.getTransitions().keySet().size();
        renameAutomate(first.getAutomate());
        renameAutomate(second.getAutomate());

        for (int i = 1; i <= secondSize; i++) {
            renameTransition(second, String.valueOf(i), String.valueOf(firstSize + i));
        }
    }

    private static void renameState(AutomateReflection reflection, String oldState, String newState) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> transitions = reflection.getTransitions();
        Map<String, Object> map = transitions.get(oldState);
        if (!oldState.equals(newState)) {
            transitions.remove(oldState);
            transitions.put(newState, map);
        }
    }

    @SuppressWarnings("unchecked")
    private static void renameBeginState(AutomateReflection reflection, String oldState, String newState) {
        Object beginState = reflection.getBeginState();
        if (beginState instanceof Collection) {
            renameInCollection(beginState, oldState, newState);
        } else {
            if (String.valueOf(beginState).equals(oldState)) {
                reflection.setBeginState(newState);
            }
        }
    }


    private static void renameEndState(AutomateReflection reflection, String oldState, String newState) {
        Object endState = reflection.getEndStates();
        renameInCollection(endState, oldState, newState);
    }

    private static void renameTransitions(AutomateReflection reflection, String oldState, String newState) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> transitions = reflection.getTransitions();
        for (Map<String, Object> map : transitions.values()) {
            renameInMap(map, oldState, newState);
        }
    }

    private static void renameInMap(Map<String, Object> map, String oldState, String newState) {
        List<String> changeKeyList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Collection) {
                renameInCollection(entry.getValue(), oldState, newState);
            } else {
                Object value = entry.getValue();
                value = (value == null ? null : String.valueOf(value));
                if (oldState.equals(value)) {
                    changeKeyList.add(entry.getKey());
                }
            }
        }

        for (String key : changeKeyList) {
            map.replace(key, newState);
        }
    }

    private static void renameInCollection(Object states, String oldState, String newState) {
        if (states instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<String> value = (Collection<String>) states;
            if (value.contains(oldState)) {
                value.remove(oldState);
                value.add(newState);
            }
        }
    }
}
