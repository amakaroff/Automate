package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.EmptyAutomateGenerateReader;
import org.makarov.automate.reader.OneSignalAutomateGenerateReader;
import org.makarov.constants.RegexConstants;
import org.makarov.util.operations.AutomateOperations;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegexParser {

    public static Automate parseRegex(String regex) {
        return parseRegex0(regex);
    }

    public static Automate parseRegex(String regex, boolean debug) {
        long time = System.currentTimeMillis();
        Automate automate = parseRegex0(regex, debug);
        time = System.currentTimeMillis() - time;

        log(debug, "Regular expression compilation complete for %s", getTime(time));
        return automate;
    }

    public static Automate generateOneAutomate(String oneChar) {
        if (oneChar == null || oneChar.isEmpty() || RegexConstants.EMPTY_SYMBOL.equals(oneChar)) {
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
            return time / 1000 + " seconds, " + time % 1000 + " milliseconds";
        }

        return "";
    }

    private static Automate parseRegex0(String regex) {
        return parseRegex0(regex, false);
    }

    private static Automate parseRegex0(String regex, boolean debug) {
        int index = 0;
        List<Automate> expressions = new ArrayList<>();
        List<Automate> forConcat = new ArrayList<>();

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
                        forConcat.add(generateOneAutomate("\\"));
                        break;
                    case 's':
                        forConcat.add(generateOneAutomate(RegexConstants.SPACE_SYMBOL));
                        break;
                    case 'w':
                        forConcat.add(generateOneAutomate(RegexConstants.LETTER_SYMBOL));
                        break;
                    case 'd':
                        forConcat.add(generateOneAutomate(RegexConstants.NUMBER_SYMBOL));
                        break;
                    case '|':
                        forConcat.add(generateOneAutomate("|"));
                        break;
                    case '?':
                        forConcat.add(generateOneAutomate(RegexConstants.EMPTY_SYMBOL));
                        break;
                    case '*':
                        forConcat.add(generateOneAutomate("*"));
                        break;
                    case '(':
                        forConcat.add(generateOneAutomate("("));
                        break;
                    case ')':
                        forConcat.add(generateOneAutomate(")"));
                        break;
                    case ' ':
                        forConcat.add(generateOneAutomate(" "));
                        break;
                    case 't':
                        forConcat.add(generateOneAutomate("\t"));
                        break;
                    case 'r':
                        forConcat.add(generateOneAutomate("\r"));
                        break;
                    case 'n':
                        forConcat.add(generateOneAutomate("\n"));
                        break;
                    case '.':
                        forConcat.add(generateOneAutomate("\\."));
                        break;
                    default:
                        log(debug, "Error at shielding symbol. Wrong character is %s", character);
                        throw new AutomateException("Error at shielding symbol. Wrong character is " + character);
                }

                index++;
            } else if (character == '|') {
                log(debug, "End of expression. Concat %s", forConcat);
                if (forConcat.isEmpty()) {
                    throw new AutomateException("Wrong symbol | on position: " + index);
                }

                expressions.add(generateConcatAutomate(forConcat, debug));

                index++;
            } else if (character == '(') {
                ArrayDeque<Character> queue = new ArrayDeque<>();
                queue.push('(');
                int currentIndex = index;
                index++;
                log(debug, "Start find close bracket expression! Current position: " + index);
                log(debug, "In brackets character: {%s}", '(');
                while (!queue.isEmpty()) {
                    if (index >= regex.length()) {
                        log(debug, "Wrong open bracket. Position: %s", currentIndex);
                        throw new AutomateException("Wrong open bracket. Position: " + currentIndex);
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
                forConcat.add(parseRegex0(innerRegex, debug));
            } else if (character == '*') {
                if (!forConcat.isEmpty()) {
                    Automate automate = forConcat.get(forConcat.size() - 1);
                    forConcat.remove(forConcat.size() - 1);
                    log(debug, "End of expression. Repeat automate {%s}", automate);
                    forConcat.add(AutomateOperations.repeat(automate));
                } else {
                    throw new AutomateException("Automates for repeat is not found!");
                }

                index++;
            } else if (character == ')') {
                throw new AutomateException("Wrong symbol " + character + " on position: " + index);
            } else {
                log(debug, "Simple character: {%s}", character);
                forConcat.add(generateOneAutomate(String.valueOf(character)));
                index++;
            }
        }

        log(debug, "Generate concat by automates: {%s}", forConcat);
        expressions.add(generateConcatAutomate(forConcat, debug));

        log(debug, "Join automates: {%s}\n", expressions);
        return generateUnionAutomate(expressions, debug);
    }

    private static Automate generateConcatAutomate(List<Automate> automates, boolean debug) {
        int index = 0;
        Automate automateResult;
        log(debug, "Concat operations %s", automates);
        if (automates.isEmpty()) {
            throw new AutomateException("Generation of concatenation is unreal! Automates is empty!");
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

    private static Automate generateUnionAutomate(List<Automate> automates, boolean debug) {
        if (automates.size() == 0) {
            throw new AutomateException("Generation of union is unreal! Automates is empty!");
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
            if (objects.length == 1 && (objects[0] instanceof Collection)) {
                Collection<Automate> automates = (Collection<Automate>) objects[0];
                for (Automate automate : automates) {
                    automate.init();
                }
            }
            System.out.println(String.format(message, objects));
        }
    }
}
