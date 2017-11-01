package org.makarov.automate.reader;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnionAutomateReader implements AutomateReader<Set<String>> {

    private static final String GENERATE_NAME = "Generate automate";

    AutomateReflection<Set<String>> first;

    AutomateReflection<Set<String>> second;

    @SuppressWarnings("unchecked")
    public UnionAutomateReader(Automate first, Automate second) {
        first.init();
        second.init();

        AutomateRenamer.renameStates(first, second);

        this.first = new AutomateReflection(first);
        this.second = new AutomateReflection(second);
    }

    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        alphabet.addAll(first.getAlphabet());
        alphabet.addAll(second.getAlphabet());
        return alphabet;
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
        Map<String, Map<String, Set<String>>> table = new HashMap<>();

        Set<String> transitions = new HashSet<>();
        transitions.addAll(first.getTransitions().keySet());
        transitions.addAll(second.getTransitions().keySet());

        Map<String, Map<String, Set<String>>> firstTable = first.getTransitions();
        Map<String, Map<String, Set<String>>> secondTable = second.getTransitions();

        for (String state : transitions) {
            Map<String, Set<String>> newState = joinMap(firstTable.get(state), secondTable.get(state));
            table.put(state, newState);
        }

        return table;
    }

    private Map<String, Set<String>> joinMap(Map<String, Set<String>> first, Map<String, Set<String>> second) {
        if (first == null) {
            first = new HashMap<>();
        }

        if (second == null) {
            second = new HashMap<>();
        }

        Map<String, Set<String>> joinedMap = new HashMap<>();
        for (String signal : getAlphabet()) {
            Set<String> newState = new HashSet<>();
            newState.addAll(getState(first.get(signal)));
            newState.addAll(getState(second.get(signal)));
            joinedMap.put(signal, newState);
        }

        return joinedMap;
    }

    private Set<String> getState(Set<String> set) {
        if (set == null) {
            Set<String> newState = new HashSet<>();
            newState.add(null);
            return newState;
        } else {
            return set;
        }
    }


    @Override
    public Set<String> getBeginState() {
        Set<String> beginStates = new HashSet<>();
        beginStates.addAll(first.getBeginState());
        beginStates.addAll(second.getBeginState());
        return beginStates;
    }

    @Override
    public List<String> getEndStates() {
        List<String> endStates = new ArrayList<>();
        endStates.addAll(first.getEndStates());
        endStates.addAll(second.getEndStates());
        return endStates;
    }

    @Override
    public String getName() {
        return GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
