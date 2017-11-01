package org.makarov.automate.reader;

import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.translators.Translator;
import org.makarov.util.AutomateReflection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransformNonDeterministicAutomateReader implements AutomateReader<Set<String>> {

    private AutomateReflection<String> automateReflection;

    @SuppressWarnings("unchecked")
    public TransformNonDeterministicAutomateReader(DeterministicAutomate automate) {
        this.automateReflection = new AutomateReflection(automate);
    }

    @Override
    public List<String> getAlphabet() {
        return automateReflection.getAlphabet();
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
        Map<String, Map<String, Set<String>>> table = new HashMap<>();
        Map<String, Map<String, String>> oldTable = automateReflection.getTransitions();
        for (Map.Entry<String, Map<String, String>> entry : oldTable.entrySet()) {
            Map<String, Set<String>> transitionsMap = new HashMap<>();
            for (Map.Entry<String, String> innerEntry : entry.getValue().entrySet()) {
                Set<String> transitions = new HashSet<>();
                if (innerEntry.getValue() != null) {
                    transitions.add(innerEntry.getValue());
                }
                transitionsMap.put(innerEntry.getKey(), transitions);
            }
            table.put(entry.getKey(), transitionsMap);
        }

        return table;
    }

    @Override
    public Set<String> getBeginState() {
        Set<String> beginStates = new HashSet<>();
        beginStates.add(automateReflection.getBeginState());

        return beginStates;
    }

    @Override
    public List<String> getEndStates() {
        return automateReflection.getEndStates();
    }

    @Override
    public String getName() {
        return automateReflection.getName();
    }

    @Override
    public int getPriority() {
        return automateReflection.getPriority();
    }

    @Override
    public String getAlwaysSymbol() {
        return automateReflection.getAlwaysSymbol();
    }

    @Override
    public Translator getTranslator() {
        return automateReflection.getTranslator();
    }
}
