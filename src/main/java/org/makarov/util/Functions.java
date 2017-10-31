package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.exception.AutomateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Functions {

    public static Pair<Boolean, Integer> function(Automate automate, String line, int index) {
        return function(automate, line, index, false);
    }

    public static Pair<Boolean, Integer> function(Automate automate, String line, int index, boolean debug) {
        automate.init(debug);
        automate.refresh();
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        if (debug) {
            System.out.println("Functions is initialized!");
        }

        if (automate.isEnd() && line.length() == 0) {
            Pair<Boolean, Integer> pair = new Pair<>(true, allCount);
            if (debug) {
                System.out.println("Automate has finished with result: " + pair + "\n");
            }
            return pair;
        }

        for (int i = index; i < line.length(); i++) {
            try {
                if (debug) {
                    System.out.println("Signal: {" + line.charAt(i) + "}. Try to next state!");
                }
                automate.nextState(line.charAt(i));
                tempCount++;
                if (automate.isEnd()) {
                    if (debug) {
                        System.out.println("Signal: {" + line.charAt(i) + "}. Data flush to line!");
                    }
                    allCount += tempCount;
                    tempCount = 0;
                    isEnd = true;
                }
            } catch (AutomateException exception) {
                if (debug) {
                    System.out.println("Signal: {" + line.charAt(i) + "}. Automate has finished hes worked!");
                }
                break;
            }
        }

        Pair<Boolean, Integer> pair = new Pair<>(isEnd, allCount);

        if (debug) {
            System.out.println("Automate has finished hes worked correctly! " + pair + "\n");
        }

        return pair;
    }

    public static Collection<Pair<String, String>> getLexemes(Collection<Automate> automates, String line) {
        return getLexemes(automates, line, false);
    }

    public static Collection<Pair<String, String>> getLexemes(Collection<Automate> automates, String line, boolean debug) {
        List<Pair<String, String>> lexemes = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        int index = 0;

        while (index < line.length()) {
            try {
                Pair<String, String> lexeme = getLexeme(automates, line, index, debug);
                if (debug) {
                    System.out.println("Find lexeme: " + lexeme);
                }
                lexemes.add(lexeme);
                index += lexeme.getValue().length();
            } catch (AutomateException exception) {
                errors.add("Position " + index + " has some error : " + exception.getMessage() + "\n");
                index++;
            }
        }

        if (debug) {
            for (String error : errors) {
                System.out.println(error);
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
            results.put(automate, function(automate, line, index, debug));
            allRefresh(automates);
        }

        Automate resultAutomate = getAutomateWithMaxPriority(results);
        Pair<Boolean, Integer> resultPair = results.get(resultAutomate);

        return new Pair<>(resultAutomate.getName(), line.substring(index, index + resultPair.getValue()));
    }

    private static Automate getAutomateWithMaxPriority(Map<Automate, Pair<Boolean, Integer>> results) {
        if (results.isEmpty()) {
            throw new AutomateException("Results is incorrect!");
        } else if (!isCorrectResults(results.values())) {
            throw new AutomateException("Automate for this lexeme is not found!");
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

    private static boolean isCorrectResults(Collection<Pair<Boolean, Integer>> results) {
        for (Pair<Boolean, Integer> result : results) {
            if (result.getKey()) {
                return true;
            }
        }

        return false;
    }

    private static void allRefresh(Collection<Automate> automates) {
        for (Automate automate : automates) {
            automate.refresh();
        }
    }
}
