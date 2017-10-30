package org.makarov.automate.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateAutomateReader implements AutomateReader<String> {

    private static final String BEGIN_POSITION = "1";

    private static final String END_POSITION = "2";

    private String oneChar;

    public GenerateAutomateReader(String oneChar) {
        this.oneChar = oneChar;
    }

    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        alphabet.add(oneChar);

        return alphabet;
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> table = new HashMap<>();
        Map<String, String> first = new HashMap<>();
        first.put(oneChar, END_POSITION);
        table.put(BEGIN_POSITION, first);
        table.put(END_POSITION, new HashMap<>());

        return table;
    }

    @Override
    public String getBeginState() {
        return BEGIN_POSITION;
    }

    @Override
    public List<String> getEndStates() {
        List<String> endStates = new ArrayList<>();
        endStates.add(END_POSITION);

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
