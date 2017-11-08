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


public class RepeatAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<Set<String>> automate;

    private String emptyState;

    @SuppressWarnings("unchecked")
    public RepeatAutomateReader(Automate automate) {
        automate.init();
        AutomateRenamer.renameAutomate(automate);

        this.automate = new AutomateReflection(automate);
        emptyState = getNextState(this.automate);
    }

    @Override
    public List<String> getAlphabet() {
        return new ArrayList<>(automate.getAlphabet());
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
        Map<String, Map<String, Set<String>>> table = new HashMap<>(automate.getTransitions());
        Set<String> beginStates = automate.getBeginState();
        List<String> endStates = automate.getEndStates();

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
        Set<String> beginState = automate.getBeginState();
        beginState.add(emptyState);
        return new HashSet<>(beginState);
    }

    @Override
    public List<String> getEndStates() {
        List<String> endStates = automate.getEndStates();
        endStates.add(emptyState);
        return new ArrayList<>(endStates);
    }

    @Override
    public String getName() {
        return AutomateOperations.GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private String getNextState(AutomateReflection<Set<String>> automate) {
        Set<String> strings = automate.getTransitions().keySet();
        int maxValue = 0;
        for (String state : strings) {
            int numberState = Integer.valueOf(state);
            if (numberState > maxValue) {
                maxValue = numberState;
            }
        }

        return String.valueOf(maxValue + 1);
    }
}
