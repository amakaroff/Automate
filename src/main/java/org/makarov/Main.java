package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;
import org.makarov.util.Pair;
import org.makarov.util.parser.LexerParser;
import org.makarov.util.parser.SyntacticParser;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        Collection<Automate> automates = LexerParser.getAutomates("lexic/lexer.lex");
        //System.out.println(automates);

        SyntacticParser.SyntacticNode node = new SyntacticParser.SyntacticNode(new Pair<>("KeyWord", "if"));
        SyntacticParser.SyntacticNode node1 = new SyntacticParser.SyntacticNode(new Pair<>("OpenBkt", "("));
        SyntacticParser.SyntacticNode node2 = new SyntacticParser.SyntacticNode(new Pair<>("expression", ""));
        SyntacticParser.SyntacticNode node4 = new SyntacticParser.SyntacticNode(new Pair<>("Identify", "a"));
        SyntacticParser.SyntacticNode node7 = new SyntacticParser.SyntacticNode(new Pair<>("Int", "5"));
        node4.addNode(node7);
        SyntacticParser.SyntacticNode node5 = new SyntacticParser.SyntacticNode(new Pair<>("Operator", "="));
        SyntacticParser.SyntacticNode node6 = new SyntacticParser.SyntacticNode(new Pair<>("Int", "5"));
        node2.addNode(node4);
        node2.addNode(node5);
        node2.addNode(node6);
        SyntacticParser.SyntacticNode node3 = new SyntacticParser.SyntacticNode(new Pair<>("CloseBkt", ")"));
        node.addNode(node1);
        node.addNode(node2);
        node.addNode(node3);

        System.out.println(node.toString());
    }

    //Regular helpers
    // one element or not (1|\?)
    // one element or more (1(1)*)
}
