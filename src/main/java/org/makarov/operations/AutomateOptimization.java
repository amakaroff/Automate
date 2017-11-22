package org.makarov.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateReflection;
import org.makarov.automate.DeterministicAutomate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AutomateOptimization {

    private static final String NON_EQUALS_SYMBOL = "X";

    private static final String EQUALS_SYMBOL = "O";

    @SuppressWarnings("unchecked")
    public static <T> void optimization(Automate<T> automate) {
        AutomateReflection<T> reflection = new AutomateReflection<>(automate);
        doBasicOptimization(reflection);
        removeUnattainableStates(reflection);

        if (automate instanceof DeterministicAutomate) {
            removeEquivalentState((AutomateReflection<String>) reflection);
        }

        AutomateRenamer.renameAutomate(automate);
    }

    private static <T> void doBasicOptimization(AutomateReflection<T> automate) {
        Map<String, Map<String, T>> transitions = automate.getTransitions();
        Object beginState = automate.getBeginState();
        Set<String> endStates = automate.getEndStates();

        List<String> removeStates = new ArrayList<>();

        boolean isEndOptimize = false;

        while (!isEndOptimize) {
            List<String> states = new ArrayList<>(transitions.keySet());
            String currentState = "";
            for (int i = 0; i < states.size(); i++) {
                currentState = states.get(i);
                for (int j = i + 1; j < states.size(); j++) {
                    String newState = states.get(j);
                    if (isEquivalentState(endStates, currentState, newState) &&
                            Objects.equals(transitions.get(currentState), transitions.get(newState))) {
                        removeStates.add(newState);
                    }
                }

                if (!removeStates.isEmpty()) {
                    break;
                }
            }

            if (removeStates.isEmpty()) {
                isEndOptimize = true;
            } else {
                for (String removeState : removeStates) {
                    if (beginState instanceof Collection) {
                        Collection<String> beginStates = AutomateOperationsUtils.toStringsCollection(beginState);
                        if (beginStates.contains(removeState)) {
                            beginStates.remove(removeState);
                            if (!beginStates.contains(currentState)) {
                                beginStates.add(currentState);
                            }
                        }
                    } else {
                        if (automate.getBeginState().equals(removeState)) {
                            automate.setBeginState(AutomateOperationsUtils.geneticCastHack(currentState));
                        }
                    }
                    removeState(removeState, currentState, transitions);
                    endStates.remove(removeState);
                }

                removeStates.clear();
            }
        }
    }

    private static <T> void removeUnattainableStates(AutomateReflection<T> automate) {
        Map<String, Map<String, T>> transitions = automate.getTransitions();
        Object beginState = automate.getBeginState();

        List<String> unattainableStates = new ArrayList<>();
        for (String state : transitions.keySet()) {
            if (isUnattainableState(state, transitions)) {
                unattainableStates.add(state);
            }
        }

        for (String unattainableState : unattainableStates) {
            if (beginState instanceof Collection) {
                Collection<String> states = AutomateOperationsUtils.toStringsCollection(beginState);
                if (!states.contains(unattainableState)) {
                    transitions.remove(unattainableState);
                }
            } else {
                if (!beginState.equals(unattainableState)) {
                    transitions.remove(unattainableState);
                }
            }
        }
    }

    private static void removeEquivalentState(AutomateReflection<String> automate) {
        String removedState = addTempState(automate);
        Map<String, Map<String, String>> checkMap = createCheckMap(automate);
        Set<String> endStates = automate.getEndStates();
        Set<String> states = new HashSet<>(automate.getTransitions().keySet());
        Map<String, Map<String, String>> transitions = automate.getTransitions();

        fillCheckMap(automate, checkMap);

        for (String state : states) {
            for (String innerState : states) {
                if (EQUALS_SYMBOL.equals(checkMap.get(innerState).get(state))) {
                    checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                }
            }
        }

        for (String state : states) {
            for (String innerState : states) {
                if (EQUALS_SYMBOL.equals(checkMap.get(state).get(innerState))) {
                    if (isEquivalentState(endStates, state, innerState)) {
                        if (automate.getBeginState().equals(state)) {
                            automate.setBeginState(innerState);
                        }
                        removeState(state, innerState, transitions);
                        endStates.remove(state);
                    }
                }
            }
        }

        removeTempState(automate, removedState);
    }

    private static String addTempState(AutomateReflection<String> automate) {
        String removedState = AutomateOperationsUtils.getNextState(automate);
        Map<String, Map<String, String>> transitions = automate.getTransitions();

        Map<String, String> map = new HashMap<>();
        for (String letter : automate.getAlphabet()) {
            map.put(letter, removedState);
        }

        transitions.put(removedState, map);

        for (Map<String, String> transitionMap : transitions.values()) {
            for (Map.Entry<String, String> entry : transitionMap.entrySet()) {
                if (entry.getValue() == null) {
                    transitionMap.replace(entry.getKey(), null, removedState);
                }
            }
        }

        return removedState;
    }

    private static void removeTempState(AutomateReflection<String> automate, String state) {
        Map<String, Map<String, String>> transitions = automate.getTransitions();

        transitions.remove(state);

        for (Map<String, String> transitionMap : transitions.values()) {
            for (Map.Entry<String, String> entry : transitionMap.entrySet()) {
                if (entry.getValue().equals(state)) {
                    transitionMap.replace(entry.getKey(), state, null);
                }
            }
        }
    }

    private static boolean isEquivalentState(Set<String> endStates, String oldState, String newState) {
        return endStates.contains(oldState) && endStates.contains(newState)
                || !endStates.contains(oldState) && !endStates.contains(newState);
    }

    private static void fillCheckMap(AutomateReflection<String> automate, Map<String, Map<String, String>> checkMap) {
        Set<String> states = automate.getTransitions().keySet();
        Map<String, Map<String, String>> transitions = automate.getTransitions();
        List<String> alphabet = automate.getAlphabet();

        boolean isEndChecking = false;

        while (!isEndChecking) {
            isEndChecking = true;
            for (String state : states) {
                for (String innerState : states) {
                    for (String letter : alphabet) {
                        if (EQUALS_SYMBOL.equals(checkMap.get(state).get(innerState)) && !state.equals(innerState)) {
                            String firstState = String.valueOf(transitions.get(state).get(letter));
                            String secondState = String.valueOf(transitions.get(innerState).get(letter));

                            if (NON_EQUALS_SYMBOL.equals(checkMap.get(firstState).get(secondState))) {
                                isEndChecking = false;
                                checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                                checkMap.get(innerState).put(state, NON_EQUALS_SYMBOL);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static Map<String, Map<String, String>> createCheckMap(AutomateReflection<String> automate) {
        Map<String, Map<String, String>> checkMap = new HashMap<>();
        Set<String> endStates = automate.getEndStates();
        Set<String> states = automate.getTransitions().keySet();

        for (String state : states) {
            Map<String, String> innerMap = new HashMap<>();
            for (String innerState : states) {
                if (state.equals(innerState)) {
                    innerMap.put(innerState, EQUALS_SYMBOL);
                } else if (!isEquivalentState(endStates, state, innerState)) {
                    innerMap.put(innerState, NON_EQUALS_SYMBOL);
                } else {
                    innerMap.put(innerState, EQUALS_SYMBOL);
                }
            }

            checkMap.put(state, innerMap);
        }

        return checkMap;
    }

    private static <T> boolean isUnattainableState(String state, Map<String, Map<String, T>> transitions) {
        for (Map.Entry<String, Map<String, T>> entry : transitions.entrySet()) {
            if (!entry.getKey().equals(state)) {
                for (Object value : entry.getValue().values()) {
                    if (value instanceof Collection) {
                        Collection<String> states = AutomateOperationsUtils.toStringsCollection(value);
                        if (states.contains(state)) {
                            return false;
                        }
                    } else {
                        if (Objects.equals(value, state)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static <T> void removeState(String state, String newState, Map<String, Map<String, T>> transitions) {
        transitions.remove(state);
        for (Map<String, T> map : transitions.values()) {
            List<String> removeTemplate = new ArrayList<>();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                if (entry.getValue() instanceof Collection) {
                    Collection<String> values = AutomateOperationsUtils.toStringsCollection(entry.getValue());
                    if (values.contains(state)) {
                        values.remove(state);
                        values.add(newState);
                    }
                } else {
                    if (Objects.equals(entry.getValue(), state)) {
                        removeTemplate.add(entry.getKey());
                    }
                }
            }

            for (String key : removeTemplate) {
                map.put(key, AutomateOperationsUtils.geneticCastHack(newState));
            }
        }
    }
}
