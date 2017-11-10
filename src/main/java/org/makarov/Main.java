package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.AutomateReflection;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.operations.AutomateRenamer;
import org.makarov.util.optimization.AutomateOptimizationUtils;
import org.makarov.util.parser.LexerParser;
import org.makarov.util.parser.RegexParser;
import org.makarov.util.transformer.AutomateTransformer;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        //System.out.println(LexerParser.getAutomates("lexic/lexer.lex"));

        Automate automate = new DeterministicAutomate("testing.json");
        System.out.println(automate.toString());
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
