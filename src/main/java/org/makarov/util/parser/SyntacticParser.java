package org.makarov.util.parser;

import org.makarov.automate.Automate;
import org.makarov.util.Functions;
import org.makarov.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SyntacticParser {

	private Rule currentRule;



	public static void parseText(String line, Collection<Rule> rules, Collection<Automate> lexicon) {
		List<Pair<String, String>> tokens = new ArrayList<>(Functions.getLexemes(lexicon, line));
		parseText0(tokens, 0, null);
	}

	public static void parseText0(List<Pair<String, String>> tokens, int tokenIndex, Rule rule) {

	}
}
