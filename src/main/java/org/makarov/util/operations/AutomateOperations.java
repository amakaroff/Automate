package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.TransformAutomateReader;

public class AutomateOperations {

    public static Automate concat(Automate first, Automate second) {

        return null;
    }

    public static Automate union(Automate first, Automate second) {

        return null;
    }

    public static Automate repeat(Automate automate) {

        return null;
    }

    public NonDeterministicAutomate toNonDeterministicAutomate(DeterministicAutomate automate) {
        return new NonDeterministicAutomate(new TransformAutomateReader(automate));
    }
}
