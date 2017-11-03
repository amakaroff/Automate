package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.parser.RegexParser;
import org.makarov.util.transformer.AutomateTransformer;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
    //  Automate automate = RegexParser.parseRegex("(+|-|\\?)(\\d)(\\d)*"); //errors
        Automate automate = RegexParser.parseRegex("+ | -"); //errors
        System.out.println(serializer.serialize(automate));
        System.out.println(Functions.function(automate, "+", 0));
        System.out.println(serializer.serialize(AutomateTransformer.toDeterministicAutomateTransform(automate)));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
