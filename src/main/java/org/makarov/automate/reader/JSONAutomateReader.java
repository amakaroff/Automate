package org.makarov.automate.reader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.task.translators.Translator;
import org.makarov.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class JSONAutomateReader<T> implements AutomateReader<T> {

    protected JSONObject json;

    protected String fileName;

    public JSONAutomateReader(String fileName) {
        if (!fileName.endsWith(TYPE)) {
            fileName += TYPE;
        }
        this.fileName = fileName;
        json = new JSONObject(FileUtils.readFile(fileName));
    }

    public String getAlwaysSymbol() {
        if (json.isNull(ALWAYS_SYMBOL)) {
            return "\\.";
        } else {
            return json.getString(ALWAYS_SYMBOL);
        }
    }

    public List<String> getAlphabet() {
        JSONArray alphabet = json.getJSONArray(ALPHABET);
        return jsonArrayToList(alphabet);
    }

    public List<String> getEndStates() {
        JSONArray endState = json.getJSONArray(END_STATES);
        return jsonArrayToList(endState);
    }

    public String getName() {
        if (json.isNull(NAME)) {
            int dotIndex = fileName.contains(".") ? fileName.lastIndexOf('.') : fileName.length();
            int slashIndex = fileName.contains("/") ? fileName.lastIndexOf('/') + 1 : 0;
            return fileName.substring(slashIndex, dotIndex);
        }
        String name = json.getString(NAME);
        return name == null ? EMPTY_NAME : name;
    }

    public int getPriority() {
        return json.isNull(PRIORITY) ? 0 : json.getInt(PRIORITY);
    }

    protected List<String> jsonArrayToList(JSONArray array) {
        List<Object> objects = array.toList();

        List<String> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add(object == null ? null : String.valueOf(object));
        }

        return strings;
    }

    @Override
    public Translator getTranslator() {
        if (!json.isNull(TRANSLATOR)) {
            String translator = json.getString(TRANSLATOR);
            try {
                Class<?> clazz = Class.forName(translator);
                if (Translator.class.isAssignableFrom(clazz)) {
                    return Translator.class.cast(clazz.newInstance());
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignore) {
                return null;
            }
        }

        return null;
    }
}
