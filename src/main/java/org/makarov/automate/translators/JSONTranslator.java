package org.makarov.automate.translators;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.constants.RegexConstants;
import org.makarov.util.json.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONTranslator implements Translator {

    private static Map<String, List<String>> translators = new HashMap<>();

    private static final String SYBMOL = "symbol";

    private static final String TRANSLATION = "translation";

    public JSONTranslator(JSONArray array) {
        this();
        List<JSONObject> translations = JSONUtils.toList(array, JSONObject.class);
        for (JSONObject translation : translations) {
            String symbol = translation.getString(SYBMOL);
            List<String> translationSymbols = JSONUtils.toList(translation.getJSONArray(TRANSLATION), String.class);
            if (!translators.containsKey(symbol)) {
                translators.put(symbol, translationSymbols);
            } else {
                translators.get(symbol).addAll(translationSymbols);
            }
        }
    }

    public JSONTranslator() {
        init();
    }

    private void init() {
        List<String> letters = new ArrayList<>();
        fillListOfSymbols(letters, 'a', 'z');
        fillListOfSymbols(letters, 'A', 'Z');
        translators.put(RegexConstants.LETTER_SYMBOL, letters);

        List<String> numbers = new ArrayList<>();
        fillListOfSymbols(numbers, '0', '9');
        translators.put(RegexConstants.NUMBER_SYMBOL, numbers);

        List<String> spaces = new ArrayList<>();
        spaces.add(" ");
        spaces.add("\t");
        spaces.add("\r");
        spaces.add("\n");
        translators.put(RegexConstants.SPACE_SYMBOL, spaces);
    }

    @Override
    public List<String> getTranslateElements(String character) {
        return translators.get(character);
    }
}
