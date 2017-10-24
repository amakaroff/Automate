package org.makarov.util;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateException;

import java.util.*;

public class Functions {

    public static Pair<Boolean, Integer> function(Automate<String> automate, String line, int index) {
        boolean isEnd = false;
        int allCount = 0;
        int tempCount = 0;

        automate.init();
        if (automate.isEnd()) {
            return new Pair<>(true, allCount);
        }

        for (int i = index; i < line.length(); i++) {
            try {
                String signal = String.valueOf(line.charAt(i));
                automate.nextState(signal);
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

    public static Collection<Pair<String, String>> getLexems(Collection<Automate<String>> automates, String line) {
        List<Pair<String, String>> lexemes = new ArrayList<>();

        int index = 0;

        while (index < line.length()) {
            Pair<String, String> lexeme = getLexeme(automates, line, index);
            lexemes.add(lexeme);
            index += lexeme.getValue().length();
        }

        return lexemes;
    }

    public static Pair<String, String> getLexeme(Collection<Automate<String>> automates, String line, int index) {
        Map<Automate, Pair<Boolean, Integer>> results = new HashMap<>();
        for (Automate<String> automate : automates) {
            results.put(automate, function(automate, line, index));
        }

        Automate resultAutomate = getAutomateWithMaxPriority(results);
        Pair<Boolean, Integer> resultPair = results.get(resultAutomate);

        if (resultPair.getKey()) {
            return new Pair<>(resultAutomate.getName(), line.substring(index, resultPair.getValue()));
        }

        return new Pair<>("", "");
    }

    private static Automate getAutomateWithMaxPriority(Map<Automate, Pair<Boolean, Integer>> results) {
        Map<Automate, Pair<Boolean, Integer>> newResults = new HashMap<>();
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

        for (Automate automate : newResults.keySet()) {
            if (automate.getPriority() == maxPriority) {
                return automate;
            }
        }

        throw new AutomateException("Not find automate for current case");
    }
}
