package org.makarov;


import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("(A | B)**");
        System.out.println(serializer.serialize(automate));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
