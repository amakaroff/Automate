package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.reader.JSONNonDeterministicAutomateReader;
import org.makarov.task.TaskTwo;

public class Main {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\makarovaa\\IdeaProjects\\Automates\\src\\main\\resources\\automate1.json";
        DeterministicAutomate automate1 = new DeterministicAutomate(new JSONDeterminateAutomateReader(filePath));
        //System.out.println(TaskOne.function(automate1, "00101010", 0));

        filePath = "C:\\Users\\makarovaa\\IdeaProjects\\Automates\\src\\main\\resources\\automate2.json";
        NonDeterministicAutomate automate2 = new NonDeterministicAutomate(new JSONNonDeterministicAutomateReader(filePath));
        //System.out.println(TaskOne.function(automate2, "01121224567", 0));

        filePath = "C:\\Users\\makarovaa\\IdeaProjects\\Automates\\src\\main\\resources\\automate3.json";
        Automate<String> automate3 = new DeterministicAutomate(new JSONDeterminateAutomateReader(filePath));
        //System.out.println(TaskTwo.function(automate3, "-0.0e5+9", 0));
        System.out.println(TaskTwo.findAllNumbers(automate3, "-.0e5a1+92.d.03e-3.25e+a++23.e45+4+e6++.45+e45.+5e+4..6e+7"));
    }
}
