package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.exception.AutomateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Functions {

    public static final Comparator<String> stringComparator = (first, second) -> {
        if (first.length() > second.length()) {
            return 1;
        } else if (first.length() < second.length()) {
            return -1;
        } else {
            return first.compareTo(second);
        }
    };

    public static String scheduleSymbols(String line) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\r') {
                builder.append("\\r");
            } else if (c == '\n') {
                builder.append("\\n");
            } else if (c == '\t') {
                builder.append("\\t");
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public static Pair<Boolean, Integer> function(Automate automate, String line, int index) {
        return function(automate, line, index, false);
    }

    public static Pair<Boolean, Integer> function(Automate automate, String line, int index, boolean debug) {
        automate.init(debug);
        automate.refresh();
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        if (automate.isEnd() && line.length() == 0) {
            Pair<Boolean, Integer> pair = new Pair<>(true, allCount);
            log(debug, "Automate has finished with result: %s\n", pair);
            return pair;
        }

        for (int i = index; i < line.length(); i++) {
            try {
                automate.nextState(line.charAt(i));
                tempCount++;
                if (automate.isEnd()) {
                    log(debug, "On signal: {%s} - State is end!", line.charAt(i));
                    allCount += tempCount;
                    tempCount = 0;
                    isEnd = true;
                }
            } catch (AutomateException exception) {
                log(debug, "Signal: {%s}. Automate has finished the work with errors!", line.charAt(i));
                break;
            }
        }

        Pair<Boolean, Integer> pair = new Pair<>(isEnd, allCount);
        log(debug, "Automate has finished the work correctly! %s\n", pair);

        return pair;
    }

    public static Collection<Pair<String, String>> getLexemes(Collection<Automate> automates, String line) {
        return getLexemes(automates, line, false);
    }

    public static Collection<Pair<String, String>> getLexemes(Collection<Automate> automates, String line, boolean debug) {
        List<Pair<String, String>> lexemes = new ArrayList<>();
        List<Pair<Integer, String>> errors = new ArrayList<>();

        int index = 0;

        while (index < line.length()) {
            try {
                allRefresh(automates);
                Pair<String, String> lexeme = getLexeme(automates, line, index, debug);
                log(debug, "Find lexeme: %s", lexeme);
                lexemes.add(lexeme);
                index += lexeme.getValue().length();
            } catch (AutomateException exception) {
                errors.add(new Pair<>(index, "Position: {" + index + "} has some error : " + exception.getMessage() + "\n"));
                index++;
            }
        }

        if (debug) {
            for (Pair<Integer, String> error : errors) {
                System.err.println(MessageConstructor.createMessage(error.getValue(), error.getKey(), line));
            }
        }

        return lexemes;
    }

    public static Pair<String, String> getLexeme(Collection<Automate> automates, String line, int index) {
        return getLexeme(automates, line, index, false);
    }

    public static Pair<String, String> getLexeme(Collection<Automate> automates, String line, int index, boolean debug) {
        Map<Automate, Pair<Boolean, Integer>> results = new HashMap<>();
        for (Automate automate : automates) {
            Pair<Boolean, Integer> token = function(automate, line, index, debug);
            if (token.getKey()) {
                results.put(automate, token);
            }
        }

        Automate resultAutomate = getAutomateWithMaxPriority(results);
        Pair<Boolean, Integer> resultPair = results.get(resultAutomate);

        return new Pair<>(resultAutomate.getName(), line.substring(index, index + resultPair.getValue()));
    }

    private static Automate getAutomateWithMaxPriority(Map<Automate, Pair<Boolean, Integer>> results) {
        if (results.isEmpty()) {
            throw new AutomateException("Results is incorrect!");
        }

        Map<Automate, Pair<Boolean, Integer>> newResults = new HashMap<>();
        int first = 0;
        int maxCount = 0;

        for (Pair<Boolean, Integer> pair : results.values()) {
            if (pair.getValue() > maxCount) {
                maxCount = pair.getValue();
            }
        }

        for (Map.Entry<Automate, Pair<Boolean, Integer>> pairEntry : results.entrySet()) {
            if (pairEntry.getValue().getValue() == maxCount) {
                newResults.put(pairEntry.getKey(), pairEntry.getValue());
            }
        }

        int maxPriority = 0;
        for (Automate automate : newResults.keySet()) {
            if (automate.getPriority() > maxPriority) {
                maxPriority = automate.getPriority();
            }
        }

        List<Automate> finalResult = new ArrayList<>();
        for (Automate automate : newResults.keySet()) {
            if (automate.getPriority() == maxPriority) {
                finalResult.add(automate);
            }
        }

        if (finalResult.size() > 1) {
            throw new AutomateException("Two or more automates is correct for this case!");
        }

        return finalResult.get(first);
    }

    private static void allRefresh(Collection<Automate> automates) {
        for (Automate automate : automates) {
            automate.refresh();
        }
    }

    private static void log(boolean debug, String message, Object... objects) {
        if (debug) {
            System.out.println(String.format(message, objects));
        }
    }
}
