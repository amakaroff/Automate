package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.reader.JSONNonDeterministicAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;

public class Main {

    public static void main(String[] args) {
        Automate automate = new NonDeterministicAutomate(new JSONNonDeterministicAutomateReader("automate2.json"));
        automate.init();

        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
    }
}
