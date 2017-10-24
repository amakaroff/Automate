package org.makarov.automate.reader;

import org.makarov.automate.Translator;

import java.util.List;
import java.util.Map;

public interface AutomateReader<T> {

    List<String> getAlphabet();

    Map<String, Map<String, T>> getTable();

    T getBeginState();

    List<String> getEndStates();

    String getName();

    int getPriority();

    Translator getTranslator();
}
