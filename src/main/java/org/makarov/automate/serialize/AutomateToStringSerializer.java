package org.makarov.automate.serialize;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AutomateToStringSerializer implements AutomateSerializer {

    @Override

    public <T> String serialize(Automate<T> automate) {
        AutomateReflection<T> automateReflection = new AutomateReflection<>(automate);
        return "Automate {\n" +
                "\tName: " + automateReflection.getName() + ";\n" +
                "\tPriority: " + automateReflection.getPriority() + ";\n" +
                "\tAlphabet: " + Functions.scheduleSymbols(automateReflection.getAlphabet().toString()) + ";\n" +
                "\tBegin state: " + automateReflection.getBeginState() + ";\n" +
                "\tEnd state: " + automateReflection.getEndStates() + ";\n" +
                "\tTransitions: " + getTransitions(automateReflection) + ";\n}";
    }

    @SuppressWarnings("unchecked")
    private <T> String getTransitions(AutomateReflection<T> automateReflection) {
        StringBuilder builder = new StringBuilder();
        int elementSize = getElementSize(automateReflection);
        int collectionSize = getCollectionSize(automateReflection);
        Set<String> states = new TreeSet<>(Functions.stringComparator);
        states.addAll(automateReflection.getTransitions().keySet());

        if (!automateReflection.getAlphabet().isEmpty()) {
            builder.append("\t\n\t")
                    .append(printEmptySpaces(elementSize + 3))
                    .append(printCollections(automateReflection.getAlphabet(), collectionSize))
                    .append("\n");
        }
        if (!automateReflection.getTransitions().isEmpty()) {
            for (String key : states) {
                builder.append("\t")
                        .append(getElement(key, elementSize))
                        .append(printCollections(getStateList(automateReflection, key), collectionSize))
                        .append("\n");
            }

            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private <T> List<String> getStateList(AutomateReflection<T> automateReflection, String state) {
        List<String> list = new ArrayList<>();
        for (String letter : automateReflection.getAlphabet()) {
            list.add(automateReflection.getTransitions().get(state).get(letter).toString());
        }

        return list;
    }

    private <T> int getElementSize(AutomateReflection<T> automateReflection) {
        int maxLength = 0;
        for (String state : automateReflection.getTransitions().keySet()) {
            if (state.length() > maxLength) {
                maxLength = state.length();
            }
        }

        return maxLength;
    }

    private String getElement(String line, int count) {
        return "[" + line + "]" + printEmptySpaces(count - line.length() + 1);
    }

    private String printEmptySpaces(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }

        return builder.toString();
    }

    private <T> int getCollectionSize(AutomateReflection<T> automateReflection) {
        int size = 0;
        for (Map<String, T> map : automateReflection.getTransitions().values()) {
            for (T element : map.values()) {
                if (element != null && String.valueOf(element).length() > size) {
                    size = String.valueOf(element).length();
                }
            }
        }

        for (String letter : automateReflection.getAlphabet()) {
            letter = Functions.scheduleSymbols(letter);
            if (letter.length() > size) {
                size = letter.length();
            }
        }

        return size;
    }

    private String printCollections(Collection collection, int collectionSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (Object element : collection) {
            if (element == null) {
                element = "-";
            }

            StringBuilder collectionBuilder = new StringBuilder(Functions.scheduleSymbols(String.valueOf(element)));
            builder.append(collectionBuilder.toString())
                    .append(printEmptySpaces(collectionSize - collectionBuilder.length()))
                    .append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }
}
