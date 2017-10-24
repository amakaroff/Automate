package org.makarov.task;


import org.makarov.automate.Automate;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

public class TaskTree {

    public static Collection<Pair<String, String>> getLexems(String line) {
        Collection<Automate<String>> automates = new ArrayList<>();

        return Functions.getLexems(automates, line);
    }
}
