package org.makarov.util.parser;

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

    public void parseText(String line) {
        List<Pair<String, String>> tokens = new ArrayList<>(Functions.getLexemes(lexicon, line));
        parseText0(tokens, 0, new SyntacticNode(startRule.getName()), startRule);
    }

    private void parseText0(List<Pair<String, String>> tokens, int tokenIndex, SyntacticNode node, Rule currentRule) {
        String value = currentRule.getName();
        int currentIndex = tokenIndex;
        for (Rule.Symbol symbol : currentRule.getExpression().getSymbols()) {
            while (tokenIndex < tokens.size()) {
                Pair<String, String> token = tokens.get(currentIndex);
                if (symbol.isTerminate()) {
                    SyntacticNode newNode = new SyntacticNode(value);
                    node.addNode(newNode);
                    parseText0(tokens, currentIndex, newNode, rules.get(symbol.getSymbol()));
                } else {
                    String tokenValue = token.getValue();
                    if (symbol.getSymbol().equals(tokenValue)) {
                        value += tokenValue;
                        currentIndex++;
                    } else {
                        errors.add("Error in line: " + currentIndex);
                        currentIndex++;
                    }
                }
            }
        }
    }

    public class SyntacticNode {

        private String name;

        private List<SyntacticNode> childes;

        public SyntacticNode(String name) {
            this.name = name;
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
                    builder.append(printEmptySpaces(count)).append(child.toString(count + 1)).append("\n");
                }

                return builder.toString();
            }
        }

        private String toString(int count) {
            return "{" + name + "}\n" + resolveList(count);
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
