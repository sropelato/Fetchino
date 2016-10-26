package fetchino.condition;

import fetchino.context.Context;

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
		return context.getVariable(variableName).matches(regex);
	}
}
