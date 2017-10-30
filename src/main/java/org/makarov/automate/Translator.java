package org.makarov.automate;

public interface Translator {

    String translate(String signal);

    String translate(String signal, boolean isAll);
}
