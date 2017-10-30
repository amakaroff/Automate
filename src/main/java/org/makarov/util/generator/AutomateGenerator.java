package org.makarov.util.generator;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.GenerateAutomateReader;

public class AutomateGenerator {

    public static Automate generate(String name, String priority, String regex) {
        System.out.println("name: " + name + "| priority: " + priority + " | regex: " + regex);
        return null;
    }

    private static Automate generateRegularAutomate(String name, String priority, String regex) {

        return null;
    }

    private static Automate generateOneAutomate(String oneChar) {
        return new DeterministicAutomate(new GenerateAutomateReader(oneChar));
    }
}
