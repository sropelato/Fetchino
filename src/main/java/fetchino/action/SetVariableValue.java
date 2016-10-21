package fetchino.action;

import fetchino.util.Util;
import fetchino.workflow.Context;

public class SetVariableValue extends SetVariable
{
	private final String value;
	private final String variableName;

	public SetVariableValue(String variableName, String value)
	{
		this.value = value;
		this.variableName = variableName;

		Util.validateVariableName(variableName);
	}

	@Override
	public void execute(Context context)
	{
		context.setVariable(variableName, Util.replacePlaceholders(value, context));
	}
}
