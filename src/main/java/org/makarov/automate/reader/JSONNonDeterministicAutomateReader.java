package org.makarov.automate.reader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

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
                Set<String> row = new HashSet<>(jsonArrayToList(transitions.getJSONArray(j)));
                Set<String> newRow = new HashSet<>();
                newRow.addAll(row);

                map.put(alphabet.get(j), newRow);
            }
        }


        return resultMap;
    }

    public Set<String> getBeginState() {
        JSONArray beginState = json.getJSONArray(BEGIN_STATES);
        return jsonArrayToSet(beginState);
    }

    private Set<String> jsonArrayToSet(JSONArray array) {
        return new HashSet<>(jsonArrayToList(array));
    }
}
