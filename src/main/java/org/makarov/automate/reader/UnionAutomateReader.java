package org.makarov.automate.reader;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateRenamer;

import java.util.*;

public class UnionAutomateReader implements AutomateReader<Set<String>> {

    private static final String GENERATE_NAME = "Generate automate";

    AutomateReflection<Set<String>> first;

    AutomateReflection<Set<String>> second;

    @SuppressWarnings("unchecked")
    public UnionAutomateReader(Automate first, Automate second) {
        if (first instanceof DeterministicAutomate) {
            first = new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader((DeterministicAutomate) first));
            first.init();
        }

        if (second instanceof DeterministicAutomate) {
            second = new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader((DeterministicAutomate) second));
            second.init();
        }

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
        table.putAll(first.getTransitions());
        table.putAll(second.getTransitions());
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
