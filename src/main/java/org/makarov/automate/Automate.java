package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.DefaultRegexTranslator;
import org.makarov.automate.translators.Translator;

import java.util.ArrayList;
import java.util.Collection;
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

    protected boolean isInit = false;

    protected Translator translator;

    protected String alwaysSymbol;

    protected boolean debug = false;

    public Automate(AutomateReader<T> reader) {
        this.reader = reader;
    }

    public void init() {
        init(false);
    }

    public void init(boolean debug) {
        if (!isInit) {
            try {
                this.debug = debug;
                log("\nInitializating of Automate is started!");

                name = reader.getName();
                log("Automate name: %s", name);

                table = reader.getTable();
                log("Automate transitions: %s", table);

                priority = reader.getPriority();
                log("Automate priority: %s", priority);

                beginState = reader.getBeginState();
                log("Automate begin state: %s", beginState);

                endState = reader.getEndStates();
                log("Automate end state: %s", endState);

                alphabet = reader.getAlphabet();
                log("Automate alphabet: %s", alphabet);

                alwaysSymbol = reader.getAlwaysSymbol();
                log("Automate always symbol: %s", alwaysSymbol);

                translator = reader.getTranslator();
                log("Translator for Automate: %s", (translator == null ? null : translator.getClass().getName()));
                if (translator == null) {
                    translator = new DefaultRegexTranslator();
                    log("Translator for Automate is degault: %s", translator.getClass().getName());
                }
                currentState = beginState;
                isInit = true;
                parseAlphabet();
                log("Initializating of Automate: %s complete!\n", name);
                log("Current state is %s", currentState);
            } catch (Exception exception) {
                throw new AutomateException("Problem at reading automate: " + name, exception);
            }
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
        log("Automate %s has been refreshed!\n", this.name);
        currentState = beginState;
        log("Current state is %s", currentState);
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    protected void checkNext(String currentSignal) {
        if ((!alphabet.contains(currentSignal) && !alphabet.contains(alwaysSymbol)) || (!(currentState instanceof Collection) && table.get(currentState) == null)) {
            log("Signal: {%s} is can't complete next", currentSignal);
            throw new AutomateException();
        }
    }

    private void parseAlphabet() {
        log("Parsing alphabet is started!");
        List<String> tempAlphabet = new ArrayList<>(alphabet);
        for (String letter : tempAlphabet) {
            log("Parsing of letter: %s", letter);
            List<String> translations = translator.getTranslateElements(letter);
            log("Maps of letters:  %s", translations);
            if (translations != null) {
                int index = alphabet.indexOf(letter);
                alphabet.remove(index);
                for (String translation : translations) {
                    alphabet.add(index, translation);
                    index++;
                }

                for (String key : table.keySet()) {
                    Map<String, T> map = table.get(key);
                    T value = map.get(letter);
                    map.remove(letter);
                    for (String translation : translations) {
                        map.put(translation, value);
                    }
                }
            }
        }

        log("Parsing alphabet is finished!");
    }

    protected void log(String message, Object... params) {
        if (debug) {
            System.out.println(String.format(message, params));
        }
    }

    @Override
    public String toString() {
        return "Name: {" + name + "}, alphabet: " + alphabet;
    }
}
