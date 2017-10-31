package org.makarov.automate.serialize;

import org.json.JSONObject;
import org.makarov.automate.Automate;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.DefaultRegexTranslator;
import org.makarov.automate.translators.Translator;
import org.makarov.constants.RegexConstants;
import org.makarov.util.AutomateReflection;

import java.util.*;

public class AutomateToJSONSerializer implements AutomateSerializer {

    private static boolean addBeginState(JSONObject object, Object element) {
        if (element instanceof Collection) {
            object.put(AutomateReader.BEGIN_STATES, ((Collection) element).toArray());
            return false;
        } else {
            object.put(AutomateReader.BEGIN_STATE, element);
            return true;
        }
    }

    private static Object getObjectToJsonValue(Object object, boolean isDeterminate) {
        if (isDeterminate) {
            return object;
        } else {
            if (object == null) {
                List<Object> arrayList = new ArrayList<>();
                arrayList.add(null);
                return arrayList.toArray();
            } else {
                return ((Collection) object).toArray();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String serialize(Automate automate) {
        automate.init();
        AutomateReflection automateReflection = new AutomateReflection(automate);

        String name = automate.getName();
        int priority = automate.getPriority();
        String alwaysSymbol = automateReflection.getAlwaysSymbol();
        Translator translator = automateReflection.getTranslator();
        List<String> alphabet = automateReflection.getAlphabet();
        List<String> endStates = automateReflection.getEndStates();

        JSONObject object = new JSONObject();

        object.put(AutomateReader.NAME, name);
        object.put(AutomateReader.PRIORITY, priority);
        if (!RegexConstants.ALWAYS_SYMBOL.equals(alwaysSymbol)) {
            object.put(AutomateReader.ALWAYS_SYMBOL, alwaysSymbol);
        }
        if (!(translator instanceof DefaultRegexTranslator)) {
            object.put(AutomateReader.TRANSLATOR, translator.getClass().getName());
        }
        object.put(AutomateReader.PRIORITY, priority);
        object.put(AutomateReader.ALPHABET, alphabet.toArray());
        object.put(AutomateReader.END_STATES, endStates.toArray());

        boolean isDeterminate = addBeginState(object, automateReflection.getBeginState());

        Map<String, Map<String, Object>> table = (Map<String, Map<String, Object>>) automateReflection.getTransitions();

        List<JSONObject> rows = new ArrayList<>();
        for (String transition : table.keySet()) {
            JSONObject row = new JSONObject();
            row.put(AutomateReader.ROW_NAME, transition);

            List<Object> values = new ArrayList<>();
            for (String signal : alphabet) {
                values.add(getObjectToJsonValue(table.get(transition).get(signal), isDeterminate));
            }

            row.put(AutomateReader.TRANSITIONS, values.toArray());
            rows.add(row);
        }

        object.put(AutomateReader.TABLE, rows.toArray());

        return object.toString();
    }
}
