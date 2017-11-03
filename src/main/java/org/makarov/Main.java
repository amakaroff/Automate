package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.parser.RegexParser;
import org.makarov.util.transformer.AutomateTransformer;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("lambda | if | cond | define");
        DeterministicAutomate deterministicAutomate = AutomateTransformer.toDeterministicAutomateTransform(automate);
        System.out.println(serializer.serialize(deterministicAutomate));
        System.out.println(Functions.function(deterministicAutomate, "lambda", 0));
        System.out.println(Functions.function(deterministicAutomate, "if", 0));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
