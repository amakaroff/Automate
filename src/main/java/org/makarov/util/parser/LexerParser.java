package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.automate.exception.AutomateException;
import org.makarov.util.AutomateReflection;
import org.makarov.util.FileUtils;

import java.util.*;

public class LexerParser {

    private static final String NAME = "name";

    private static final String COLON = "colon";

    private static final String PRIORITY = "priority";

    private static final String SEMICOLON = "semicolon";

    private static final String SPACE = "space";

    private static final String REGEX = "regex";

    public static Collection<Automate> getAutomates(String filePath) {
        String content = FileUtils.readFile(filePath);
        return parseText(content);
    }

    private static Collection<Automate> parseText(String content) {
        int index = 0;

        List<AutomateTemplate> automateTemplates = new ArrayList<>();

        while (index < content.length()) {
            char character;
            index = skipSpace(content, index);
            character = content.charAt(index);

            StringBuilder nameBuilder = new StringBuilder();
            while (!isSpace(character) || character != ':') {
                nameBuilder.append(character);
                index++;
                character = content.charAt(index);
            }

            index = skipSeparator(content, index);
            character = content.charAt(index);

            StringBuilder priorityBuilder = new StringBuilder();
            while (Character.isDigit(character)) {
                priorityBuilder.append(character);
                index++;
                character = content.charAt(index);
            }

            index = skipSeparator(content, index);
            character = content.charAt(index);

            StringBuilder regexBuilder = new StringBuilder();

            while (!isSpace(character) || character != ';') {
                regexBuilder.append(character);
                index++;
                character = content.charAt(index);
            }

            index = skipSpace(content, index);

            if (content.charAt(index) != ';') {
                throw new AutomateException("Separator is not fould!");
            }
            index++;

            automateTemplates.add(new AutomateTemplate(nameBuilder.toString(),
                    Integer.parseInt(priorityBuilder.toString()), regexBuilder.toString()));
        }

        LexicalEnvironment lexicalEnvironment = new LexicalEnvironment(automateTemplates);

        return lexicalEnvironment.getAutomates();
    }

    private static int skipSeparator(String content, int index) {
        index = skipSpace(content, index);
        char character = content.charAt(index);

        if (character != ':') {
            throw new AutomateException("Separator is not found!");
        }
        index++;

        return skipSpace(content, index);
    }

    private static int skipSpace(String line, int index) {
        char character = line.charAt(index);
        while (isSpace(character)) {
            index++;
            character = line.charAt(index);
        }

        return index;
    }


    private static boolean isSpace(char symbol) {
        return symbol == ' ' || symbol == '\n' || symbol == '\t' || symbol == '\r';
    }

    @SuppressWarnings("unchecked")
    public static Automate generate(AutomateTemplate template) {
        Automate automate = RegexParser.parseRegex(template.getRegularExpression());
        AutomateReflection reflection = new AutomateReflection(automate);
        reflection.setName(template.getName());
        reflection.setPriority(template.getPriority());

        return reflection.getAutomate();
    }

    public static class LexicalEnvironment {

        Map<String, AutomateTemplate> automateTemplates;

        Map<String, Automate> automates;

        public LexicalEnvironment(List<AutomateTemplate> automateTemplates) {
            this.automateTemplates = new HashMap<>();
            for (AutomateTemplate template : automateTemplates) {
                this.automateTemplates.put(template.getName(), template);
            }
            automates = new HashMap<>();

            for (String key : this.automateTemplates.keySet()) {
                automates.computeIfAbsent(key, put -> getAutomate(key));
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
    }
}
