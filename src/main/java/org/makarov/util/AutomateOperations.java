package org.makarov.util;

import org.makarov.automate.Automate;

import java.util.Map;
import java.util.Set;

public class AutomateOperations {

    public static Automate concat(Automate first, Automate second) {

        return null;
    }

    public static Automate union(Automate first, Automate second) {

        return null;
    }

    public static Automate repeat(Automate automate, int count) {

        return null;
    }

    private static void renameStates(Automate first, Automate second) {
        AutomateReflection firstReflection = new AutomateReflection(first);
        AutomateReflection secondReflection = new AutomateReflection(second);

        Set firstSet = firstReflection.getTransitions().keySet();
        Set secondSet = secondReflection.getTransitions().keySet();

        if (firstSet.size() > secondSet.size()) {
            renamingStates(firstReflection, secondReflection);
        } else {
            renamingStates(secondReflection, firstReflection);
        }
    }

    private static void renamingStates(AutomateReflection first, AutomateReflection second) {


    }

    private static void prepareRenaming(AutomateReflection first, AutomateReflection second) {
        int firstSize = first.getTransitions().keySet().size();
        int secondSize = second.getTransitions().keySet().size();
        renameAutomate(first);
        renameAutomate(second);


    }

    public static void renameAutomate(AutomateReflection reflection) {
        int index = 1;
        for (Object object : reflection.getTransitions().keySet()) {
            String key = object.toString();
            rename(reflection, key, String.valueOf(index));
            index++;
        }
    }

    @SuppressWarnings("unchecked")
    private static void rename(AutomateReflection reflection, String oldState, String newState) {
        Map<String, Map<String, Object>> transitions = reflection.getTransitions();
        Map<String, Object> oldMap = transitions.get(oldState);
        renameInMap(oldMap, oldState, newState);
        transitions.put(newState, oldMap);
        transitions.remove(oldState);

        for (Map<String, Object> map : transitions.values()) {
            renameInMap(map, oldState, newState);
        }
    }

    private static void renameInMap(Map<String, Object> map, String oldState, String newState) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (oldState.equals(entry.getValue())) {
                map.replace(entry.getKey(), newState);
            }
        }
    }
}
