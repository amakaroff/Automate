package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;

import java.util.List;
import java.util.Map;


public abstract class Automate<T> {

    protected Map<String, Map<String, T>> table;

    protected T beginState;

    protected String name;

    protected int priority;

    protected List<String> endState;

    protected List<String> alphabet;

    protected T currentState;

    protected AutomateReader<T> reader;

    public Automate(AutomateReader<T> reader) {
        this.reader = reader;
    }

    public void init() {
        table = reader.getTable();
        name = reader.getName();
        priority = reader.getPriority();
        beginState = reader.getBeginState();
        endState = reader.getEndState();
        alphabet = reader.getAlphabet();
        currentState = beginState;

    }

    public abstract void nextState(String signal) throws AutomateException;

    public abstract boolean isEnd();

    public T getCurrentState() {
        return currentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Automate<?> automate = (Automate<?>) o;

        return !(name != null ? !name.equals(automate.name) : automate.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
