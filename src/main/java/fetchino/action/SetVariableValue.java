package fetchino.action;

import fetchino.util.Util;
import fetchino.context.Context;

/**
 * The {@code SetVariableValue} action assigns the given value to a variable.
 *
 * @version 1.0-SNAPSHOT
 */
public class SetVariableValue extends SetVariable
{
	private final String value;
	private final String variableName;

	/**
	 * Constructor.
	 *
	 * @param variableName Name of the variable.
	 * @param value        Value of the variable. This may contain variable placeholders.
	 */
	public SetVariableValue(String variableName, String value)
	{
		this.value = value;
		this.variableName = variableName;

		Util.validateVariableName(variableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		context.setVariable(variableName, Util.replacePlaceholders(value, context));
	}
}
