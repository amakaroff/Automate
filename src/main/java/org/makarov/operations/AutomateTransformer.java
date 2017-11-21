package org.makarov.operations;


import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.transform.TransformDeterministicAutomateReader;
import org.makarov.automate.reader.transform.TransformNonDeterministicAutomateReader;

public class AutomateTransformer {

    public static DeterministicAutomate toDeterministicAutomateTransform(Automate automate) {
        if (automate instanceof DeterministicAutomate) {
            return (DeterministicAutomate) automate;
        } else {
            return new DeterministicAutomate(new TransformDeterministicAutomateReader((NonDeterministicAutomate) automate));
        }
    }

    public static NonDeterministicAutomate toNonDeterministicAutomateTransform(Automate automate) {
        if (automate instanceof NonDeterministicAutomate) {
            return (NonDeterministicAutomate) automate;
        } else {
            return new NonDeterministicAutomate(new TransformNonDeterministicAutomateReader((DeterministicAutomate) automate));
        }
    }
}
