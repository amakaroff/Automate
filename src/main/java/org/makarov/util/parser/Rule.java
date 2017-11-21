package org.makarov.util.parser;


import java.util.List;

public class Rule {

	private String name;

	private Expression expression;

	public Rule(String name, Expression expression) {
		this.name = name;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	public static class Expression {

		private List<Symbol> symbols;

		public Expression(List<Symbol> symbols) {
			this.symbols = symbols;
		}

		public List<Symbol> getSymbols() {
			return symbols;
		}

		public void add(Symbol symbol) {
			symbols.add(symbol);
		}
	}

	public interface Symbol {

		boolean isTerminate();

		String getSymbol();
	}

	public static class TerminateSymbol implements Symbol {

		private String symbol;

		public TerminateSymbol(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String getSymbol() {
			return symbol;
		}

		@Override
		public boolean isTerminate() {
			return true;
		}
	}

	public static class NonTerminateSymbol implements Symbol {

		private String symbol;

		public NonTerminateSymbol(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String getSymbol() {
			return symbol;
		}

		@Override
		public boolean isTerminate() {
			return false;
		}
	}
}
