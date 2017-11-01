package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.TransformNonDeterministicAutomateReader;
import org.makarov.automate.reader.UnionAutomateReader;

public class AutomateOperations {

    public static Automate concat(Automate first, Automate second) {
        first = getNonDeterminateAutomate(first);
        second = getNonDeterminateAutomate(second);
        AutomateRenamer.renameStates(first, second);


        return null;
    }

    public static Automate union(Automate first, Automate second) {
        first = getNonDeterminateAutomate(first);
        second = getNonDeterminateAutomate(second);

        return new NonDeterministicAutomate(new UnionAutomateReader(first, second));
    }

    public static Automate repeat(Automate automate) {
        AutomateRenamer.renameAutomate(automate);

        return null;
    }

    private static Automate getNonDeterminateAutomate(Automate first) {
        if (first instanceof DeterministicAutomate) {
            return toNonDeterministicAutomate((DeterministicAutomate) first);
        } else {
            return first;
        }
    }

    private static NonDeterministicAutomate toNonDeterministicAutomate(DeterministicAutomate automate) {
        return new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader(automate));
    }
}
