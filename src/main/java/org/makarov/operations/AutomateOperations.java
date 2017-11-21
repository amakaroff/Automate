package org.makarov.operations;

import org.makarov.automate.Automate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.operations.ConcatAutomateReader;
import org.makarov.automate.reader.operations.RepeatAutomateReader;
import org.makarov.automate.reader.operations.UnionAutomateReader;

public class AutomateOperations {

    public static final String GENERATE_NAME = "Generate automate";

    public static Automate concat(Automate first, Automate second) {
        NonDeterministicAutomate newFirst = AutomateTransformer.toNonDeterministicAutomateTransform(first);
        NonDeterministicAutomate newSecond = AutomateTransformer.toNonDeterministicAutomateTransform(second);

        return new NonDeterministicAutomate(new ConcatAutomateReader(newFirst, newSecond));
    }

    public static Automate union(Automate first, Automate second) {
        NonDeterministicAutomate newFirst = AutomateTransformer.toNonDeterministicAutomateTransform((first));
        NonDeterministicAutomate newSecond = AutomateTransformer.toNonDeterministicAutomateTransform((second));

        return new NonDeterministicAutomate(new UnionAutomateReader(newFirst, newSecond));
    }

    public static Automate repeat(Automate automate) {
        NonDeterministicAutomate newAutomate = AutomateTransformer.toNonDeterministicAutomateTransform((automate));

        return new NonDeterministicAutomate(new RepeatAutomateReader(newAutomate));
    }
}
