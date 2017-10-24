package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.task.TaskTree;
import org.makarov.util.AutomateOperations;
import org.makarov.util.AutomateReflection;
import org.makarov.util.FileUtils;
import org.makarov.util.Pair;

public class Main {

    public static void main(String[] args) {
        Automate automate = new DeterministicAutomate(new JSONDeterminateAutomateReader("three/Real1.json"));
        automate.init();
        AutomateOperations.renameAutomate(new AutomateReflection(automate));
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
    }
}
