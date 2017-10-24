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

    protected Translator translator;

    protected boolean isInit = false;

    public Automate(AutomateReader<T> reader) {
        this.reader = reader;
    }

    public Automate(AutomateReader<T> reader, Translator translator) {
        this.reader = reader;
        this.translator = translator;
    }

    public void init() {
        if (!isInit) {
            table = reader.getTable();
            name = reader.getName();
            priority = reader.getPriority();
            beginState = reader.getBeginState();
            endState = reader.getEndStates();
            alphabet = reader.getAlphabet();
            currentState = beginState;
            isInit = true;
        }
    }

    public abstract void nextState(char signal) throws AutomateException;

    public abstract boolean isEnd();

    public T getCurrentState() {
        return currentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Automate<?> automate = (Automate<?>) o;

        return priority == automate.priority &&
                (name != null ? name.equals(automate.name) : automate.name == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + priority;
        return result;
    }

    public void refresh() {
        currentState = beginState;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
