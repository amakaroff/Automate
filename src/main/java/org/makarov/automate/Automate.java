package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;

import java.util.List;
import java.util.Map;


public abstract class Automate<T> {

    protected Map<String, Map<String, T>> table;

    protected T beginState;

    protected List<String> endState;

    protected List<String> alphabet;

    protected T currentState;

    protected AutomateReader<T> reader;

    public Automate(AutomateReader<T> reader) {
        this.reader = reader;
    }

    public void init() {
        table = reader.getTable();
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
}
