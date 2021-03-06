package org.makarov.automate.reader.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateReflection;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.operations.AutomateOperations;
import org.makarov.operations.AutomateOperationsUtils;
import org.makarov.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnionAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<Set<String>> first;

    private AutomateReflection<Set<String>> second;

    public UnionAutomateReader(Automate<Set<String>> first, Automate<Set<String>> second) {
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

        Set<String> transitions = new HashSet<>();
        transitions.addAll(first.getTransitions().keySet());
        transitions.addAll(second.getTransitions().keySet());

        Map<String, Map<String, Set<String>>> firstTable = first.getTransitions();
        Map<String, Map<String, Set<String>>> secondTable = second.getTransitions();

        for (String state : transitions) {
            Map<String, Set<String>> newState = AutomateOperationsUtils.joinMap(firstTable.get(state),
                    secondTable.get(state), getAlphabet());
            table.put(state, newState);
        }

        return table;
    }

    @Override
    public Set<String> getBeginState() {
        Set<String> beginStates = new HashSet<>();
        beginStates.addAll(first.getBeginState());
        beginStates.addAll(second.getBeginState());
        return beginStates;
    }

    @Override
    public Set<String> getEndStates() {
        Set<String> endStates = new HashSet<>();
        endStates.addAll(first.getEndStates());
        endStates.addAll(second.getEndStates());
        return endStates;
    }

    @Override
    public String getName() {
        return AutomateOperations.GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getAlwaysSymbol() {
        return AutomateOperationsUtils.getAlwaysSymbol(first, second);
    }
}
