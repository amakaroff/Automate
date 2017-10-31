package org.makarov;

import org.makarov.task.TaskTree;
import org.makarov.util.FileUtils;
import org.makarov.util.Pair;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        Collection<Pair<String, String>> lexems = TaskTree.getLexems(FileUtils.readFile("code.txt"));
        for (Pair<String, String> lexem : lexems) {
            System.out.println(lexem);
        }
        /*
        Automate automate = new DeterministicAutomate("three/Identify");
        automate.init();
        AutomateRenamer.renameAutomate(automate);
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
        Automate automate = new DeterministicAutomate("three/Real1");
        automate.init();
        System.out.println(TaskTwo.findAllNumbers(automate, "5выф-5.в+.5e+5.5e.7cda-.3ee-5.e-3502e.3e-3eв3.E3.3."));*/
    }
}
