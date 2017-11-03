package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.reader.json.JSONNonDeterministicAutomateReader;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NonDeterministicAutomate extends Automate<Set<String>> {

    private boolean isNullCleaning = false;

    public NonDeterministicAutomate(String filePath) {
        super(new JSONNonDeterministicAutomateReader(filePath));
    }

    public NonDeterministicAutomate(AutomateReader<Set<String>> reader) {
        super(reader);
    }

    @Override
    public void nextState(char signal) throws AutomateException {
        String currentSignal = String.valueOf(signal);
        log("Input signal: {%s}", currentSignal);
        checkNext(currentSignal);

        Set<String> newStates = new HashSet<>();
        log("Current state is : {%s}", currentState);
        for (String state : currentState) {
            Set<String> newState = table.get(state).get(currentSignal);
            if (newState == null || newState.isEmpty()) {
                newState = table.get(state).get(alwaysSymbol);
                if (newState != null && !newState.isEmpty()) {
                    log("New state for automate is not found!");
                }
            }

            if (newState != null && !newState.isEmpty()) {
                log("New state: {%s}", newState);
                newStates.addAll(newState);
            }
        }

        currentState = newStates;
    }

    @Override
    public boolean isEnd() {
        for (String newState : currentState) {
            if (endState.contains(newState)) {
                return true;
            }
        }

        return false;
    }
}
