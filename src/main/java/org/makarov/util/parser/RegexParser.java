package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.generate.EmptyAutomateGenerateReader;
import org.makarov.automate.reader.generate.OneSignalAutomateGenerateReader;
import org.makarov.automate.translators.Translator;
import org.makarov.util.MessageUtils;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.optimization.AutomateOptimization;
import org.makarov.util.transformer.AutomateTransformer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RegexParser {

    public static Automate parseRegex(String regex) {
        return parseRegex(regex, false);
    }

    public static Automate parseRegex(String regex, boolean debug) {
        List<String> errors = new ArrayList<>();

        long time = System.currentTimeMillis();
        Automate tempAutomate = parseRegex0(regex, debug, errors);
        DeterministicAutomate automate = AutomateTransformer.toDeterministicAutomateTransform(tempAutomate);
        AutomateOptimization.optimization(automate);
        time = System.currentTimeMillis() - time;

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.err.println(error);
            }
            throw new AutomateException("Regular expressions is compiled with errors!");
        }

        log(true, "Regular expression is compiled complete for %s", getTime(time));
        return automate;
    }

    private static Automate generateAutomate(String oneChar) {
        if (oneChar == null || oneChar.isEmpty() || Translator.EMPTY_SYMBOL.equals(oneChar)) {
            Automate automate = new DeterministicAutomate(new EmptyAutomateGenerateReader());
            automate.init();
            return automate;
        } else {
            Automate automate = new DeterministicAutomate(new OneSignalAutomateGenerateReader(oneChar));
            automate.init();
            return automate;
        }
    }

    private static String getTime(long time) {
        if (time / 1000 == 0) {
            return time + " milliseconds";
        }

        if (time / 60000 == 0) {
            return time / 1000 + " seconds and " + time % 1000 + " milliseconds";
        }

        return "Overtime!";
    }

    private static Automate parseRegex0(String regex, boolean debug, List<String> errors) {
        int index = 0;
        List<Automate> expressions = new ArrayList<>();
        List<Automate> forConcat = new ArrayList<>();
        int unionCount = 0;

        log(debug, "\nRegex is initialized!");

        if (regex.trim().isEmpty()) {
            return generateAutomate(Translator.EMPTY_SYMBOL);
        }

        next:
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
                        forConcat.add(generateAutomate("\\"));
                        break;
                    case 's':
                        forConcat.add(generateAutomate(Translator.SPACE_SYMBOL));
                        break;
                    case 'w':
                        forConcat.add(generateAutomate(Translator.LETTER_SYMBOL));
                        break;
                    case 'd':
                        forConcat.add(generateAutomate(Translator.NUMBER_SYMBOL));
                        break;
                    case '|':
                        forConcat.add(generateAutomate("|"));
                        break;
                    case '?':
                        forConcat.add(generateAutomate(Translator.EMPTY_SYMBOL));
                        break;
                    case '*':
                        forConcat.add(generateAutomate("*"));
                        break;
                    case '(':
                        forConcat.add(generateAutomate("("));
                        break;
                    case ')':
                        forConcat.add(generateAutomate(")"));
                        break;
                    case ' ':
                        forConcat.add(generateAutomate(" "));
                        break;
                    case 't':
                        forConcat.add(generateAutomate("\t"));
                        break;
                    case 'r':
                        forConcat.add(generateAutomate("\r"));
                        break;
                    case 'n':
                        forConcat.add(generateAutomate("\n"));
                        break;
                    case '.':
                        forConcat.add(generateAutomate("\\."));
                        break;
                    default:
                        errors.add(MessageUtils.createMessage("Error at shielding symbol. Wrong character is "
                                + character, index, regex));
                        index++;
                        continue;
                }

                index++;
            } else if (character == '|') {
                log(debug, "End of expression. Concat %s", forConcat);
                if (forConcat.isEmpty()) {
                    errors.add(MessageUtils.createMessage("Wrong symbol | on position: " + index, index, regex));
                    index++;
                    continue;
                }

                expressions.add(generateConcatAutomate(forConcat));
                unionCount++;
                index++;
            } else if (character == '(') {
                Deque<Character> queue = new ArrayDeque<>();
                queue.push('(');
                int currentIndex = index;
                index++;
                log(debug, "Start find close bracket expression! Current position: " + index);
                log(debug, "In brackets character: {%s}", '(');
                while (!queue.isEmpty()) {
                    if (index >= regex.length()) {
                        errors.add(MessageUtils.createMessage("Wrong open bracket. Position: " + currentIndex,
                                currentIndex, regex));
                        index++;
                        continue next;
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
                forConcat.add(parseRegex0(innerRegex, debug, errors));
            } else if (character == '*') {
                if (!forConcat.isEmpty()) {
                    Automate automate = forConcat.get(forConcat.size() - 1);
                    forConcat.remove(forConcat.size() - 1);
                    log(debug, "End of expression. Repeat automate {%s}", automate);
                    forConcat.add(AutomateOperations.repeat(automate));
                } else {
                    errors.add(MessageUtils.createMessage("Wrong symbol * on position: " + index, index, regex));
                }

                index++;
            } else if (character == ')') {
                errors.add(MessageUtils.createMessage("Not opened close bracket on position: " + index, index,
                        regex));
                index++;
            } else {
                log(debug, "Simple character: {%s}", character);
                forConcat.add(generateAutomate(String.valueOf(character)));
                index++;
            }
        }

        log(debug, "Generate concat by automates: {%s}", forConcat);
        if (!forConcat.isEmpty()) {
            expressions.add(generateConcatAutomate(forConcat));
        }

        if (expressions.size() <= unionCount && expressions.size() != 0) {
            int errorIndex = regex.lastIndexOf('|');
            errors.add(MessageUtils.createMessage("Wrong symbol | on position: " + errorIndex, errorIndex, regex));
        }

        log(debug, "Join automates: {%s}\n", expressions);
        return generateUnionAutomate(expressions);
    }

    private static Automate generateConcatAutomate(List<Automate> automates) {
        int index = 0;
        Automate automateResult;
        if (automates.isEmpty()) {
            return generateAutomate(Translator.EMPTY_SYMBOL);
        } else if (automates.size() == 1) {
            Automate automate = automates.get(index);
            automates.clear();
            return automate;
        }

        automateResult = automates.get(index);
        index++;
        while (index < automates.size()) {
            Automate automate = automates.get(index);
            automateResult = AutomateOperations.concat(automateResult, automate);
            index++;
        }
        automates.clear();

        return automateResult;
    }

    private static Automate generateUnionAutomate(List<Automate> automates) {
        if (automates.isEmpty()) {
            return generateAutomate(Translator.EMPTY_SYMBOL);
        } else if (automates.size() == 1) {
            return automates.get(0);
        }

        Automate automateResult = AutomateOperations.union(automates.get(0), automates.get(1));
        for (int i = 2; i < automates.size(); i++) {
            automateResult = AutomateOperations.union(automateResult, automates.get(i));
        }
        automateResult.init();

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
