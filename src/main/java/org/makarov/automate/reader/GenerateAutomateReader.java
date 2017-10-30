package org.makarov.automate.reader;

import org.makarov.automate.Translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 30.10.2017.
 */
public class GenerateAutomateReader implements AutomateReader<String> {

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
        first.put(oneChar, "2");
        table.put("1", first);
        table.put("2", new HashMap<>());

        return table;
    }

    @Override
    public String getBeginState() {
        return "1";
    }

    @Override
    public List<String> getEndStates() {
        List<String> endStates = new ArrayList<>();
        endStates.add("2");

        return endStates;
    }

    @Override
    public String getName() {
        return "Empty";
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
