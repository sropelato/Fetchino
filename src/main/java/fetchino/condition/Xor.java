package fetchino.condition;

import fetchino.workflow.Context;

import java.util.ArrayList;
import java.util.List;

public class Xor extends Matches
{
	private final List<Condition> conditions = new ArrayList<>();

	public Xor(List<Condition> conditions)
	{
		this.conditions.addAll(conditions);

		if(conditions.isEmpty())
			throw new RuntimeException("conditions cannot be empty");
	}

	@Override
	public boolean test(Context context)
	{
		int positive = 0;
		for(Condition condition : conditions)
			if(condition.test(context))
				positive++;
		return positive == 1;
	}
}
