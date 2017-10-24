package org.makarov;

import org.makarov.task.TaskTree;
import org.makarov.util.FileUtils;
import org.makarov.util.Pair;

public class Main {

    public static void main(String[] args) {
        for (Pair<String, String> stringStringPair : TaskTree.getLexems(FileUtils.readFile("code.txt"))) {
            System.out.println(stringStringPair);
        }
    }
}
