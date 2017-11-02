package org.makarov.util.json;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(JSONArray array, Class<T> clazz) {
        List list =  array.toList();
        List<T> result = new ArrayList<>();
        for (Object object : list) {
            if (clazz.isInstance(object)) {
                result.add((T) object);
            }
        }

        return result;
    }
}
