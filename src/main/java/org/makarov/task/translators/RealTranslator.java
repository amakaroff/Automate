package org.makarov.task.translators;

import org.makarov.automate.Translator;

/**
 * Created by Aleksei Makarov on 24.10.2017.
 */
public class RealTranslator implements Translator {

    @Override
    public String translate(char signal) {
        if (Character.isDigit(signal)) {
            return "N";
        }

        if (signal == '+' || signal == '-') {
            return "S";
        }

        if (signal == '.') {
            return "P";
        }

        if (signal == 'e' || signal == 'E') {
            return "E";
        }

        return null;
    }
}