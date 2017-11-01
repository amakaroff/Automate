package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.OneSignalAutomateGenerateReader;
import org.makarov.automate.reader.TransformNonDeterministicAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("12", true);

        System.out.println(serializer.serialize(automate));
        System.out.println(Functions.function(automate, "12", 0));
    }
}
