package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Automate automate = RegexParser.parseRegex("((\\w | \\d | + | - | _ | %)(\\w | \\d | + | - | _ | % | !)*)| (\\|\\.(\\.)*\\|)");
        //Automate automate = RegexParser.parseRegex("((+ | -) ( (. | \\d(\\d)*) ) )|(. | \\d)");
        System.out.println(Functions.function(automate, "|asdad234k2347283h78w|", 0));
    }
}
