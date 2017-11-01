package org.makarov.automate.reader;

import org.makarov.automate.Automate;
import org.makarov.util.AutomateReflection;
import org.makarov.util.operations.AutomateOperations;
import org.makarov.util.operations.AutomateRenamer;


import java.util.List;
import java.util.Map;
import java.util.Set;


public class RepeatAutomateReader implements AutomateReader<Set<String>> {

    AutomateReflection<Set<String>> automate;

    @SuppressWarnings("unchecked")
    public RepeatAutomateReader(Automate automate) {
        automate.init();
        AutomateRenamer.renameAutomate(automate);

        this.automate = new AutomateReflection(automate);
    }

    @Override
    public List<String> getAlphabet() {
        return null;
    }

    @Override
    public Map<String, Map<String, Set<String>>> getTable() {
       return null;
    }

    @Override
    public Set<String> getBeginState() {
        return null;
    }

    @Override
    public List<String> getEndStates() {
        return null;
    }

    @Override
    public String getName() {
        return AutomateOperations.GENERATE_NAME;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
