package fetchino.condition;

import fetchino.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code And} condition is satisfied iff all of the nested conditions are satisfied.
 *
 * @version 1.0-SNAPSHOT
 */
public class And implements Condition
{
	private final List<Condition> conditions = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * @param conditions List of nested conditions.
	 */
	public And(List<Condition> conditions)
	{
		this.conditions.addAll(conditions);

		if(conditions.isEmpty())
			throw new RuntimeException("conditions cannot be empty");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(Context context)
	{
		for(Condition condition : conditions)
		{
			if(!condition.test(context))
				return false;
		}
		return true;
	}
}
