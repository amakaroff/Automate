package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;

public class Main {

    public static void main(String[] args) {
        //System.out.println(LexerParser.getAutomates("lexic/lexer.lex"));
        Automate automate = new DeterministicAutomate(new JSONDeterminateAutomateReader("test.json"));
        automate.init();
        Functions.function(automate, "abc  \t\t\n cd 4 5 6 % 1 !", 0);
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
    }
}
