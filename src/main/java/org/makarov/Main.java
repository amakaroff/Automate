package org.makarov;

import org.makarov.automate.Automate;
import org.makarov.automate.DeterministicAutomate;
import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.automate.reader.JSONDeterminateAutomateReader;
import org.makarov.automate.reader.JSONNonDeterministicAutomateReader;
import org.makarov.task.TaskOne;
import org.makarov.task.TaskTwo;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        //String filePath = "C:\\Users\\Слава\\IdeaProjects\\automate\\src\\main\\resources\\automate3.json";
        //Automate<String> automate = new DeterministicAutomate(new JSONDeterminateAutomateReader(filePath));
        //System.out.println(TaskTwo.function(automate, "0a0", 1));

        String filePath = "C:\\Users\\Слава\\IdeaProjects\\automate\\src\\main\\resources\\automate1.json";
        DeterministicAutomate automate1 = new DeterministicAutomate(new JSONDeterminateAutomateReader(filePath));
        System.out.println(TaskOne.function(automate1, "+**+**", 0));

        //String filePath = "C:\\Users\\Слава\\IdeaProjects\\automate\\src\\main\\resources\\automate2.json";
        //NonDeterministicAutomate automate2 = new NonDeterministicAutomate(new JSONNonDeterministicAutomateReader(filePath));
        //System.out.println(TaskOne.function(automate2, "0124567", 0));
    }
}
