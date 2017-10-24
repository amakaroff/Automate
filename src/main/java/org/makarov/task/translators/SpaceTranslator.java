package org.makarov.task.translators;

import org.makarov.automate.Translator;

/**
 * Created by Aleksei Makarov on 24.10.2017.
 */
public class SpaceTranslator implements Translator {

    @Override
    public String translate(char signal) {
        if (signal == '\r' || signal == '\n' || signal == '\t' || signal == ' ') {
           return "S";
        } else {
            return String.valueOf(signal);
        }
    }
}
