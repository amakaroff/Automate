package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.parser.RegexParser;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        //System.out.println(LexerParser.getAutomates("lexic/lexer.lex"));

        Automate automate = RegexParser.parseRegex("12");
        System.out.println(automate.toString());
        System.out.println(Functions.function(automate, "12", 0));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
