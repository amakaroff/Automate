package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.util.generator.AutomateGenerator;
import org.makarov.util.operations.AutomateOperations;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class RegexParser {

    public static Automate parseRegex(String regex) {
        return parseRegex(regex, false);
    }

    public static Automate parseRegex(String regex, boolean debug) {
        int index = 0;
        List<Automate> expressions = new ArrayList<>();
        List<Automate> forConcat = new ArrayList<>();
        List<String> expression = new ArrayList<>();

        log(debug, "\nRegex is initialized!");

        while (index < regex.length()) {
            char character = regex.charAt(index);
            if (isSpace(character)) {
                log(debug, "Skip space symbol");
                index++;
            } else if (character == '\\') {
                index++;
                character = regex.charAt(index);
                log(debug, "Shielding symbol of %s", character);
                switch (character) {
                    case '\\':
                        expression.add("\\");
                        break;
                    case 's':
                        expression.add("\\s");
                        break;
                    case 'w':
                        expression.add("\\w");
                        break;
                    case 'd':
                        expression.add("\\d");
                        break;
                    case '|':
                        expression.add("|");
                        break;
                    case '?':
                        expression.add("\\?");
                        break;
                    case '*':
                        expression.add("*");
                        break;
                    case '(':
                        expression.add("(");
                        break;
                    case ')':
                        expression.add(")");
                        break;
                    case ' ':
                        expression.add(" ");
                        break;
                    case 't':
                        expression.add("\t");
                        break;
                    case 'r':
                        expression.add("\r");
                        break;
                    case 'n':
                        expression.add("\n");
                        break;
                    default:
                        log(debug, "Error at shielding symbol. Wrong character is %s", character);
                        throw new AutomateException("Error at shielding symbol. Wrong character is " + character);
                }

                index++;
            } else if (character == '|') {
                log(debug, "End of expression. Concat %s", expression);
                if (expression.size() != 0) {
                    expressions.add(generateConcatAutomate(forConcat, expression));
                    expression = new ArrayList<>();
                }

                index++;
            } else if (character == '(') {
                ArrayDeque<Character> queue = new ArrayDeque<>();
                queue.push('(');
                int currentIndex = index;
                index++;
                log(debug, "Start find close bracket expression! Current position: " + index);
                log(debug, "In brackets character: {%s}", '(');
                while (!queue.isEmpty()) {
                    if (index > regex.length()) {
                        log(debug, "Wrong open bracket. Position: %s", currentIndex);
                        throw new AutomateException("Wrong open bracket. Position: " + index);
                    }
                    character = regex.charAt(index);
                    log(debug, "In brackets character: {%s}", character);
                    if (character == '(') {
                        queue.push(character);
                    } else if (character == ')') {
                        queue.pop();
                    } else if (character == '\\') {
                        index++;
                    }
                    index++;
                }
                int startIndex = currentIndex + 1;
                int endIndex = index - 1;


                String innerRegex = regex.substring(startIndex, endIndex);
                log(debug, "Find inner regular expression: {%s}. Start position: {%s}, End position {%s}",
                        innerRegex, startIndex, endIndex);
                expressions.add(parseRegex(innerRegex, debug));
            } else if (character == '*') {
                if (!expression.isEmpty()) {
                    log(debug, "End of expression. Repeat {%s}", expression);
                    expressions.add(AutomateOperations.repeat(generateConcatAutomate(forConcat, expression)));
                } else {
                    Automate automate = forConcat.get(expressions.size() - 1);
                    log(debug, "End of expression. Repeat automate {%s}", automate);
                    expressions.add(AutomateOperations.repeat(automate));
                }


                expression = new ArrayList<>();
                index++;
            } else {
                log(debug, "Simple character: {%s}", character);
                expression.add(String.valueOf(character));
                index++;
            }
        }

        log(debug, "Generate concat by expression: {%s}", expression);
        expressions.add(generateConcatAutomate(forConcat, expression));

        log(debug, "Join automates: {%s}", expressions);
        return generateUnionAutomate(expressions);
    }

    private static Automate generateConcatAutomate(List<Automate> automates, List<String> expression) {
        int index = 0;
        Automate automateResult;

        if (automates.isEmpty()) {
            if (expression.size() == 0) {
                return AutomateGenerator.generateOneAutomate("");
            }

            automateResult = AutomateGenerator.generateOneAutomate(expression.get(index));
        } else {
            int innerIndex = 0;

            automateResult = automates.get(innerIndex);

            while (index < expression.size()) {
                Automate automate = automates.get(innerIndex);
                automateResult = AutomateOperations.concat(automateResult, automate);
                innerIndex++;
            }
        }

        index++;
        while (index < expression.size()) {
            Automate automate = AutomateGenerator.generateOneAutomate(expression.get(index));
            automateResult = AutomateOperations.concat(automateResult, automate);
            index++;
        }

        return automateResult;
    }

    private static Automate generateUnionAutomate(List<Automate> automates) {
        if (automates.size() == 0) {
            return AutomateGenerator.generateOneAutomate("");
        } else if (automates.size() == 1) {
            return automates.get(0);
        }

        Automate automateResult = AutomateOperations.union(automates.get(0), automates.get(1));
        for (int i = 2; i < automates.size(); i++) {
            automateResult = AutomateOperations.union(automateResult, automates.get(i));
        }

        return automateResult;
    }

    private static boolean isSpace(char symbol) {
        return symbol == ' ' || symbol == '\n' || symbol == '\t' || symbol == '\r';
    }

    private static void log(boolean debug, String message, Object... objects) {
        if (debug) {
            System.out.println(String.format(message, objects));
        }
    }
}
