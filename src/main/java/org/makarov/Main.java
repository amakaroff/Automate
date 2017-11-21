package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.parser.LexerParser;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Collection<Automate> automates = LexerParser.getAutomates("lexic/lexer.lex");
        System.out.println(automates);
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
