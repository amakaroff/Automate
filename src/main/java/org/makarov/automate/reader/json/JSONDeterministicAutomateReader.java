package org.makarov.automate.reader.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONDeterministicAutomateReader extends JSONAutomateReader<String> {

    public JSONDeterministicAutomateReader(String fileName) {
        super(fileName);
    }

    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> resultMap = new HashMap<>();

        JSONArray table = json.getJSONArray(TABLE);
        List<String> columns = getAlphabet();
        for (int i = 0; i < table.length(); i++) {
            JSONObject metaRow = table.getJSONObject(i);
            String rowName = metaRow.getString(ROW_NAME);
            Map<String, String> map = new HashMap<>();
            resultMap.put(rowName, map);
            List<String> row = jsonArrayToList(metaRow.getJSONArray(TRANSITIONS));
            for (int j = 0; j < columns.size(); j++) {
                String element = row.get(j);
                map.put(columns.get(j), element);
            }
        }


        return resultMap;
    }

    public String getBeginState() {
        return json.getString(BEGIN_STATE);
    }
}
