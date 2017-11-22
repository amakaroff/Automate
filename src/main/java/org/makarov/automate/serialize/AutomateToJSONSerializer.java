package org.makarov.automate.serialize;

import org.json.JSONObject;
import org.makarov.automate.Automate;
import org.makarov.automate.AutomateReflection;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.JSONTranslator;
import org.makarov.automate.translators.Translator;
import org.makarov.operations.AutomateOperationsUtils;
import org.makarov.util.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AutomateToJSONSerializer implements AutomateSerializer {

    private static boolean addBeginState(JSONObject object, Object element) {
        if (element instanceof Collection) {
            Collection<String> collection = AutomateOperationsUtils.toStringsCollection(element);
            object.put(AutomateReader.BEGIN_STATES, (new TreeSet<>(collection)).toArray());
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
                return new ArrayList<>().toArray();
            } else {
                return ((Collection) object).toArray();
            }
        }
    }

    @Override
    public <T> String serialize(Automate<T> automate) {
        automate.init();
        AutomateReflection<T> automateReflection = new AutomateReflection<>(automate);

        String name = automate.getName();
        int priority = automate.getPriority();
        String alwaysSymbol = automateReflection.getAlwaysSymbol();
        Translator translator = automateReflection.getTranslator();
        List<String> alphabet = automateReflection.getAlphabet();

        Set<String> endStates = new TreeSet<>(Functions.stringComparator);
        endStates.addAll(automateReflection.getEndStates());

        JSONObject object = new JSONObject();

        object.put(AutomateReader.NAME, name);
        object.put(AutomateReader.PRIORITY, priority);

        if (!Translator.ALWAYS_SYMBOL.equals(alwaysSymbol)) {
            object.put(AutomateReader.ALWAYS_SYMBOL, alwaysSymbol);
        }

        if (translator != null && !(translator instanceof JSONTranslator)) {
            object.put(AutomateReader.TRANSLATOR, translator.getClass().getName());
        }

        object.put(AutomateReader.PRIORITY, priority);
        object.put(AutomateReader.ALPHABET, alphabet.toArray());
        object.put(AutomateReader.END_STATES, endStates.toArray());

        boolean isDeterminate = addBeginState(object, automateReflection.getBeginState());

        Map<String, Map<String, T>> table = automateReflection.getTransitions();

        List<JSONObject> rows = new ArrayList<>();
        Set<String> states = new TreeSet<>(Functions.stringComparator);
        states.addAll(table.keySet());

        for (String transition : states) {
            JSONObject row = new JSONObject();
            row.put(AutomateReader.ROW_NAME, transition);

            List<Object> values = new ArrayList<>();
            for (String signal : alphabet) {
                Map<String, T> map = table.get(transition);
                values.add(getObjectToJsonValue(map.get(signal), isDeterminate));
            }

            row.put(AutomateReader.TRANSITIONS, values.toArray());
            rows.add(row);
        }

        object.put(AutomateReader.TABLE, rows.toArray());

        return object.toString();
    }
}
