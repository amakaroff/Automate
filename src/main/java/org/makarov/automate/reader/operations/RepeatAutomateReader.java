package org.makarov.automate.reader.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.operations.AutomateOperations;
import org.makarov.operations.AutomateOperationsUtils;
import org.makarov.operations.AutomateRenamer;
import org.makarov.automate.AutomateReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RepeatAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<Set<String>> automate;

    private String emptyState;

    public RepeatAutomateReader(Automate<Set<String>> automate) {
        automate.init();
        AutomateRenamer.renameAutomate(automate);

        this.automate = new AutomateReflection<>(automate);
        emptyState = AutomateOperationsUtils.getNextState(this.automate);
    }

    @Override
    public List<String> getAlphabet() {
        return new ArrayList<>(automate.getAlphabet());
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
        Map<String, Map<String, Set<String>>> table = new HashMap<>(automate.getTransitions());
        Set<String> beginStates = automate.getBeginState();
        Set<String> endStates = automate.getEndStates();

        for (String endState : endStates) {
            Map<String, Set<String>> map = table.get(endState);
            for (String letter : getAlphabet()) {
                Set<String> innerTransitions = AutomateOperationsUtils.getState(map.get(letter));

                for (String beginState : beginStates) {
                    Set<String> beginTransitions = AutomateOperationsUtils.getState(table.get(beginState).get(letter));
                    innerTransitions.addAll(beginTransitions);
                }
                map.put(letter, innerTransitions);
            }
        }

        for (String letter : getAlphabet()) {
            Map<String, Set<String>> transitions = new HashMap<>();
            transitions.put(letter, new HashSet<>());
            table.put(emptyState, transitions);
        }

        return table;
    }

    @Override
    public Set<String> getBeginState() {
        Set<String> beginState = new HashSet<>(automate.getBeginState());
        beginState.add(emptyState);
        return beginState;
    }

    @Override
    public Set<String> getEndStates() {
        Set<String> endStates = new HashSet<>(automate.getEndStates());
        endStates.add(emptyState);
        return endStates;
    }

    @Override
    public String getName() {
        return AutomateOperations.GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return automate.getPriority();
    }

    @Override
    public String getAlwaysSymbol() {
        return automate.getAlwaysSymbol();
    }
}
