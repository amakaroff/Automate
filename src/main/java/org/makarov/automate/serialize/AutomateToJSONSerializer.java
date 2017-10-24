package org.makarov.automate.serialize;

import org.json.JSONObject;
import org.makarov.automate.Automate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AutomateToJSONSerializer implements AutomateSerializer {

    private static void addBeginState(JSONObject object, Object element) {
        if (element instanceof Collection) {
            object.put("beginStates", ((Collection) element).toArray());
        } else {
            object.put("beginState", element);
        }
    }

    private static Object getObjectToJsonValue(Object object) {
        if (object instanceof Collection) {
            return ((Collection) object).toArray();
        } else {
            return object;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String serialize(Automate automate) {
        try {
            Field alphabetField = Automate.class.getDeclaredField("alphabet");
            alphabetField.setAccessible(true);

            Field beginStateField = Automate.class.getDeclaredField("beginState");
            beginStateField.setAccessible(true);

            Field translatorField = Automate.class.getDeclaredField("translator");
            translatorField.setAccessible(true);

            Field endStateField = Automate.class.getDeclaredField("endState");
            endStateField.setAccessible(true);

            Field tableField = Automate.class.getDeclaredField("table");
            tableField.setAccessible(true);

            String name = automate.getName();
            int priority = automate.getPriority();
            List<String> alphabet = (List<String>) alphabetField.get(automate);
            List<String> endStates = (List<String>) endStateField.get(automate);

            JSONObject object = new JSONObject();

            object.put("name", name);
            if (priority != 0) {
                object.put("priority", priority);
            }
            object.put("alphabet", alphabet.toArray());
            object.put("endStates", endStates.toArray());
            Class<?> translator = (Class<?>) translatorField.get(automate);
            if (translator != null) {
                object.put("translator", translator.getName());
            }

            addBeginState(object, beginStateField.get(automate));

            Map<String, Map<String, Object>> table = (Map<String, Map<String, Object>>) tableField.get(automate);

            List<JSONObject> rows = new ArrayList<>();
            for (String transition : table.keySet()) {
                JSONObject row = new JSONObject();
                row.put("rowName", transition);

                List<Object> values = new ArrayList<>();
                for (String signal : alphabet) {
                    values.add(getObjectToJsonValue(table.get(transition).get(signal)));
                }

                row.put("transitions", values.toArray());
                rows.add(row);
            }

            object.put("table", rows.toArray());

            return object.toString();
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

}
