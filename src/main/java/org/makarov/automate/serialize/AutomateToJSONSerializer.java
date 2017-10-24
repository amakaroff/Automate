package org.makarov.automate.serialize;

import org.json.JSONObject;
import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;

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
        AutomateReflection automateReflection = new AutomateReflection(automate);

        String name = automate.getName();
        int priority = automate.getPriority();
        List<String> alphabet = automateReflection.getAlphabet();
        List<String> endStates = automateReflection.getEndStates();

        JSONObject object = new JSONObject();

        object.put("name", name);
        if (priority != 0) {
            object.put("priority", priority);
        }
        object.put("alphabet", alphabet.toArray());
        object.put("endStates", endStates.toArray());

        object.put("translator", automateReflection.getTranslatorName());

        addBeginState(object, automateReflection.getBeginState());

        Map<String, Map<String, Object>> table = (Map<String, Map<String, Object>>) automateReflection.getTransitions();

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
    }

}
