package org.makarov.automate.reader.transform;

import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.Translator;
import org.makarov.util.AutomateReflection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransformDeterministicAutomateReader implements AutomateReader<String> {

    private AutomateReflection<Set<String>> automateReflection;

    private Map<List<String>, String> newStates = new HashMap<>();

    private Map<String, Map<String, String>> table = new HashMap<>();

    private Deque<List<String>> stack = new ArrayDeque<>();

    private int nextState = 1;

    private String beginState;

    private List<String> endStates = new ArrayList<>();

    public TransformDeterministicAutomateReader(NonDeterministicAutomate automate) {
        this.automateReflection = new AutomateReflection<>(automate);
        this.table = reconstructAutomate();
    }

    @Override
    public List<String> getAlphabet() {
        return new ArrayList<>(automateReflection.getAlphabet());
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        return table;
    }

    @Override
    public String getBeginState() {
        return beginState;
    }

    @Override
    public List<String> getEndStates() {
        return endStates;
    }

    @Override
    public String getName() {
        return automateReflection.getName();
    }

    @Override
    public int getPriority() {
        return automateReflection.getPriority();
    }

    private Map<String, Map<String, String>> reconstructAutomate() {
        List<String> beginStates = new ArrayList<>(automateReflection.getBeginState());
        beginState = getState(beginStates);
        stack.push(beginStates);

        while (!stack.isEmpty()) {
            List<String> currentState = stack.pop();
            Map<String, String> newTransitions = new HashMap<>();
            for (String signal : automateReflection.getAlphabet()) {
                newTransitions.put(signal, getState(getTransitions(currentState, signal)));
            }

            if (isEndState(currentState)) {
                this.endStates.add(getState(currentState));
            }

            table.put(getState(currentState), newTransitions);
        }

        return table;
    }

    private List<String> getTransitions(List<String> states, String signal) {
        Map<String, Map<String, Set<String>>> transitions = automateReflection.getTransitions();
        Set<String> newStates = new HashSet<>();
        for (String state : states) {
            Set<String> oldStates = transitions.get(state).get(signal);
            if (oldStates != null && !oldStates.isEmpty() && !oldStates.contains(null)) {
                newStates.addAll(oldStates);
            }
        }

        return new ArrayList<>(newStates);
    }

    private boolean isEndState(List<String> list) {
        List<String> endStates = automateReflection.getEndStates();
        for (String state : list) {
            if (endStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

    private String getState(List<String> list) {
        if (list.isEmpty()) {
            return null;
        }

        String newState = newStates.get(list);
        if (newState == null) {
            String state = String.valueOf(nextState);
            newStates.put(list, state);
            stack.push(list);
            nextState++;
            return state;

        } else {
            return newState;
        }
    }

    @Override
    public String getAlwaysSymbol() {
        return automateReflection.getAlwaysSymbol();
    }
}
