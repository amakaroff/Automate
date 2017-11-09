package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.util.AutomateReflection;
import org.makarov.util.FileReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexerParser {

    public static Collection<Automate> getAutomates(String filePath) {
        String content = FileReader.readFile(filePath);
        return parseText(content);
    }

    private static Collection<Automate> parseText(String content) {
        int index = 0;

        List<AutomateTemplate> automateTemplates = new ArrayList<>();

        while (!content.substring(index, content.length()).trim().isEmpty() && index < content.length()) {
            String name = readBeforeChar(content, index, ':');
            index += name.length() + 1;
            name = name.trim();

            String priority = readBeforeChar(content, index, ':');
            index += priority.length() + 1;
            priority = priority.trim();

            String regex = readBeforeChar(content, index, ';');
            index += regex.length() + 1;
            regex = regex.trim();

            if (name.isEmpty() || priority.isEmpty() || regex.isEmpty()) {
                int lineNumber = countLineSeparators(content, index);
                int positionIndex = index - content.lastIndexOf("\n", index);
                throw new AutomateException("Automate reading error. Line: " + lineNumber + ", " + "Position: " + positionIndex);
            }

            AutomateTemplate automateTemplate = new AutomateTemplate(name, Integer.parseInt(priority), regex);
            automateTemplates.add(automateTemplate);
        }

        LexicalEnvironment lexicalEnvironment = new LexicalEnvironment(automateTemplates);

        return lexicalEnvironment.getAutomates();
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

    @SuppressWarnings("unchecked")
    private static Automate generate(AutomateTemplate template) {
        Automate automate = RegexParser.parseRegex(template.getRegularExpression());
        AutomateReflection reflection = new AutomateReflection(automate);
        reflection.setName(template.getName());
        reflection.setPriority(template.getPriority());

        return reflection.getAutomate();
    }

    public static class LexicalEnvironment {

        private Map<String, AutomateTemplate> automateTemplates;

        private Map<String, Automate> automates;

        public LexicalEnvironment(List<AutomateTemplate> automateTemplates) {
            this.automateTemplates = new HashMap<>();
            for (AutomateTemplate template : automateTemplates) {
                this.automateTemplates.put(template.getName(), template);
            }
            automates = new HashMap<>();

            for (String key : this.automateTemplates.keySet()) {
                automates.computeIfAbsent(key, value -> getAutomate(key));
            }
        }

        public Automate getAutomate(String name) {
            Automate automate = automates.get(name);
            if (automate == null) {
                AutomateTemplate automateTemplate = automateTemplates.get(name);
                if (automateTemplate == null) {
                    throw new AutomateException("Automate " + name + " is not found!");
                } else {
                    Automate generate = generate(automateTemplate);
                    automates.put(name, generate);
                    return generate;
                }
            } else {
                return automate;
            }
        }

        public Collection<Automate> getAutomates() {
            return automates.values();
        }
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
