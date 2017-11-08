package org.makarov.automate.reader.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.util.json.JSONUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONNonDeterministicAutomateReader extends JSONAutomateReader<Set<String>> {

    public JSONNonDeterministicAutomateReader(String fileName) {
        super(fileName);
    }

    public Map<String, Map<String, Set<String>>> getTable() {
        List<String> alphabet = getAlphabet();
        Map<String, Map<String, Set<String>>> resultMap = new HashMap<>();

        JSONArray table = json.getJSONArray(TABLE);
        for (int i = 0; i < table.length(); i++) {
            JSONObject metaRow = table.getJSONObject(i);
            String rowName = metaRow.getString(ROW_NAME);
            Map<String, Set<String>> map = new HashMap<>();
            resultMap.put(rowName, map);

            JSONArray transitions = metaRow.getJSONArray(TRANSITIONS);

            for (int j = 0; j < alphabet.size(); j++) {
                Set<String> row = new HashSet<>(JSONUtils.toList(transitions.getJSONArray(j), String.class));
                Set<String> newRow = new HashSet<>();
                newRow.addAll(row);

                map.put(alphabet.get(j), newRow);
            }
        }

        return resultMap;
    }

    public Set<String> getBeginState() {
        JSONArray beginState = json.getJSONArray(BEGIN_STATES);
        return new HashSet<>(JSONUtils.toList(beginState, String.class));
    }
}
