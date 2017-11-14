package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AutomateRenamer {

    public static <T> void renameStates(Automate<T> first, Automate<T> second) {
        if (first == null || second == null) {
            return;
        }

        AutomateReflection<T> firstReflection = new AutomateReflection<>(first);
        AutomateReflection<T> secondReflection = new AutomateReflection<>(second);

        Set<String> firstSet = firstReflection.getTransitions().keySet();
        Set<String> secondSet = secondReflection.getTransitions().keySet();

        if (firstSet.size() > secondSet.size()) {
            renamingStates(firstReflection, secondReflection);
        } else {
            renamingStates(secondReflection, firstReflection);
        }
    }


    public static <T> void renameAutomate(Automate<T> automate) {
        if (automate == null) {
            return;
        }

        automate.init();
        AutomateReflection<T> reflection = new AutomateReflection<>(automate);
        int index = 1;
        Map<String, Map<String, T>> transitions = reflection.getTransitions();
        Set<String> states = reflection.getTransitions().keySet();

        Map<String, String> changes = new HashMap<>();

        Set<String> automateStates = new TreeSet<>(Functions.stringComparator);
        automateStates.addAll(transitions.keySet());
        for (String state : automateStates) {
            String newState = String.valueOf(index);
            if (newState.equals(state)) {
                index++;
                continue;
            }

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

        if (changes.size() > 1) {
            String state = String.valueOf(index - 1);
            String oldState = changes.get(state);
            renameTransition(reflection, state, oldState);
        }

        if (reflection.getBeginState() instanceof String) {
            renameTransition(reflection, reflection.getBeginState().toString(), "1");
        }
    }

    private static String findTempState(Set<String> states) {
        int index = 1;
        while (states.contains(String.valueOf(index))) {
            index++;
        }

        return String.valueOf(index);
    }

    private static <T> void renameTransition(AutomateReflection<T> reflection, String oldState, String newState) {
        renameTransitions(reflection, oldState, newState);
        renameState(reflection, oldState, newState);
        renameBeginState(reflection, oldState, newState);
        renameEndState(reflection, oldState, newState);
    }

    private static <T> void renamingStates(AutomateReflection<T> first, AutomateReflection<T> second) {
        int firstSize = first.getTransitions().keySet().size() + 1;
        int secondSize = second.getTransitions().keySet().size();
        renameAutomate(first.getAutomate());
        renameAutomate(second.getAutomate());

        for (int i = 1; i <= secondSize; i++) {
            renameTransition(second, String.valueOf(i), String.valueOf(firstSize + i));
        }
    }

    private static <T> void renameState(AutomateReflection<T> reflection, String oldState, String newState) {
        Map<String, Map<String, T>> transitions = reflection.getTransitions();
        Map<String, T> map = transitions.get(oldState);
        if (map != null) {
            transitions.remove(oldState);
            transitions.put(newState, map);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void renameBeginState(AutomateReflection<T> reflection, String oldState, String newState) {
        T beginState = reflection.getBeginState();
        if (beginState instanceof Collection) {
            renameInCollection(beginState, oldState, newState);
        } else {
            if (String.valueOf(beginState).equals(oldState)) {
                reflection.setBeginState((T) newState);
            }
        }
    }

    private static <T> void renameEndState(AutomateReflection<T> reflection, String oldState, String newState) {
        Object endState = reflection.getEndStates();
        renameInCollection(endState, oldState, newState);
    }

    private static <T> void renameTransitions(AutomateReflection<T> reflection, String oldState, String newState) {
        Map<String, Map<String, T>> transitions = reflection.getTransitions();
        for (Map<String, T> map : transitions.values()) {
            renameInMap(map, oldState, newState);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void renameInMap(Map<String, T> map, String oldState, String newState) {
        List<String> changeKeyList = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, T> entry : map.entrySet()) {
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
                if (map.get(key) instanceof String) {
                    map.replace(key, (T) newState);
                }
            }
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
