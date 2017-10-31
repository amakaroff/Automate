package org.makarov.util.generator;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.EmptyAutomateGenerateReader;
import org.makarov.automate.reader.OneSignalAutomateGenerateReader;
import org.makarov.util.AutomateReflection;
import org.makarov.util.parser.RegexParser;

public class AutomateGenerator {

    public static Automate generate(String name, String priority, String regex) {
        System.out.println("name: " + name + "| priority: " + priority + " | regex: " + regex);
        Automate automate = RegexParser.parseRegex(regex);

        return null;
    }

    private static Automate generateRegularAutomate(String name, String priority, String regex) {

        return null;
    }
}
