package org.makarov.util;

import org.makarov.automate.Automate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class AutomateReflection<T> {

    private Automate<T> automate;

    public AutomateReflection(Automate<T> automate) {
        this.automate = automate;
    }

    public String getName() {
        return automate.getName();
    }

    public int getPriority() {
        return automate.getPriority();
    }

    @SuppressWarnings("unchecked")
    public T getBeginState() {
        return (T) getFieldValue("beginState");
    }

    @SuppressWarnings("unchecked")
    public List<String> getEndStates() {
        return (List<String>) getFieldValue("endState");
    }

    @SuppressWarnings("unchecked")
    public List<String> getAlphabet() {
        return (List<String>) getFieldValue("alphabet");
    }

    @SuppressWarnings("unchecked")
    public String getTranslatorName() {
        Object translator = getFieldValue("translator");
        if (translator == null) {
            return null;
        } else {
            return translator.getClass().getName();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, T>> getTransitions() {
        return (Map<String, Map<String, T>>) getFieldValue("table");
    }

    private Object getFieldValue(String fieldName) {
        try {
            Field declaredField = Automate.class.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(automate);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            System.err.println("Field [" + fieldName + "] - is not found!");
            throw new RuntimeException(exception);
        }
    }
}
