package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.TransformAutomateReader;
import org.makarov.automate.serialize.AutomateSerializer;
import org.makarov.automate.serialize.AutomateToJSONSerializer;

public class Main {

    public static void main(String[] args) {
        DeterministicAutomate automate = new DeterministicAutomate("three/Real1");
        automate.init();
        Automate newAutomate = new NonDeterministicAutomate(new TransformAutomateReader(automate));
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(newAutomate));
        /*Automate generate = new DeterministicAutomate(new EmptyAutomateGenerateReader());
        generate.init(true);
        System.out.println(Functions.function(generate, "", 0, true));
        System.out.println(LexerParser.getAutomates("lexic/lexer.lex"));
        TaskTree.getLexems(FileUtils.readFile("code.txt"));
        Automate automate = new DeterministicAutomate("three/Identify");
        automate.init();
        AutomateRenamer.renameAutomate(automate);
        AutomateSerializer serializer = new AutomateToJSONSerializer();
        System.out.println(serializer.serialize(automate));
        Automate automate = new DeterministicAutomate("three/Real1");
        automate.init();
        System.out.println(TaskTwo.findAllNumbers(automate, "5выф-5.в+.5e+5.5e.7cda-.3ee-5.e-3502e.3e-3eв3.E3.3."));*/
    }
}
