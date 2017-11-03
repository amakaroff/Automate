package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.AutomateReflection;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateRenamer;
import org.makarov.util.optimization.AutomateOptimizationUtils;
import org.makarov.util.parser.RegexParser;
import org.makarov.util.transformer.AutomateTransformer;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("(+|-|\\?)(((\\d(\\d)*|\\?).\\d(\\d)*)|(\\d(\\d)*(.\\d(\\d)*|\\?)))");
        AutomateOptimizationUtils.verticalOptimization(automate);
        System.out.println(serializer.serialize(automate) + "\n\n");
        System.out.println(Functions.function(automate, "-.55", 0));

        DeterministicAutomate deterministicAutomate = AutomateTransformer.toDeterministicAutomateTransform(automate);
        System.out.println(serializer.serialize(deterministicAutomate));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
