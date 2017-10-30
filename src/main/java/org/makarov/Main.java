package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.GenerateAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.task.translators.BasicTranslator;
import org.makarov.util.Functions;
import org.makarov.util.parser.LexerParser;

public class Main {

    public static void main(String[] args) {
        //System.out.println(LexerParser.getAutomates("lexic/lexer.lex", false));

        Automate automate = new DeterministicAutomate("util/regex.json");
        automate.init();
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
    }
}
