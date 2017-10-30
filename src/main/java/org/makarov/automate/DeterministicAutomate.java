package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;

public class DeterministicAutomate extends Automate<String> {

    public DeterministicAutomate(String filePath) {
        super(new JSONDeterminateAutomateReader(filePath));
    }

    public DeterministicAutomate(AutomateReader<String> reader) {
        super(reader);
    }

    @Override
    public void nextState(char signal) throws AutomateException {
        String currentSignal = String.valueOf(signal);
        if (debug) {
            System.out.println("Input signal: " + currentSignal);
        }
        checkNext(currentSignal);

        String newState = table.get(currentState).get(currentSignal);
        if (debug && newState != null) {
            System.out.println("New state: " + newState);
        }
        if (newState == null) {
            newState = table.get(currentState).get(alwaysSymbol);
            if (debug && newState != null) {
                System.out.println("New state: " + newState);
            }

            if (newState == null) {
                if (debug) {
                    System.out.println("New state for authomate is not found! Current State: " + currentState);
                }
                throw new AutomateException();
            }
        }

        currentState = newState;
    }

    @Override
    public boolean isEnd() {
        return endState.contains(currentState);
    }
}
