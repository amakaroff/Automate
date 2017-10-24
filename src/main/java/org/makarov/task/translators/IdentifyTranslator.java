package org.makarov.task.translators;

import org.makarov.automate.Translator;

public class IdentifyTranslator implements Translator {

    @Override
    public String translate(char signal) {
        if (Character.isDigit(signal) ||
                Character.isLetter(signal) ||
                signal == '+' ||
                signal == '-' ||
                signal == '*' ||
                signal == '/' ||
                signal == '%' ||
                signal == '!' ||
                signal == '_') {
            return "C";
        }

        if (signal == '|') {
            return "|";
        }

        return "A";
    }
}
