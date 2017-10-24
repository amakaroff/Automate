package org.makarov.automate;

import org.makarov.automate.reader.AutomateReader;

public class DeterministicAutomate extends Automate<String> {

    public DeterministicAutomate(AutomateReader<String> reader) {
        super(reader);
    }

    @Override
    public void nextState(char signal) throws AutomateException {
        String currentSignal = String.valueOf(signal);
        if (translator != null) {
            currentSignal = translator.translate(signal);
        }

        if (!alphabet.contains(currentSignal) || table.get(currentState) == null) {
            throw new AutomateException();
        }

        String newState = table.get(currentState).get(currentSignal);

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
