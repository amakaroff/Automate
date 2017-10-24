package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;

import java.util.*;

public class Functions {

    public static Pair<Boolean, Integer> function(Automate automate, String line, int index) {
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        automate.init();
        if (automate.isEnd()) {
            return new Pair<>(true, allCount);
        }

        for (int i = index; i < line.length(); i++) {
            try {
                automate.nextState(line.charAt(i));
                tempCount++;
                if (automate.isEnd()) {
                    allCount += tempCount;
                    tempCount = 0;
                    isEnd = true;
                }
            } catch (AutomateException exception) {
                break;
            }
        }

        if (line.length() == 0) {
            isEnd = true;
        }

        return new Pair<>(isEnd, allCount);
    }

    public static Collection<Pair<String, String>> getLexems(Collection<Automate> automates, String line) {
        List<Pair<String, String>> lexemes = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        int index = 0;

        while (index < line.length()) {
            try {
                Pair<String, String> lexeme = getLexeme(automates, line, index);
                lexemes.add(lexeme);
                index += lexeme.getValue().length();
            } catch (AutomateException exception) {
                errors.add("Position " + index + " has some error : " + exception.getMessage() + "\n");
                index++;
            }
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.out.println(error);
            }
        }

        return lexemes;
    }

    public static Pair<String, String> getLexeme(Collection<Automate> automates, String line, int index) {
        Map<Automate, Pair<Boolean, Integer>> results = new HashMap<>();
        for (Automate automate : automates) {
            results.put(automate, function(automate, line, index));
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
        final int FIRST = 0;
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

        return finalResult.get(FIRST);
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
