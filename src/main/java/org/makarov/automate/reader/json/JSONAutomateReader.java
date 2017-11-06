package org.makarov.automate.reader.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.JSONTranslator;
import org.makarov.automate.translators.Translator;
import org.makarov.automate.translators.constants.RegexConstants;
import org.makarov.util.FileUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
            return RegexConstants.ALWAYS_SYMBOL;
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
            int reverseSlashIndex = fileName.contains("\\") ? fileName.lastIndexOf('\\') + 1 : 0;
            int secondIndex = slashIndex > reverseSlashIndex ? slashIndex : reverseSlashIndex;
            return fileName.substring(secondIndex, dotIndex);
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
            JSONObject translator = json.getJSONObject(TRANSLATOR);
            String className = translator.getString(TRANSLATOR_CLASS_NAME);
            JSONArray translations = translator.getJSONArray(TRANSLATOR_TRANSLATION_ARRAY);

            try {
                Class<?> clazz = Class.forName(className);
                if (JSONTranslator.class.isAssignableFrom(clazz)) {
                    Constructor<?> constructor = clazz.getConstructor(JSONArray.class);
                    return Translator.class.cast(constructor.newInstance(translations));
                }
            } catch (ClassNotFoundException | IllegalAccessException |
                    InstantiationException | NoSuchMethodException | InvocationTargetException ignore) {
                return null;
            }
        }

        return null;
    }
}
