package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.OneSignalAutomateGenerateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.operations.AutomateRenamer;
import org.makarov.util.parser.RegexParser;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        /*
        Automate automate = new DeterministicAutomate("three/Identify");
        automate.init();
        AutomateRenamer.renameAutomate(automate);
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
        Automate automate = new DeterministicAutomate("three/Real1");
        automate.init();
        System.out.println(TaskTwo.findAllNumbers(automate, "5выф-5.в+.5e+5.5e.7cda-.3ee-5.e-3502e.3e-3eв3.E3.3."));*/
       /* AutomateSerializer serializer = new AutomateToJSONSerializer();

        Automate automate = new DeterministicAutomate(new OneSignalAutomateGenerateReader("\\w"));
        System.out.println(Functions.function(automate, "a", 0));
        System.out.println(Functions.function(automate, "1", 0));

        Automate automate2 = new DeterministicAutomate(new OneSignalAutomateGenerateReader("\\d"));
        System.out.println(Functions.function(automate2, "b", 0));

        System.out.println(Functions.function(automate2, "1", 0));

        Automate union = AutomateOperations.union(automate, automate2);
        System.out.println(serializer.serialize(union));

        System.out.println(Functions.function(union, "1", 0));
        System.out.println(Functions.function(union, "b", 0));*/
    }
}
