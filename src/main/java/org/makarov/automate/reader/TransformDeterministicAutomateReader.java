package org.makarov.automate.reader;

import org.makarov.automate.NonDeterministicAutomate;
import org.makarov.util.AutomateReflection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransformDeterministicAutomateReader implements AutomateReader<String> {

	AutomateReflection<Set<String>> automateReflection;

	public TransformDeterministicAutomateReader(NonDeterministicAutomate automate) {
		this.automateReflection = new AutomateReflection<>(automate);
	}

	@Override
	public List<String> getAlphabet() {
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getTable() {
		return null;
	}

	@Override
	public String getBeginState() {
		return null;
	}

	@Override
	public List<String> getEndStates() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
