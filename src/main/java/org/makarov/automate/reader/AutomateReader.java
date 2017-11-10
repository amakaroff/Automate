package org.makarov.automate.reader;

import org.makarov.automate.translators.DefaultTranslator;
import org.makarov.automate.translators.Translator;
import org.makarov.automate.translators.constants.RegexConstants;

import java.util.List;
import java.util.Map;

public interface AutomateReader<T> {

    String EMPTY_NAME = "Empty name";

    String ONE_NAME = "One symbol name";

    String TYPE = ".json";

    String ALPHABET = "alphabet";

    String ALWAYS_SYMBOL = "always-symbol";

    String END_STATES = "end-states";

    String NAME = "name";

    String PRIORITY = "priority";

    String TABLE = "table";

    String ROW_NAME = "row-name";

    String TRANSITIONS = "transitions";

    String BEGIN_STATE = "begin-state";

    String BEGIN_STATES = "begin-states";

    String TRANSLATOR = "translator";

    String TRANSLATOR_CLASS_NAME = "class-name";

    String TRANSLATOR_TRANSLATION_ARRAY = "translators";

    List<String> getAlphabet();

    Map<String, Map<String, T>> getTable();

    T getBeginState();

    default String getAlwaysSymbol() {
        return RegexConstants.ALWAYS_SYMBOL;
    }

    List<String> getEndStates();

    String getName();

    int getPriority();

    default Translator getTranslator() {
        return new DefaultTranslator();
    }
}
