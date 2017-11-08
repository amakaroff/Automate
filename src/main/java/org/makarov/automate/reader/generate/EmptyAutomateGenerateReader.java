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
        return new ArrayList<>();
    }

    @Override
    public Map<String, Map<String, String>> getTable() {
        return new HashMap<>();
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
