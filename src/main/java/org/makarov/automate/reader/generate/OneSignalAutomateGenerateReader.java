package org.makarov.automate.reader.generate;

import org.makarov.automate.reader.AutomateReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OneSignalAutomateGenerateReader implements AutomateReader<String> {

    private static final String BEGIN_POSITION = "1";

    private static final String END_POSITION = "2";

    private String character;

    public OneSignalAutomateGenerateReader(String character) {
        this.character = character;
    }

    @Override
    public List<String> getAlphabet() {
        List<String> alphabet = new ArrayList<>();
        alphabet.add(character);

        return alphabet;
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> table = new HashMap<>();
        Map<String, String> first = new HashMap<>();
        first.put(character, END_POSITION);
        table.put(BEGIN_POSITION, first);
        table.put(END_POSITION, new HashMap<>());

        return table;
    }

    @Override
    public String getBeginState() {
        return BEGIN_POSITION;
    }

    @Override
    public Set<String> getEndStates() {
        Set<String> endStates = new HashSet<>();
        endStates.add(END_POSITION);

        return endStates;
    }

    @Override
    public String getName() {
        return AutomateReader.ONE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
