package fetchino.condition;

import fetchino.workflow.Context;

import java.util.ArrayList;
import java.util.List;

public class Not extends Matches
{
	private final Condition condition;

	public Not(Condition condition)
	{
		this.condition = condition;
	}

	@Override
	public boolean test(Context context)
	{
		return !condition.test(context);
	}
}
