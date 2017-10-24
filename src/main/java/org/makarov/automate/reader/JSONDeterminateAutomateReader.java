package org.makarov.automate.reader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONDeterminateAutomateReader implements AutomateReader<String> {

    private JSONObject json;

    public JSONDeterminateAutomateReader(String fileName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(fileName);
            String content = IOUtils.toString(stream);
            json = new JSONObject(content);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<String> getAlphabet() {
        JSONArray alphabet = json.getJSONArray("alphabet");
        return jsonArrayToList(alphabet);
    }

    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> resultMap = new HashMap<>();

        JSONObject table = json.getJSONObject("table");
        List<String> columns = jsonArrayToList(table.getJSONArray("columns"));
        JSONArray rows = table.getJSONArray("rows");
        for (int i = 0; i < rows.length(); i++) {
            JSONObject metaRow = rows.getJSONObject(i);
            String rowName = metaRow.getString("rowName");
            Map<String, String> map = new HashMap<>();
            resultMap.put(rowName, map);
            List<String> row = jsonArrayToList(metaRow.getJSONArray("transitions"));
            for (int j = 0; j < columns.size(); j++) {
                String element = row.get(j);
                if (element.equals("-")) {
                    element = null;
                }
                map.put(columns.get(j), element);
            }
        }


        return resultMap;
    }

    public String getBeginState() {
        JSONArray beginState = json.getJSONArray("beginState");
        return beginState.getString(0);
    }

    public List<String> getEndState() {
        JSONArray endState = json.getJSONArray("endState");
        return jsonArrayToList(endState);
    }

    public String getName() {
        String name = json.getString("name");
        return name == null ? "" : name;
    }

    public int getPriority() {
        return json.getInt("priority");
    }

    private List<String> jsonArrayToList(JSONArray array) {
        List<Object> objects = array.toList();

        List<String> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add(object.toString());
        }

        return strings;
    }
}
