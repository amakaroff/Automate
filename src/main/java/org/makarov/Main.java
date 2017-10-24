package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.JSONNonDeterministicAutomateReader;

public class Main {

    public static void main(String[] args) {
        Automate automate = new NonDeterministicAutomate(new JSONNonDeterministicAutomateReader("automate2.json"));
    }
}
