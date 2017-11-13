package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.translators.Translator;
import org.makarov.util.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public abstract class Automate<T> {

    protected String name;

    protected int priority;

    protected Map<String, Map<String, T>> table;

    protected T beginState;

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
        return "Automate {\n" +
                "\tName: " + name + ";\n" +
                "\tPriority: " + priority + ";\n" +
                "\tAlphabet: " + Functions.scheduleSymbols(alphabet.toString()) + ";\n" +
                "\tBegin state: " + beginState + ";\n" +
                "\tEnd state: " + endState + ";\n" +
                "\tTransitions: " + getTransitions() + ";\n}";
    }

    public boolean isInit() {
        return isInit;
    }

    private String getTransitions() {
        StringBuilder builder = new StringBuilder();
        int elementSize = getElementSize();
        int collectionSize = getCollectionSize();
        Set<String> states = new TreeSet<>(Functions.stringComparator);
        states.addAll(table.keySet());

        if (!alphabet.isEmpty()) {
            builder.append("\t\n\t")
                    .append(printEmptySpaces(elementSize + 3))
                    .append(printCollections(alphabet, collectionSize))
                    .append("\n");
        }
        if (!table.isEmpty()) {
            for (String key : states) {
                builder.append("\t")
                        .append(getElement(key, elementSize))
                        .append(printCollections(getStateList(key), collectionSize))
                        .append("\n");
            }

            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    private List<T> getStateList(String state) {
        List<T> list = new ArrayList<>();
        for (String letter : alphabet) {
            list.add(table.get(state).get(letter));
        }

        return list;
    }

    private int getElementSize() {
        int maxLength = 0;
        for (String state : table.keySet()) {
            if (state.length() > maxLength) {
                maxLength = state.length();
            }
        }

        return maxLength;
    }

    private String getElement(String line, int count) {
        return "[" + line + "]" + printEmptySpaces(count - line.length() + 1);
    }

    private String printEmptySpaces(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }

        return builder.toString();
    }

    private int getCollectionSize() {
        int size = 0;
        for (Map<String, T> map : table.values()) {
            for (T element : map.values()) {
                if (element != null && String.valueOf(element).length() > size) {
                    size = String.valueOf(element).length();
                }
            }
        }

        for (String letter : alphabet) {
            letter = Functions.scheduleSymbols(letter);
            if (letter.length() > size) {
                size = letter.length();
            }
        }

        return size;
    }

    private String printCollections(Collection collection, int collectionSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (Object element : collection) {
            if (element == null) {
                element = "-";
            }

            StringBuilder collectionBuilder = new StringBuilder(Functions.scheduleSymbols(String.valueOf(element)));
            builder.append(collectionBuilder.toString())
                    .append(printEmptySpaces(collectionSize - collectionBuilder.length()))
                    .append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }
}
