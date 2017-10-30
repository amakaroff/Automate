package org.makarov.automate.reader;

import org.makarov.task.translators.Translator;

import java.util.List;
import java.util.Map;

public interface AutomateReader<T> {

    String DEBUG = "debug";

    String EMPTY_NAME = "Empty name";

    String TYPE = ".json";

    String ALPHABET = "alphabet";

    String ALWAYS_SYMBOL = "alwaysSymbol";

    String END_STATES = "endStates";

    String NAME = "name";

    String PRIORITY = "priority";

    String TABLE = "table";

    String ROW_NAME = "rowName";

    String TRANSITIONS = "transitions";

    String BEGIN_STATE = "beginState";

    String BEGIN_STATES = "beginStates";

    String TRANSLATOR = "translator";

    List<String> getAlphabet();

    Map<String, Map<String, T>> getTable();

    T getBeginState();

    default String getAlwaysSymbol() {
        return "\\.";
    }

    List<String> getEndStates();

    String getName();

    int getPriority();

    default Translator getTranslator() {
        return null;
    }
}
