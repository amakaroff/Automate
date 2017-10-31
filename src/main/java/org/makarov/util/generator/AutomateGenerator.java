package org.makarov.util.generator;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.EmptyAutomateGenerateReader;
import org.makarov.automate.reader.OneSignalAutomateGenerateReader;

public class AutomateGenerator {

    public static Automate generate(String name, String priority, String regex) {
        System.out.println("name: " + name + "| priority: " + priority + " | regex: " + regex);
        return null;
    }

    private static Automate generateRegularAutomate(String name, String priority, String regex) {

        return null;
    }

    private static Automate generateOneAutomate(String oneChar) {
        if (oneChar.isEmpty()) {
            return new DeterministicAutomate(new EmptyAutomateGenerateReader());
        } else {
            return new DeterministicAutomate(new OneSignalAutomateGenerateReader(oneChar));
        }
    }
}
