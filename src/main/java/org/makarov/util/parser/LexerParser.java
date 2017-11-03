package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.json.JSONDeterministicAutomateReader;
import org.makarov.util.AutomateReflection;
import org.makarov.util.FileUtils;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Deprecated
//TODO: rewrite this class to line parser
public class LexerParser {

    private static final String NAME = "name";

    private static final String COLON = "colon";

    private static final String PRIORITY = "priority";

    private static final String SEMICOLON = "semicolon";

    private static final String SPACE = "space";

    private static final String REGEX = "regex";

    public static Collection<Automate> getAutomates(String filePath) {
        return getAutomates(filePath, false);
    }



    public static Collection<Automate> getAutomates(String filePath, boolean debug) {
        String content = FileUtils.readFile(filePath);

        List<Automate> automates = new ArrayList<>();
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/name")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/priority")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/priority")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/colon")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/space")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/regex")));
        automates.add(new DeterministicAutomate(new JSONDeterministicAutomateReader("lexer-util/semicolon")));

        return parseText(Functions.getLexemes(automates, content, debug));
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
            getTokenValue(iterator, SEMICOLON);
            automates.add(generate(name, priority, regex));
        }

        return automates;
    }

    private static String getTokenValue(Iterator<Pair<String, String>> tokens, String tokenName) {
        Pair<String, String> token = tokens.next();
        if (SPACE.equals(token.getKey())) {
            token = tokens.next();
        }

        if (token.getKey().equals(tokenName)) {
            return token.getValue();
        }

        throw new AutomateException("Token: {" + token + "} is incorrect");
    }

    private static void removeLastSpace(List<Pair<String, String>> tokens) {
        int lastIndex = tokens.size() - 1;
        if (SPACE.equals(tokens.get(lastIndex).getKey())) {
            tokens.remove(lastIndex);
        }
    }

    @SuppressWarnings("unchecked")
    public static Automate generate(String name, String priority, String regex) {
        Automate automate = RegexParser.parseRegex(regex);
        AutomateReflection reflection = new AutomateReflection(automate);
        reflection.setName(name);
        reflection.setPriority(Integer.valueOf(priority));

        return reflection.getAutomate();
    }

    private class AutomateTemplate {

        private String name;

        private int priority;

        private String regularExpression;

        public AutomateTemplate(String name, int priority, String regularExpression) {
            this.name = name;
            this.priority = priority;
            this.regularExpression = regularExpression;
        }

        public String getName() {
            return name;
        }

        public int getPriority() {
            return priority;
        }

        public String getRegularExpression() {
            return regularExpression;
        }
    }
}
