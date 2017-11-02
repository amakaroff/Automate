package org.makarov.automate.reader;

import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.util.AutomateReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransformDeterministicAutomateReader implements AutomateReader<String> {

    private AutomateReflection<Set<String>> automateReflection;

    private Map<String, Map<String, String>> table = new HashMap<>();

    private String beginState;

    private List<String> endStates;

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


        return null;
    }
}
