package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;

import java.util.HashSet;
import java.util.Set;

public class NonDeterministicAutomate extends Automate<Set<String>> {

    public NonDeterministicAutomate(AutomateReader<Set<String>> reader) {
        super(reader);
    }

    @Override
    public void nextState(char signal) throws AutomateException {
        String currentSignal = String.valueOf(signal);
        if (translator != null) {
            currentSignal = translator.translate(signal);
        }

        if (!alphabet.contains(currentSignal) || currentState.size() == 0) {
            throw new AutomateException();
        }

        Set<String> newStates = new HashSet<>();

        for (String state : currentState) {
            Set<String> newState = table.get(state).get(currentSignal);
            if (!newState.isEmpty()) {
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
