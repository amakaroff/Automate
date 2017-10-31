package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.TransformNonDeterministicAutomateReader;

public class AutomateOperations {

    public static Automate concat(Automate first, Automate second) {

        return null;
    }

    public static Automate union(Automate first, Automate second) {

        return null;
    }

    public static Automate repeat(Automate automate) {
        AutomateRenamer.renameAutomate(automate);

        return null;
    }

    public NonDeterministicAutomate toNonDeterministicAutomate(DeterministicAutomate automate) {
        return new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader(automate));
    }
}
