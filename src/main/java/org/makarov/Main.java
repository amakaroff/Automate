package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateRenamer;
import org.makarov.util.optimization.AutomateOptimizationUtils;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("(+|-|\\?)(((\\d(\\d)*|\\?).\\d(\\d)*)|(\\d(\\d)*(.\\d(\\d)*|\\?)))");
        System.out.println(serializer.serialize(automate) + "\n\n");
        AutomateOptimizationUtils.verticalOptimization(automate);
        System.out.println(serializer.serialize(automate) + "\n\n");
        AutomateRenamer.renameAutomate(automate);
        System.out.println(serializer.serialize(automate) + "\n\n");
        System.out.println(Functions.function(automate, "-.55", 0, true));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
