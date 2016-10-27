package fetchino.condition;

import fetchino.context.Context;
import org.slf4j.LoggerFactory;

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
		LoggerFactory.getLogger(Not.class).debug("Testing condition: " + this);
		return !condition.test(context);
	}

	@Override
	public String toString()
	{
		return "Not{" +
				"condition=" + condition +
				'}';
	}
}
