package org.makarov.task;


import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterministicAutomateReader;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

public class TaskTree {

    public static Collection<Pair<String, String>> getLexems(String line) {
        Collection<Automate> automates = new ArrayList<>();
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/CloseBkt.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/OpenBkt.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/KeyWord.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/Int1.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/Real1.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/Space.json")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("three/Identify.json")));

        return Functions.getLexemes(automates, line, true);
    }
}
