package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;

public class Main {

    public static void main(String[] args) {
        Automate automate = new DeterministicAutomate(new JSONDeterminateAutomateReader("automate1.json"));
        automate.init();
    }
}
