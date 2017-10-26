package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;
import org.makarov.task.translators.BasicTranslator;

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
            if (translator == null) {
                translator = reader.getTranslator();
            }
            currentState = beginState;

            unzipAutomate();
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

    private void unzipAutomate() {
        for (String symbol : alphabet) {
            List<String> newSymbols = BasicTranslator.getTranslationList(symbol);
            if (newSymbols != null) {
                for (String state : table.keySet()) {
                    Map<String, T> transitionsMap = table.get(state);
                    T states = transitionsMap.get(symbol);
                    transitionsMap.remove(symbol);
                    for (String newSymbol : newSymbols) {
                        transitionsMap.put(newSymbol, states);
                    }
                }
            }
        }
    }
}
