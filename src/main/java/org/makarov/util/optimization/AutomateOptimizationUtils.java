package org.makarov.util.optimization;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateOperationsUtils;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AutomateOptimizationUtils {

    private static final String NON_EQUALS_SYMBOL = "X";

    private static final String EQUALS_SYMBOL = "O";

    @SuppressWarnings("unchecked")
    public static <T> void optimization(Automate<T> automate) {
        AutomateReflection<T> reflection = new AutomateReflection<>(automate);
        doBasicOptimization(reflection);
        removeUnattainableStates(reflection);
        automateMinimization(reflection);

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
                        Collection<String> beginStates = AutomateOperationsUtils.toList(beginState);
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
                Collection<String> states = AutomateOperationsUtils.toList(beginState);
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
        Map<String, Map<String, String>> checkMap = createCheckMap(automate);
        Set<String> endStates = automate.getEndStates();
        Set<String> states = new HashSet<>(automate.getTransitions().keySet());
        Map<String, Map<String, String>> transitions = automate.getTransitions();

        fillCheckMap(automate, checkMap);

        for (String state : states) {
            for (String innerState : states) {
                if (checkMap.get(innerState).get(state).equals(EQUALS_SYMBOL)) {
                    checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                }
            }
        }

        for (String state : states) {
            for (String innerState : states) {
                if (checkMap.get(state).get(innerState).equals(EQUALS_SYMBOL)) {
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
    }

    private static <T> void automateMinimization(AutomateReflection<T> automate) {
        Map<String, Map<String, T>> transitions = automate.getTransitions();
        Set<String> states = transitions.keySet();
        Set<String> endStates = automate.getEndStates();

        boolean isEndOptimization = false;

        Map<String, String> changes = new HashMap<>();

        while (!isEndOptimization) {
            isEndOptimization = true;
            for (String state : states) {
                for (String innerState : states) {
                    if (!state.equals(innerState)) {
                        Map<String, T> firstMap = getNewMap(transitions.get(state), state);
                        Map<String, T> secondMap = getNewMap(transitions.get(innerState), innerState);
                        if (firstMap.equals(secondMap)) {
                            isEndOptimization = true;
                            changes.put(state, innerState);
                        }
                    }
                }
            }

            for (Map.Entry<String, String> entry : changes.entrySet()) {
                if (isEquivalentState(endStates, entry.getKey(), entry.getValue())) {
                    if (automate.getBeginState().equals(entry.getKey())) {
                        automate.setBeginState(AutomateOperationsUtils.geneticCastHack(entry.getValue()));
                    }
                    removeState(entry.getKey(), entry.getValue(), transitions);
                    endStates.remove(entry.getKey());
                }
            }
        }
    }

    private static boolean isEquivalentState(Set<String> endStates, String oldState, String newState) {
        return endStates.contains(oldState) && endStates.contains(newState)
                || !endStates.contains(oldState) && !endStates.contains(newState);
    }

    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> getNewMap(Map<String, T> map, String state) {
        Map<String, T> newMap = new HashMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (entry.getValue() instanceof Collection) {
                Collection collection = new ArrayList((Collection) entry.getValue());
                collection.remove(state);
                newMap.put(entry.getKey(), (T) collection);
                ((Collection) entry.getValue()).remove(state);
            } else {
                String value = state.equals(entry.getValue()) ? null : String.valueOf(entry.getValue());
                newMap.put(entry.getKey(), AutomateOperationsUtils.geneticCastHack(value));
            }
        }

        return newMap;
    }


    private static void fillCheckMap(AutomateReflection<String> automate, Map<String, Map<String, String>> checkMap) {
        Set<String> states = automate.getTransitions().keySet();
        Map<String, Map<String, String>> transitions = automate.getTransitions();
        List<String> alphabet = automate.getAlphabet();

        boolean isEndChecking = false;
        boolean isNullStates = false;

        while (!isEndChecking) {
            isEndChecking = true;
            for (String state : states) {
                for (String innerState : states) {
                    for (String letter : alphabet) {
                        if (checkMap.get(state).get(innerState).equals(EQUALS_SYMBOL) && !state.equals(innerState)) {
                            String firstState = String.valueOf(transitions.get(state).get(letter));
                            String secondState = String.valueOf(transitions.get(innerState).get(letter));

                            if (firstState.equals("null") || secondState.equals("null")) {
                                isNullStates = true;
                                continue;
                            }

                            if (NON_EQUALS_SYMBOL.equals(checkMap.get(firstState).get(secondState))) {
                                isEndChecking = false;
                                checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                                checkMap.get(innerState).put(state, NON_EQUALS_SYMBOL);
                                break;
                            }
                        }
                    }

                    if (isNullStates) {
                        isNullStates = false;
                        isEndChecking = false;
                        checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                        checkMap.get(innerState).put(state, NON_EQUALS_SYMBOL);
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
                        Collection<String> states = AutomateOperationsUtils.toList(value);
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
                    Collection<String> values = AutomateOperationsUtils.toList(entry.getValue());
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
