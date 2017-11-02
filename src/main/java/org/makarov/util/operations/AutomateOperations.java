package org.makarov.util.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.operations.ConcatAutomateReader;
import org.makarov.automate.reader.operations.RepeatAutomateReader;
import org.makarov.automate.reader.operations.UnionAutomateReader;
import org.makarov.util.transformer.AutomateTransformer;

public class AutomateOperations {

    public static final String GENERATE_NAME = "Generate automate";

    public static Automate concat(Automate first, Automate second) {
        first = AutomateTransformer.toNonDeterministicAutomateTransform(first);
        second = AutomateTransformer.toNonDeterministicAutomateTransform(second);

        return new NonDeterministicAutomate(new ConcatAutomateReader(first, second));
    }

    public static Automate union(Automate first, Automate second) {
        first = AutomateTransformer.toNonDeterministicAutomateTransform((first));
        second = AutomateTransformer.toNonDeterministicAutomateTransform((second));

        return new NonDeterministicAutomate(new UnionAutomateReader(first, second));
    }

    public static Automate repeat(Automate automate) {
        automate = AutomateTransformer.toNonDeterministicAutomateTransform((automate));

        return new NonDeterministicAutomate(new RepeatAutomateReader(automate));
    }
}
