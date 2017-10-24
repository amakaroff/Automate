package org.makarov.automate.reader;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class JSONAutomateReader<T> implements AutomateReader<T> {

    protected JSONObject json;

    protected String fileName;

    public JSONAutomateReader(String fileName) {
        try {
            this.fileName = fileName;
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

    public List<String> getEndStates() {
        JSONArray endState = json.getJSONArray("endState");
        return jsonArrayToList(endState);
    }

    public String getName() {
        if (json.isNull("name")) {
            int dotIndex = fileName.contains(".") ? fileName.lastIndexOf('.'): fileName.length();
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
            strings.add(object.toString());
        }

        return strings;
    }
}
