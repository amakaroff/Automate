package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.TransformNonDeterministicAutomateReader;

public class AutomateOperations {

    public static Automate concat(Automate first, Automate second) {
       first = getCorrectFirstAutomate(first, second);
       second = getCorrectSecondAutomate(first, second);
       AutomateRenamer.renameStates(first, second);


        return null;
    }

    public static Automate union(Automate first, Automate second) {
        first = getCorrectFirstAutomate(first, second);
        second = getCorrectSecondAutomate(first, second);
        AutomateRenamer.renameStates(first, second);


        return null;
    }

    public static Automate repeat(Automate automate) {
        AutomateRenamer.renameAutomate(automate);

        return null;
    }

    private static Automate getCorrectFirstAutomate(Automate first, Automate second) {
        if (first instanceof DeterministicAutomate && second instanceof NonDeterministicAutomate) {
            return toNonDeterministicAutomate((DeterministicAutomate) first);
        } else {
            return first;
        }
    }

    private static Automate getCorrectSecondAutomate(Automate first, Automate second) {
        if (first instanceof NonDeterministicAutomate && second instanceof DeterministicAutomate) {
            return toNonDeterministicAutomate((DeterministicAutomate) second);
        } else {
            return first;
        }
    }

    private static NonDeterministicAutomate toNonDeterministicAutomate(DeterministicAutomate automate) {
        return new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader(automate));
    }
}
