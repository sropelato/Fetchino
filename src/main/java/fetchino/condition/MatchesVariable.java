package fetchino.condition;

import fetchino.context.Context;
import org.slf4j.LoggerFactory;

/**
 * The {@code Matches} condition is satisfied iff a variable matches a given regex pattern.
 *
 * @version 1.0-SNAPSHOT
 */
public class MatchesVariable extends Matches
{
	private final String variableName;
	private final String regex;

	/**
	 * Constructor.
	 *
	 * @param variableName Name of the variable.
	 * @param regex        Regular expression.
	 */
	public MatchesVariable(String variableName, String regex)
	{
		this.variableName = variableName;
		this.regex = regex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(Context context)
	{
		LoggerFactory.getLogger(MatchesVariable.class).debug("Testing condition: " + this);
		return context.getVariable(variableName).matches(regex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "MatchesVariable{" +
				"variableName='" + variableName + '\'' +
				", regex='" + regex + '\'' +
				'}';
	}
}
