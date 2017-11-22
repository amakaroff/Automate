package org.makarov.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.AutomateReflection;
import org.makarov.automate.exception.AutomateException;
import org.makarov.operations.AutomateOperationsUtils;
import org.makarov.util.FileReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LexerParser {

    private static final char LETTER_SEPARATOR = ':';

    private static final char EXPRESSION_SEPARATOR = ';';

    public static Collection<Automate> getAutomates(String filePath) {
        return getAutomates(filePath, false);
    }

    public static Collection<Automate> getAutomates(String filePath, boolean debug) {
        String content = FileReader.readFile(filePath);
        return parseText(content, debug);
    }

    private static Collection<Automate> parseText(String content, boolean debug) {
        int index = 0;

        List<AutomateTemplate> automateTemplates = new ArrayList<>();

        while (!content.substring(index, content.length()).trim().isEmpty() && index < content.length()) {
            String name = readBeforeChar(content, index, LETTER_SEPARATOR);
            index += name.length() + 1;
            name = name.trim();

            String priority = readBeforeChar(content, index, LETTER_SEPARATOR);
            index += priority.length() + 1;
            priority = priority.trim();

            String regex = readBeforeChar(content, index, EXPRESSION_SEPARATOR);
            index += regex.length() + 1;
            regex = regex.trim();

            if (name.isEmpty() || priority.isEmpty() || regex.isEmpty() || !AutomateOperationsUtils.isNumber(priority)) {
                int lineNumber = countLineSeparators(content, index);
                int positionIndex = index - content.lastIndexOf("\n", index);
                throw new AutomateException("Automate reading error. Line: " + lineNumber + ", " + "Position: " + positionIndex);
            }

            AutomateTemplate automateTemplate = new AutomateTemplate(name, Integer.parseInt(priority), regex);
            log(debug, automateTemplate.toString());
            automateTemplates.add(automateTemplate);
        }

        List<Automate> automates = new ArrayList<>();
        for (AutomateTemplate automateTemplate : automateTemplates) {
            automates.add(generate(automateTemplate));
        }

        return automates;
    }


    private static int countLineSeparators(String content, int index) {
        int count = 0;
        for (int i = 0; i < index; i++) {
            if (content.charAt(i) == '\n') {
                count++;
            }
        }

        return count;
    }

    private static String readBeforeChar(String content, int index, char separator) {
        StringBuilder lineBuilder = new StringBuilder();
        char character = content.charAt(index);
        while (character != separator && index + 1 < content.length()) {
            lineBuilder.append(character);
            index++;
            character = content.charAt(index);
        }

        return lineBuilder.toString();
    }

    private static void log(boolean debug, String message) {
        if (debug) {
            System.out.println(message);
        }
    }

    private static <T> Automate generate(AutomateTemplate template) {
        @SuppressWarnings("unchecked")
        Automate<T> automate = RegexParser.parseRegex(template.getRegularExpression());
        AutomateReflection<T> reflection = new AutomateReflection<>(automate);
        reflection.setName(template.getName());
        reflection.setPriority(template.getPriority());

        return reflection.getAutomate();
    }

    public static class AutomateTemplate {

        private String name;

        private int priority;

        private String regularExpression;

        public AutomateTemplate(String name, int priority, String regularExpression) {
            this.name = name;
            this.priority = priority;
            this.regularExpression = regularExpression;
        }

        public String getName() {
            return name;
        }

        public int getPriority() {
            return priority;
        }

        public String getRegularExpression() {
            return regularExpression;
        }

        @Override
        public String toString() {
            return "AutomateTemplate {" +
                    "\n\tname = \"" + name + "\";" +
                    "\n\tpriority = " + priority + ";" +
                    "\n\tregularExpression = \"" + regularExpression + "\";\n}\n";
        }
    }
}
