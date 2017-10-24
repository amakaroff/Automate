package org.makarov.task;


import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

public class TaskTree {

    public static Collection<Pair<String, String>> getLexems(String line) {
        Collection<Automate> automates = new ArrayList<>();
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/CloseBkt.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/OpenBkt.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/KeyWord.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/Int1.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/Real1.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/Space.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("three/Identify.json")));

        return Functions.getLexems(automates, line);
    }
}
