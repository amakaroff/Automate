package org.makarov.automate.reader;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.operations.AutomateOperationsUtil;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcatAutomateReader implements AutomateReader<Set<String>> {

    AutomateReflection<Set<String>> first;

    AutomateReflection<Set<String>> second;

    @SuppressWarnings("unchecked")
    public ConcatAutomateReader(Automate first, Automate second) {
        first.init();
        second.init();

        AutomateRenamer.renameStates(first, second);

        this.first = new AutomateReflection(first);
        this.second = new AutomateReflection(second);
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

        String emptyState = AutomateOperationsUtil.getEmptyState(second);
        if (emptyState != null) {
            secondTable.remove(emptyState);
        }

        for (String state : transitions) {
            Map<String, Set<String>> newState = AutomateOperationsUtil.joinMap(firstTable.get(state),
                    secondTable.get(state), getAlphabet());
            table.put(state, newState);
        }


        Set<String> beginStates = second.getBeginState();
        beginStates.remove(emptyState);
        for (String endState : first.getEndStates()) {
            Map<String, Set<String>> endStateMap = new HashMap<>(table.get(endState));
            for (String beginState : beginStates) {
                Map<String, Set<String>> beginStateMap = secondTable.get(beginState);
                for (String signal : getAlphabet()) {
                    Set<String> states = endStateMap.get(signal);
                    if (states == null) {
                        states = new HashSet<>();
                    }
                    states.addAll(AutomateOperationsUtil.getState(beginStateMap.get(signal)));
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
    public List<String> getEndStates() {
        List<String> endState = new ArrayList<>(second.getEndStates());
        String emptyState = AutomateOperationsUtil.getEmptyState(second);
        if (emptyState != null) {
            endState.remove(emptyState);
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
}
