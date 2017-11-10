package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.optimization.AutomateOptimizationUtils;
import org.makarov.util.parser.RegexParser;
import org.makarov.util.transformer.AutomateTransformer;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        //System.out.println(LexerParser.getAutomates("lexic/lexer.lex"));

        Automate automate = RegexParser.parseRegex("(+|-|\\?)((\\d(\\d*)) | (\\d(\\d*)). | .(\\d(\\d*)) | (\\d(\\d*)).(\\d(\\d*)) )(\\? | ((e|E)(+|-|\\?) (\\d(\\d*))))");
        automate = AutomateTransformer.toDeterministicAutomateTransform(automate);
        AutomateOptimizationUtils.optimization(automate);
        System.out.println(automate.toString());
        System.out.println(Functions.function(automate, "+12.35e+10", 0));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
