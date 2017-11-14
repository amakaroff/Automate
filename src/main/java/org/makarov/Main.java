package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.optimization.AutomateOptimizationUtils;
import org.makarov.util.parser.LexerParser;
import org.makarov.util.transformer.AutomateTransformer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        List<Automate> automates = new ArrayList<>();

        Automate automate1 = null;
        for (Automate automate : LexerParser.getAutomates("lexic/lexer.lex")) {
            automate = AutomateTransformer.toDeterministicAutomateTransform(automate);
            AutomateOptimizationUtils.optimization(automate);
            automates.add(automate);
            automate1 = automate;
        }


        //01011010001010
        //01011111000101
        //System.out.println(automates);

        System.out.println(automate1);

        System.out.println(Functions.function(automate1, "01011111000101", 0));

        //System.out.println(TaskTree.getLexemes(FileReader.readFile("code.txt")));
        //System.out.println(Functions.getLexemes(automates, FileReader.readFile("code.txt")));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
