package fetchino.condition;

import fetchino.context.Context;

/**
 * The {@code Not} condition is satisfied iff the nested condition is not satisfied.
 *
 * @version 1.0-SNAPSHOT
 */
public class Not implements Condition
{
	private final Condition condition;

	/**
	 * Constructor.
	 *
	 * @param condition Nested condition.
	 */
	public Not(Condition condition)
	{
		this.condition = condition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(Context context)
	{
		return !condition.test(context);
	}
}
