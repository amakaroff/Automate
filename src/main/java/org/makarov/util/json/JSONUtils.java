package org.makarov.util.json;

import org.json.JSONArray;
import org.makarov.util.operations.AutomateOperationsUtils;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static <T> List<T> toList(JSONArray array, Class<T> clazz) {
        List list = array.toList();
        List<T> result = new ArrayList<>();
        for (Object object : list) {
            if (clazz.isInstance(object) || object == null) {
                result.add(AutomateOperationsUtils.geneticCastHack(object));
            }
        }

        return result;
    }
}
