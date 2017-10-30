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

    public static Collection<Automate> getAutomates(String filePath, boolean debug) {
        String context = FileUtils.readFile(filePath);

        List<Automate> automates = new ArrayList<>();
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/name.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/priority.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/priority.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/colon.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/space.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/regex.json")));
        automates.add(new DeterministicAutomate(new JSONDeterminateAutomateReader("lexer-util/cotchie.json")));

        return parseText(Functions.getLexemes(automates, context, debug));
    }

    private static Collection<Automate> parseText(Collection<Pair<String, String>> oldTokens) {
        List<Automate> automates = new ArrayList<>();
        List<Pair<String, String>> tokens = new ArrayList<>(oldTokens);
        removeLastSpace(tokens);

        Iterator<Pair<String, String>> iterator = tokens.iterator();
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
        Pair<String, String> token = tokens.next();
        if ("space".equals(token.getKey())) {
            token = tokens.next();
            if (token.getKey().equals(tokenName)) {
                return token.getValue();
            }
        } else if (token.getKey().equals(tokenName)) {
            return token.getValue();
        }

        throw new AutomateException("Name token is incorrect");
    }

    private static void removeLastSpace(List<Pair<String, String>> tokens) {
        int lastIndex = tokens.size() - 1;
        if ("space".equals(tokens.get(lastIndex).getKey())) {
            tokens.remove(lastIndex);
        }
    }

}
