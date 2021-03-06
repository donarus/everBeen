package cz.cuni.mff.d3s.been.client;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import cz.cuni.mff.d3s.been.api.BeenApiException;
import cz.cuni.mff.d3s.been.persistence.DAOException;

/**
 * @author Martin Sixta
 * 
 *         WARNING: this code is in incubator phase
 * 
 */
abstract class AbstractMode implements IMode {
	protected String prompt;
	protected String[] actions;
	protected ConsoleReader reader;
	protected PrintWriter out;

	@Override
	public String getPrompt() {
		return prompt;
	}

	@Override
	public String[] getActions() {
		return actions;
	}

	protected AbstractMode(ConsoleReader reader, String prompt, String[] actions) {
		this.prompt = prompt;
		this.actions = actions;

		setup(reader);

	}

	@Override
	public void setup(ConsoleReader reader) {
		this.reader = reader;

		out = new PrintWriter(reader.getOutput());

		for (Completer completer : reader.getCompleters()) {
			reader.removeCompleter(completer);
		}

		List<Completer> completors = new LinkedList<>();
		completors.add(new StringsCompleter(actions));

		for (Completer c : completors) {
			reader.addCompleter(c);
		}

		reader.setPrompt(prompt);
	}

	@Override
	public IMode takeAction(String[] args) throws DAOException, BeenApiException {
		for (String arg : args) {
			out.println(arg);
		}

		return this;
	}
}
