package org.makarov.automate;

import org.makarov.automate.exception.AutomateException;
import org.makarov.automate.reader.AutomateReader;
import org.makarov.automate.reader.json.JSONDeterministicAutomateReader;

public class DeterministicAutomate extends Automate<String> {

    public DeterministicAutomate(String filePath) {
        super(new JSONDeterministicAutomateReader(filePath));
    }

    public DeterministicAutomate(AutomateReader<String> reader) {
        super(reader);
    }

    @Override
    public void nextState(char signal) throws AutomateException {
        String currentSignal = String.valueOf(signal);
        log("Input signal: {%s}", currentSignal);
        checkNext(currentSignal);

        String newState = table.get(currentState).get(currentSignal);
        if (newState != null) {
            log("New state: {%s}", newState);
        }
        if (newState == null) {
            newState = table.get(currentState).get(alwaysSymbol);
            if (newState != null) {
                log("New state: {%s}", newState);
            }

            if (newState == null) {
                log("New state for automate is not found! Current State: %s", currentState);
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
