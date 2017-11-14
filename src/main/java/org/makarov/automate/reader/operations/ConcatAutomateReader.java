package org.makarov.automate.reader.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.operations.AutomateOperationsUtils;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcatAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<Set<String>> first;

    private AutomateReflection<Set<String>> second;

    public ConcatAutomateReader(Automate<Set<String>> first, Automate<Set<String>> second) {
        first.init();
        second.init();

        AutomateRenamer.renameStates(first, second);

        this.first = new AutomateReflection<>(first);
        this.second = new AutomateReflection<>(second);
    }

    @Override
    public List<String> getAlphabet() {
        Set<String> alphabet = new HashSet<>();
        alphabet.addAll(first.getAlphabet());
        alphabet.addAll(second.getAlphabet());

        return new ArrayList<>(alphabet);
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
        Map<String, Map<String, Set<String>>> table = new HashMap<>();
        Map<String, Map<String, Set<String>>> firstTable = new HashMap<>(first.getTransitions());
        Map<String, Map<String, Set<String>>> secondTable = new HashMap<>(second.getTransitions());

        Set<String> transitions = new HashSet<>();
        transitions.addAll(first.getTransitions().keySet());
        transitions.addAll(second.getTransitions().keySet());

        String emptyState = getEmptyState(second);

        for (String state : transitions) {
            Map<String, Set<String>> newState = AutomateOperationsUtils.joinMap(firstTable.get(state),
                    secondTable.get(state), getAlphabet());
            table.put(state, newState);
        }

        Set<String> beginStates = new HashSet<>(second.getBeginState());
        beginStates.remove(emptyState);

        for (String endState : first.getEndStates()) {
            Map<String, Set<String>> endStateMap = new HashMap<>(table.get(endState));
            for (String beginState : beginStates) {
                Map<String, Set<String>> beginStateMap = secondTable.get(beginState);
                for (String signal : getAlphabet()) {
                    Set<String> states = AutomateOperationsUtils.getState(endStateMap.get(signal));
                    states.addAll(AutomateOperationsUtils.getState(beginStateMap.get(signal)));
                    endStateMap.put(signal, states);
                }
            }
        }

        return table;
    }

    @Override
    public Set<String> getBeginState() {
        return new HashSet<>(first.getBeginState());
    }

    @Override
    public Set<String> getEndStates() {
        Set<String> endState = new HashSet<>(second.getEndStates());
        String emptyState = getEmptyState(second);
        if (emptyState != null) {
            endState.remove(emptyState);
            endState.addAll(first.getEndStates());
        }

        return endState;
    }

    @Override
    public String getName() {
        return AutomateOperations.GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private String getEmptyState(AutomateReflection<Set<String>> automate) {
        Set<String> endStates = automate.getEndStates();
        for (String state : automate.getBeginState()) {
            if (endStates.contains(state)) {
                return state;
            }
        }

        return null;
    }

    @Override
    public String getAlwaysSymbol() {
        return AutomateOperationsUtils.getAlwaysSymbol(first, second);
    }
}
