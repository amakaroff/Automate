package org.makarov.util.optimization;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AutomateOptimizationUtils {

    @SuppressWarnings("unchecked")
    public static void optimization(Automate automate) {
        AutomateReflection reflection = new AutomateReflection(automate);
        Map<String, Map<String, Object>> transitions = reflection.getTransitions();
        Object beginState = reflection.getBeginState();
        List<String> endStates = reflection.getEndStates();

        List<String> removeStates = new ArrayList<>();

        boolean isEndOptimize = false;

        while (!isEndOptimize) {
            List<String> states = new ArrayList<>(transitions.keySet());
            String currentState = "";
            for (int i = 0; i < states.size(); i++) {
                currentState = states.get(i);
                for (int j = i + 1; j < states.size(); j++) {
                    String newState = states.get(j);
                    if (containsMap(transitions.get(currentState), transitions.get(newState))) {
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
                    if ((endStates.contains(removeState) && endStates.contains(removeState)) ||
                            (!endStates.contains(removeState) && !endStates.contains(removeState))) {
                        if (beginState instanceof Collection) {
                            Collection<String> beginStates = (Collection<String>) beginState;
                            if (beginStates.contains(removeState)) {
                                beginStates.remove(removeState);
                                if (!beginStates.contains(currentState)) {
                                    beginStates.add(currentState);
                                }
                            }
                        } else {
                            if (beginState.equals(removeState)) {
                                beginState = currentState;
                            }
                        }
                        removeState(removeState, currentState, transitions);
                        endStates.remove(removeState);
                    }
                }

                removeStates.clear();
            }
        }

        List<String> unattainableStates = new ArrayList<>();
        for (String state : transitions.keySet()) {
            if (isUnattainableState(state, transitions)) {
                unattainableStates.add(state);
            }
        }

        for (String unattainableState : unattainableStates) {
            if (beginState instanceof Collection) {
                Collection<String> states = (Collection<String>) beginState;
                if (!states.contains(unattainableState)) {
                    transitions.remove(unattainableState);
                }
            } else {
                if (!beginState.equals(unattainableState)) {
                    transitions.remove(unattainableState);
                }
            }
        }

        AutomateRenamer.renameAutomate(automate);
    }


    @SuppressWarnings("unchecked")
    private static boolean isUnattainableState(String state, Map<String, Map<String, Object>> transitions) {
        for (Map.Entry<String, Map<String, Object>> entry : transitions.entrySet()) {
            if (!entry.getKey().equals(state)) {
                for (Object value : entry.getValue().values()) {
                    if (value instanceof Collection) {
                        Collection<String> states = (Collection<String>) value;
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


    @SuppressWarnings("unchecked")
    private static void removeState(String state, String newState, Map<String, Map<String, Object>> transitions) {
        transitions.remove(state);
        for (Map<String, Object> map : transitions.values()) {
            List<String> removeTemplate = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Collection) {
                    Collection<String> values = (Collection<String>) entry.getValue();
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
                map.put(key, newState);
            }
        }

    }

    private static boolean containsMap(Map<String, Object> first, Map<String, Object> second) {
        if (first == null || second == null || first.size() != second.size()) {
            return false;
        }

        List<Object> firstValues = new ArrayList<>(first.values());
        List<Object> secondValues = new ArrayList<>(second.values());

        for (int i = 0; i < firstValues.size(); i++) {
            Object firstObject = firstValues.get(i);
            Object secondObject = secondValues.get(i);
            if (!Objects.equals(firstObject, secondObject)) {
                return false;
            }
        }

        return true;
    }
}
