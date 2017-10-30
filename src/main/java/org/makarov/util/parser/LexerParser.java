package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.util.FileUtils;
import org.makarov.util.Functions;
import org.makarov.util.Pair;
import org.makarov.util.generator.AutomateGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LexerParser {

    private static final String NAME = "name";

    private static final String COLON = "colon";

    private static final String PRIORITY = "priority";

    private static final String COTCHIE = "cotchie";

    private static final String SPACE = "space";

    private static final String REGEX = "regex";

    private static Collection<Automate> getAutomates(String filePath) {
        return getAutomates(filePath, false);
    }

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
            String name = getTokenValue(iterator, NAME);
            getTokenValue(iterator, COLON);
            String priority = getTokenValue(iterator, PRIORITY);
            getTokenValue(iterator, COLON);
            String regex = getTokenValue(iterator, REGEX);
            getTokenValue(iterator, COTCHIE);
            automates.add(AutomateGenerator.generate(name, priority, regex));
        }

        return automates;
    }

    private static String getTokenValue(Iterator<Pair<String, String>> tokens, String tokenName) {
        Pair<String, String> token = tokens.next();
        if (SPACE.equals(token.getKey())) {
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
        if (SPACE.equals(tokens.get(lastIndex).getKey())) {
            tokens.remove(lastIndex);
        }
    }

}
