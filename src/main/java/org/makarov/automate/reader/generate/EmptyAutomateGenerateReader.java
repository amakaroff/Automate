package org.makarov.automate.reader.generate;

import org.makarov.automate.reader.AutomateReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmptyAutomateGenerateReader implements AutomateReader<String> {

    private static final String POSITION = "1";

    public EmptyAutomateGenerateReader() {
    }

    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        alphabet.add(getAlwaysSymbol());

        return alphabet;
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        Map<String, String> transitions = new HashMap<>();
        transitions.put(AutomateReader.ALWAYS_SYMBOL, null);
        Map<String, Map<String, String>> table = new HashMap<>();
        table.put(POSITION, transitions);

        return table;
    }

    @Override
    public String getBeginState() {
        return POSITION;
    }

    @Override
    public List<String> getEndStates() {
        List<String> list = new ArrayList<>();
        list.add(POSITION);

        return list;
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
