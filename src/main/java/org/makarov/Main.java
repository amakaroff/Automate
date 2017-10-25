package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.reader.JSONNonDeterministicAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.operations.AutomateRenamer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Automate automate = new NonDeterministicAutomate(new JSONNonDeterministicAutomateReader("automate2.json"));
        Automate automate2 = new DeterministicAutomate(new JSONDeterminateAutomateReader("automate3.json"));
        automate.init();
        automate2.init();
        //AutomateRenamer.renameStates(automate, automate2);
        AutomateRenamer.renameAutomate(automate);
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        //System.out.println(serializer.serialize(automate));
        //System.out.println();
        System.out.println(serializer.serialize(automate));
    }
}
