package org.makarov.util.optimization;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.serialize.AutomateToStringSerializer;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateRenamer;

import java.util.*;

public class AutomateOptimizationUtils {

    private static final String NON_EQUALS_SYMBOL = "X";

    private static final String EQUALS_SYMBOL = "O";

    @SuppressWarnings("unchecked")
    public static void optimization(Automate automate) {
        AutomateReflection reflection = new AutomateReflection(automate);
        Map<String, Map<String, Object>> transitions = reflection.getTransitions();
        Object beginState = reflection.getBeginState();
        Set<String> endStates = reflection.getEndStates();

        List<String> removeStates = new ArrayList<>();

        boolean isEndOptimize = false;

        while (!isEndOptimize) {
            List<String> states = new ArrayList<>(transitions.keySet());
            String currentState = "";
            for (int i = 0; i < states.size(); i++) {
                currentState = states.get(i);
                for (int j = i + 1; j < states.size(); j++) {
                    String newState = states.get(j);
                    if ((endStates.contains(currentState) && endStates.contains(newState) ||
                            !endStates.contains(currentState) && !endStates.contains(newState)) &&
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

        System.out.println(automate.toString());

        if (automate instanceof DeterministicAutomate) {
            deleteEqualsState(reflection);
        }

        AutomateRenamer.renameAutomate(automate);
    }

    @SuppressWarnings("unchecked")
    private static DeterministicAutomate deleteEqualsState(AutomateReflection automate) {
        Map<String, Map<String, String>> checkMap = new HashMap<>();
        Set<String> endStates = automate.getEndStates();
        Set<String> states = new HashSet<>(automate.getTransitions().keySet());
        Map<String, Map<String, Object>> transitions = automate.getTransitions();
        List<String> alphabet = automate.getAlphabet();

        for (String state : states) {
            Map<String, String> innerMap = new HashMap<>();
            for (String innerState : states) {
                if (state.equals(innerState)) {
                    innerMap.put(innerState, EQUALS_SYMBOL);
                } else if (endStates.contains(state) && !endStates.contains(innerState)
                        || !endStates.contains(state) && endStates.contains(innerState)) {
                    innerMap.put(innerState, NON_EQUALS_SYMBOL);
                } else {
                    innerMap.put(innerState, "");
                }
            }

            checkMap.put(state, innerMap);
        }

        for (Map<String, String> stringStringMap : checkMap.values()) {
            System.out.println(stringStringMap);
        }

        boolean isEndChecking = false;

        while (!isEndChecking) {
            isEndChecking = true;
            for (String state : states) {
                for (String innerState : states) {
                    for (String letter : alphabet) {
                        if (!checkMap.get(state).get(innerState).equals(NON_EQUALS_SYMBOL)) {
                            String firstState = String.valueOf(transitions.get(state).get(letter));
                            String secondState = String.valueOf(transitions.get(innerState).get(letter));

                            if (!firstState.equals("null") && !secondState.equals("null") &&
                                    NON_EQUALS_SYMBOL.equals(checkMap.get(firstState).get(secondState))) {
                                isEndChecking = false;
                                checkMap.get(state).put(innerState, NON_EQUALS_SYMBOL);
                                checkMap.get(innerState).put(state, NON_EQUALS_SYMBOL);
                                break;
                            }
                        }
                    }

                    if (!checkMap.get(innerState).get(state).equals(NON_EQUALS_SYMBOL)) {
                        checkMap.get(innerState).put(state, EQUALS_SYMBOL);
                        checkMap.get(state).put(innerState, EQUALS_SYMBOL);
                    }
                }
            }
        }

        for (String state : states) {
            for (String innerState : states) {
                if (checkMap.get(state).get(innerState).equals(EQUALS_SYMBOL)) {
                    checkMap.get(innerState).put(state, NON_EQUALS_SYMBOL);
                }
            }
        }

        for (String state : states) {
            for (String innerState : states) {
                if (checkMap.get(state).get(innerState).equals(EQUALS_SYMBOL)) {
                    if (automate.getBeginState().equals(state)) {
                        automate.setBeginState(innerState);
                    }

                    if (endStates.contains(state) && endStates.contains(innerState)
                            || !endStates.contains(state) && !endStates.contains(innerState)) {
                        removeState(state, innerState, transitions);
                    }
                }
            }
        }

        return (DeterministicAutomate) automate.getAutomate();
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

    private static void removeState(String state, String newState, Map<String, Map<String, Object>> transitions) {
        transitions.remove(state);
        for (Map<String, Object> map : transitions.values()) {
            List<String> removeTemplate = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Collection) {
                    @SuppressWarnings("unchecked")
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
}
