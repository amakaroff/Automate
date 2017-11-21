package org.makarov.automate.reader.generate;

import org.makarov.automate.reader.AutomateReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmptyAutomateGenerateReader implements AutomateReader<String> {

    private static final String POSITION = "1";

    public EmptyAutomateGenerateReader() {
    }

    @Override
    public List<String> getAlphabet() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> table = new HashMap<>();
        table.put(POSITION, new HashMap<>());
        return table;
    }

    @Override
    public String getBeginState() {
        return POSITION;
    }

    @Override
    public Set<String> getEndStates() {
        Set<String> endStates = new HashSet<>();
        endStates.add(POSITION);

        return endStates;
    }

    @Override
    public String getName() {
        return AutomateReader.EMPTY_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
