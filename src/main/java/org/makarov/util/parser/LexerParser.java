package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.util.FileUtils;
import org.makarov.util.Functions;
import org.makarov.util.Pair;
import org.makarov.util.generator.AutomateGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Aleksei Makarov on 26.10.2017.
 */
public class LexerParser {

    public static Collection<Automate> getAutomates(String filePath) {
        String context = FileUtils.readFile(filePath);

        List<Automate> automates = new ArrayList<>();
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/name.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/priority.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/priority.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/colon.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/space.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("util/cotchie.json")));

        return parseText(Functions.getLexemes(automates, context));
    }

    private static Collection<Automate> parseText(Collection<Pair<String, String>> oldTokens) {
        List<Automate> automates = new ArrayList<>();

        Iterator<Pair<String, String>> iterator = oldTokens.iterator();
        while (iterator.hasNext()) {
            String name = getTokenValue(iterator, "name");
            getTokenValue(iterator, "colon");
            String priority = getTokenValue(iterator, "priority");
            getTokenValue(iterator, "colon");
            String regex = getTokenValue(iterator, "regex");
            getTokenValue(iterator, "cotchie");
            automates.add(AutomateGenerator.generate(name, priority, regex));
        }

        return automates;
    }

    private static String getTokenValue(Iterator<Pair<String, String>> tokens, String tokenName) {
        Pair<String, String> next = tokens.next();
        if (next.getKey().equals("space")) {
            next = tokens.next();
            if (next.getKey().equals(tokenName)) {
                return next.getValue();
            }
        } else if (next.getKey().equals(tokenName)) {
            return next.getValue();
        }

        throw new AutomateException("Name token is incorrect");
    }
}
