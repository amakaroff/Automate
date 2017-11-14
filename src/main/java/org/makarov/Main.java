package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Functions;
import org.makarov.util.parser.RegexParser;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        List<Automate> automates = new ArrayList<>();

        /*for (Automate automate : LexerParser.getAutomates("lexic/lexer.lex")) {
            automate = AutomateTransformer.toDeterministicAutomateTransform(automate);
            AutomateOptimizationUtils.optimization(automate);
            automates.add(automate);
        }

        System.out.println(automates);*/

        //01011010001010
        //01011111000101
        Automate automate = RegexParser.parseRegex("(00|11)*((01|10)(00|11)*(01|10)(00|11)*)*");
        System.out.println(automate.toString());
        System.out.println(automate.toString());
        System.out.println(Functions.function(automate, "01011111000101", 0));
        //System.out.println(automate.toString());


        //System.out.println(TaskTree.getLexemes(FileReader.readFile("code.txt")));
        //System.out.println(Functions.getLexemes(automates, FileReader.readFile("code.txt")));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
