package org.makarov.parser;

import org.makarov.automate.Automate;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntacticParser {

    private Rule startRule;

    private Map<String, Rule> rules;

    private Collection<Automate> lexicon;

    private List<String> errors;

    SyntacticParser(Collection<Rule> rules, Collection<Automate> lexicon, Rule startRule) {
        this.lexicon = lexicon;
        this.startRule = startRule;
        this.errors = new ArrayList<>();
        this.rules = new HashMap<>();
        for (Rule rule : rules) {
            this.rules.put(rule.getName(), rule);
        }
    }

    public SyntacticNode parseText(String line) {
        List<Pair<String, String>> tokens = new ArrayList<>(Functions.getLexemes(lexicon, line));
        SyntacticNode rootNode = new SyntacticNode(new Pair<>(startRule.getName(), ""));
        parseTokens(tokens, 0, rootNode, startRule);
        return rootNode;
    }

    private int parseTokens(List<Pair<String, String>> tokens, int tokenIndex, SyntacticNode node, Rule currentRule) {
        SyntacticNode currentNode = new SyntacticNode(new Pair<>(currentRule.getName(), ""));
        int currentIndex = tokenIndex;
        boolean isEnd = true;
        for (Rule.Symbol symbol : currentRule.getExpression().getSymbols()) {
            isEnd = true;
            while (tokenIndex < tokens.size()) {
                Pair<String, String> token = tokens.get(currentIndex);
                if (symbol.isTerminate()) {
                    int appendIndex = parseTokens(tokens, currentIndex, currentNode, rules.get(symbol.getSymbol()));
                    if (appendIndex == 0) {
                        isEnd = false;
                        break;
                    }
                } else {
                    String tokenValue = token.getValue();
                    if (symbol.getSymbol().equals(tokenValue)) {
                        currentNode.addNode(new SyntacticNode(token));
                        currentIndex++;
                    } else {
                        isEnd = false;
                        break;
                    }
                }
            }
            if (isEnd) {
                break;
            }
        }

        if (!isEnd) {
            return 0;
        }

        node.addNode(currentNode);
        return currentIndex;
    }

    public static class SyntacticNode {

        private Pair<String, String> token;

        private List<SyntacticNode> childes;

        public SyntacticNode(Pair<String, String> token) {
            this.token = token;
            this.childes = new ArrayList<>();
        }

        public void addNode(SyntacticNode node) {
            childes.add(node);
        }

        @Override
        public String toString() {
            return toString(0);
        }

        public String resolveList(int count) {
            if (childes == null || childes.isEmpty()) {
                return "";
            } else {
                StringBuilder builder = new StringBuilder();
                for (SyntacticNode child : childes) {
                    builder.append(child.toString(count + 4));
                }

                return builder.toString();
            }
        }

        private String toString(int count) {
            return printEmptySpaces(count) + (token.getValue().isEmpty() ? token.getKey() : token) + "\n" + resolveList(count);
        }

        private String printEmptySpaces(int count) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                builder.append(" ");
            }

            return builder.toString();
        }
    }
}
