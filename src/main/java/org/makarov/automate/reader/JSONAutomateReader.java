package org.makarov.automate.reader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.automate.Translator;
import org.makarov.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class JSONAutomateReader<T> implements AutomateReader<T> {

    protected JSONObject json;

    protected String fileName;

    public JSONAutomateReader(String fileName) {
        this.fileName = fileName;
        json = new JSONObject(FileUtils.readFile(fileName));
    }

    public List<String> getAlphabet() {
        JSONArray alphabet = json.getJSONArray("alphabet");
        return jsonArrayToList(alphabet);
    }

    public List<String> getEndStates() {
        JSONArray endState = json.getJSONArray("endStates");
        return jsonArrayToList(endState);
    }

    public String getName() {
        if (json.isNull("name")) {
            int dotIndex = fileName.contains(".") ? fileName.lastIndexOf('.') : fileName.length();
            int slashIndex = fileName.contains("/") ? fileName.lastIndexOf('/') + 1 : 0;
            return fileName.substring(slashIndex, dotIndex);
        }
        String name = json.getString("name");
        return name == null ? "" : name;
    }

    public int getPriority() {
        return json.isNull("priority") ? 0 : json.getInt("priority");
    }

    protected List<String> jsonArrayToList(JSONArray array) {
        List<Object> objects = array.toList();

        List<String> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add(object == null ? null : String.valueOf(object));
        }

        return strings;
    }
}
