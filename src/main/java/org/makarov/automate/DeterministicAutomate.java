package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;

public class DeterministicAutomate extends Automate<String> {

    public DeterministicAutomate(AutomateReader<String> reader) {
        super(reader);
    }

    @Override
    public void nextState(String signal) throws AutomateException {
        if (!alphabet.contains(signal)) {
            throw new AutomateException();
        }

        if (table.get(currentState) == null) {
            throw new AutomateException();
        }

        String newState = table.get(currentState).get(signal);

        if (newState == null) {
            throw new AutomateException();
        }

        currentState = newState;
    }

    @Override
    public boolean isEnd() {
        return endState.contains(currentState);
    }
}
