package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("(+|-|\\?)(.\\d(\\d)*((e|E)(+|-|\\?)\\d(\\d)*|\\?))|(\\d(\\d)*.\\d(\\d)*((e|E)(+|-|\\?)\\d(\\d)*|\\?))|(\\d(\\d)*((e|E)(+|-|\\?)\\d(\\d)*|\\?))|(\\d(\\d)*.)((e|E)(+|-|\\?)\\d(\\d)*|\\?)");
        System.out.println(serializer.serialize(automate));
        System.out.println(Functions.function(automate, "-.53e-3", 0));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
