package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.translators.Translator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class AutomateReflection<T> {

    private Automate<T> automate;

    private Map<String, Map<String, T>> table;

    private List<String> alphabet;

    private T beginState;

    private List<String> endStates;

    public AutomateReflection(Automate<T> automate) {
        this.automate = automate;
        automate.init();
    }

    public String getAlwaysSymbol() {
        return getFieldValue("alwaysSymbol").toString();
    }

    public void setAlwaysSymbol(String value) {
        setFieldValue("alwaysSymbol", value);
    }

    public Translator getTranslator() {
        return (Translator) getFieldValue("translator");
    }

    public Automate<T> getAutomate() {
        return automate;
    }

    public String getName() {
        return automate.getName();
    }

    public void setName(String newName) {
        setFieldValue("name", newName);
    }

    public int getPriority() {
        return automate.getPriority();
    }

    public void setPriority(int newPriority) {
        setFieldValue("priority", newPriority);
    }

    @SuppressWarnings("unchecked")
    public T getBeginState() {
        if (beginState == null) {
            beginState = (T) getFieldValue("beginState");
        }
        return beginState;
    }

    public void setBeginState(T newBeginState) {
        setFieldValue("beginState", newBeginState);
    }

    @SuppressWarnings("unchecked")
    public List<String> getEndStates() {
        if (endStates == null) {
            endStates = (List<String>) getFieldValue("endState");
        }

        return endStates;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAlphabet() {
        if (alphabet == null) {
            alphabet = (List<String>) getFieldValue("alphabet");
        }
        return alphabet;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, T>> getTransitions() {
        if (table == null) {
            table = (Map<String, Map<String, T>>) getFieldValue("table");
        }
        return table;
    }

    private Object getFieldValue(String fieldName) {
        try {
            return getField(fieldName).get(automate);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setFieldValue(String fieldName, Object value) {
        try {
            getField(fieldName).set(automate, value);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Field getField(String fieldName) {
        try {
            Field declaredField = Automate.class.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException exception) {
            System.err.println("Field [" + fieldName + "] - is not found!");
            throw new RuntimeException(exception);
        }
    }
}
