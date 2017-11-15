package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.parser.LexerParser;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        List<Automate> automates = new ArrayList<>();

        automates.addAll(LexerParser.getAutomates("lexic/lexer.lex"));

        System.out.println(automates);

        //01011010001010
        //01011111000101

        //System.out.println(TaskTree.getLexemes(FileReader.readFile("code.txt")));
        //System.out.println(Functions.getLexemes(automates, FileReader.readFile("code.txt")));
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
