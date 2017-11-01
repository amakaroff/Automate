package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.ConcatAutomateReader;
import org.makarov.automate.reader.RepeatAutomateReader;
import org.makarov.automate.reader.TransformNonDeterministicAutomateReader;
import org.makarov.automate.reader.UnionAutomateReader;
import org.makarov.util.AutomateReflection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomateOperations {

    public static final String GENERATE_NAME = "Generate automate";

    public static Automate concat(Automate first, Automate second) {
        first = getNonDeterminateAutomate(first);
        second = getNonDeterminateAutomate(second);

        return new NonDeterministicAutomate(new ConcatAutomateReader(first, second));
    }

    public static Automate union(Automate first, Automate second) {
        first = getNonDeterminateAutomate(first);
        second = getNonDeterminateAutomate(second);

        return new NonDeterministicAutomate(new UnionAutomateReader(first, second));
    }

    public static Automate repeat(Automate automate) {
        automate = getNonDeterminateAutomate(automate);

        return new NonDeterministicAutomate(new RepeatAutomateReader(automate));
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
