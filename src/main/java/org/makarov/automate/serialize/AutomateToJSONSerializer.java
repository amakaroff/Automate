package org.makarov.automate.serialize;

import org.json.JSONObject;
import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Aleksei Makarov on 24.10.2017.
 */
public class AutomateToJSONSerializer implements AutomateSerializer {

    @Override
    @SuppressWarnings("unchecked")
    public String serialize(Automate automate) {
        Class<? extends Automate> clazz = automate.getClass();
        try {
            Field alphabetField = Automate.class.getDeclaredField("alphabet");
            alphabetField.setAccessible(true);

            Field beginStateField = Automate.class.getDeclaredField("beginState");
            beginStateField.setAccessible(true);

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
            object.put("priority", priority);
            object.put("alphabet", alphabet.toArray());
            object.put("endStates", endStates.toArray());

            if (clazz == DeterministicAutomate.class) {
                String beginState = (String) beginStateField.get(automate);
                object.put("beginState", beginState);
                Map<String, Map<String, String>> table = (Map<String, Map<String, String>>) tableField.get(automate);

                List<JSONObject> rows = new ArrayList<>();
                for (String transition : table.keySet()) {
                    JSONObject row = new JSONObject();
                    row.put("rowName", transition);

                    List<String> values = new ArrayList<>();
                    for (String signal : alphabet) {
                        values.add(table.get(transition).get(signal));
                    }

                    row.put("transitions", values.toArray());
                    rows.add(row);
                }

                object.put("table", rows.toArray());

                return object.toString();
            } else if (clazz == NonDeterministicAutomate.class) {
                Set<String> beginState = (Set<String>) beginStateField.get(automate);
                object.put("beginState", beginState.toArray());

                Map<String, Map<String, Set<String>>> table = (Map<String, Map<String, Set<String>>>) tableField.get(automate);

                List<JSONObject> rows = new ArrayList<>();
                for (String transition : table.keySet()) {
                    JSONObject row = new JSONObject();
                    row.put("rowName", transition);

                    List<Object[]> values = new ArrayList<>();
                    for (String signal : alphabet) {
                        values.add(table.get(transition).get(signal).toArray());
                    }

                    row.put("transitions", values.toArray());
                    rows.add(row);
                }

                object.put("table", rows.toArray());

                return object.toString();
            } else {
                throw new RuntimeException("Unknown automate!");
            }

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
