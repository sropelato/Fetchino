package fetchino.condition;

import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Or} condition is satisfied iff at least one of the nested conditions are satisfied.
 *
 * @version 1.0-SNAPSHOT
 */
public class Or implements Condition
{
	private final List<Condition> conditions = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * @param conditions List of nested conditions.
	 */
	public Or(List<Condition> conditions)
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
		LoggerFactory.getLogger(Or.class).debug("Testing condition: " + this);
		for(Condition condition : conditions)
		{
			if(condition.test(context))
				return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "Or{" +
				"conditions=" + conditions +
				'}';
	}
}
