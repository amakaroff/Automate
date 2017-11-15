package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToStringSerializer;
import org.makarov.automate.translators.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Automate<T> {

    private static final AutomateSerializer serializer = new AutomateToStringSerializer();

    protected String name;

    protected int priority;

    protected Map<String, Map<String, T>> table;

    protected T beginState;

    protected Set<String> endState;

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
                log("\nInitialization of Automate is started!");
                name = reader.getName();
                priority = reader.getPriority();
                table = reader.getTable();
                beginState = reader.getBeginState();
                endState = reader.getEndStates();
                alphabet = reader.getAlphabet();
                alwaysSymbol = reader.getAlwaysSymbol();
                translator = reader.getTranslator();
                currentState = beginState;
                isInit = true;
                compileAlphabet();
                log("Initialization of Automate: %s complete!\n", name);
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
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    protected void checkNext(String currentSignal) {
        if ((!alphabet.contains(currentSignal) && !alphabet.contains(alwaysSymbol))) {
            log("Signal: {%s} is can't complete next", currentSignal);
            throw new AutomateException();
        }
    }

    private void compileAlphabet() {
        if (translator != null) {
            log("Parsing alphabet is started!");
            List<String> tempAlphabet = new ArrayList<>(alphabet);
            for (String letter : tempAlphabet) {
                List<String> translations = translator.getTranslateElements(letter);
                if (translations != null) {
                    log("Parsing of letter: %s", letter);
                    log("Maps of letters:  %s", translations);
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
    }

    protected void log(String message, Object... params) {
        if (debug) {
            System.out.println(String.format(message, params));
        }
    }

    @Override
    public String toString() {
        init();
        return serializer.serialize(this);
    }

    public boolean isInit() {
        return isInit;
    }
}
