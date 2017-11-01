package org.makarov.automate.reader;

import org.makarov.automate.Automate;
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


public class RepeatAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<Set<String>> automate;

    private String emptyState;

    @SuppressWarnings("unchecked")
    public RepeatAutomateReader(Automate automate) {
        automate.init();
        AutomateRenamer.renameAutomate(automate);

        this.automate = new AutomateReflection(automate);
        emptyState = AutomateOperationsUtil.getNextState(this.automate);
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

        AutomateOperationsUtil.repeatTransitionOperation(table, beginStates, endStates, getAlphabet());

        for (String letter : getAlphabet()) {
            HashMap<String, Set<String>> transitions = new HashMap<>();
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
}
