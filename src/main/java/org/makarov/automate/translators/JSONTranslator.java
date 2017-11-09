package org.makarov.automate.translators;

import org.json.JSONArray;
import org.json.JSONObject;
import org.makarov.automate.translators.constants.RegexConstants;
import org.makarov.util.json.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONTranslator implements Translator {

    private static final String SYMBOL = "symbol";

    private static final String TRANSLATION = "translation";

    private Translator translator;

    private static Map<String, List<String>> translators = new HashMap<>();

    public JSONTranslator(JSONArray array) {
        this();
        List<Map> translations = JSONUtils.toList(array, Map.class);
        for (Map translation : translations) {
            String symbol = String.valueOf(translation.get(SYMBOL));
            @SuppressWarnings("unchecked")
            List<String> translationSymbols = (List<String>) translation.get(TRANSLATION);
            if (!translators.containsKey(symbol)) {
                translators.put(symbol, getTranslationSymbols(translationSymbols));
            } else {
                translators.get(symbol).addAll(getTranslationSymbols(translationSymbols));
            }
        }
    }

    public JSONTranslator() {
        translator = new DefaultTranslator();
    }

    @Override
    public List<String> getTranslateElements(String character) {
        List<String> strings = translators.get(character);
        if (strings == null) {
            return translator.getTranslateElements(character);
        }

        return strings;
    }

    private List<String> getTranslationSymbols(List<String> translationSymbols) {
        if (translationSymbols.size() == 1) {
            String line = translationSymbols.get(0);
            if (line.length() == 3 && line.charAt(1) == '-') {
                List<String> list = new ArrayList<>();
                fillListOfSymbols(list, line.charAt(0), line.charAt(2));
                return list;
            }
        }

        return translationSymbols;
    }
}
