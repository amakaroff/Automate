package org.makarov.util.parser;


import java.util.List;

public class Rule {

	private String name;

	private List<List<String>> expressions;

	public Rule(String name, List<List<String>> expressions) {
		this.name = name;
		this.expressions = expressions;
	}

	public String getName() {
		return name;
	}

	public List<List<String>> getExpressions() {
		return expressions;
	}

	public void addRule(List<String> rule) {
		expressions.add(rule);
	}
}
