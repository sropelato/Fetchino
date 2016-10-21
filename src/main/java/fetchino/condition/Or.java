package fetchino.condition;

import fetchino.workflow.Context;

import java.util.ArrayList;
import java.util.List;

public class Or extends Matches
{
	private final List<Condition> conditions = new ArrayList<>();

	public Or(List<Condition> conditions)
	{
		this.conditions.addAll(conditions);

		if(conditions.isEmpty())
			throw new RuntimeException("conditions cannot be empty");
	}

	@Override
	public boolean test(Context context)
	{
		for(Condition condition : conditions)
		{
			if(condition.test(context))
				return true;
		}
		return false;
	}
}
