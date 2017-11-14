package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.translators.Translator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomateReflection<T> {

    private Automate<T> automate;

    public AutomateReflection(Automate<T> automate) {
        this.automate = automate;
        automate.init();
    }

    public String getAlwaysSymbol() {
        return getFieldValue("alwaysSymbol").toString();
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
        return (T) getFieldValue("beginState");
    }

    public void setBeginState(T newBeginState) {
        setFieldValue("beginState", newBeginState);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getEndStates() {
        return (Set<String>) getFieldValue("endState");
    }

    @SuppressWarnings("unchecked")
    public List<String> getAlphabet() {
        return (List<String>) getFieldValue("alphabet");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, T>> getTransitions() {
        return (Map<String, Map<String, T>>) getFieldValue("table");
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
