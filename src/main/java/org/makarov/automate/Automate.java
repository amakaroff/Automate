package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.DefaultRegexTranslator;
import org.makarov.automate.translators.Translator;

import java.util.ArrayList;
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
                if (this.debug) {
                    System.out.println("\nInitializating of Automate is started!");
                }

                name = reader.getName();
                if (this.debug) {
                    System.out.println("Automate name: " + name);
                }

                table = reader.getTable();
                if (this.debug) {
                    System.out.println("Automate transitions: " + table);
                }

                priority = reader.getPriority();
                if (this.debug) {
                    System.out.println("Automate priority: " + priority);
                }

                beginState = reader.getBeginState();
                if (this.debug) {
                    System.out.println("Automate begin state: " + beginState);
                }

                endState = reader.getEndStates();
                if (this.debug) {
                    System.out.println("Automate end state: " + endState);
                }

                alphabet = reader.getAlphabet();
                if (this.debug) {
                    System.out.println("Automate alphabet: " + alphabet);
                }

                alwaysSymbol = reader.getAlwaysSymbol();
                if (this.debug) {
                    System.out.println("Automate always symbol: " + alwaysSymbol);
                }

                translator = reader.getTranslator();
                if (this.debug && translator != null) {
                    System.out.println("Translator for Automate: " + translator.getClass().getName());
                }
                if (translator == null) {
                    translator = new DefaultRegexTranslator();
                    if (this.debug) {
                        System.out.println("Translator for Automate is degault: " + translator.getClass().getName() + "");
                    }
                }
                currentState = beginState;
                isInit = true;
                parseAlphabet();
                if (this.debug) {
                    System.out.println("Initializating of Automate: " + name + " complete!\n");
                }

                if (this.debug) {
                    System.out.println("Current state is " + currentState);
                }
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
        if (debug) {
            System.out.println("Automate has been refreshed!\n");
        }
        currentState = beginState;
        if (debug) {
            System.out.println("Current state is " + currentState);
        }
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    protected void checkNext(String currentSignal) {
        if ((!alphabet.contains(currentSignal) && !alphabet.contains(alwaysSymbol)) || table.get(currentState) == null) {
            if (debug) {
                System.out.println("Signal: " + currentSignal + " is can't complete next");
            }
            throw new AutomateException();
        }
    }

    private void parseAlphabet() {
        if (debug) {
            System.out.println("Parsing alphabet is started!");
        }
        List<String> tempAlphabet = new ArrayList<>(alphabet);
        for (String letter : tempAlphabet) {
            if (debug) {
                System.out.println("Parsing of letter: " + letter);
            }
            List<String> translations = translator.getTranslateElements(letter);
            if (debug) {
                System.out.println("Maps of letters: " + translations);
            }
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

        if (debug) {
            System.out.println("Parsing alphabet is finished!");
        }
    }
}
