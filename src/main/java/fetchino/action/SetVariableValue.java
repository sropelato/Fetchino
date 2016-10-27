package fetchino.action;

import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

/**
 * The {@code SetVariableValue} action assigns the given value to a variable.
 *
 * @version 1.0-SNAPSHOT
 */
public class SetVariableValue extends SetVariable
{
	private final String variableName;
	private final String value;

	/**
	 * Constructor.
	 *
	 * @param variableName Name of the variable.
	 * @param value        Value of the variable. This may contain variable placeholders.
	 */
	public SetVariableValue(String variableName, String value)
	{
		this.variableName = variableName;
		this.value = value;

		Util.validateVariableName(variableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(SetVariableValue.class).debug("Executing action: " + this);
		context.setVariable(variableName, Util.replacePlaceholders(value, context));
	}

	@Override
	public String toString()
	{
		return "SetVariableValue{" +
				"variableName='" + variableName + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
