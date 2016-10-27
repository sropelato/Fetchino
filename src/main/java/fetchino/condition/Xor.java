package fetchino.condition;

import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Xor} condistion is satisfied iff exactly one of the nested conditions are satisfied.
 *
 * @version 1.0-SNAPSHOT
 */
public class Xor implements Condition
{
	private final List<Condition> conditions = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * @param conditions List of nested conditions.
	 */
	public Xor(List<Condition> conditions)
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
		LoggerFactory.getLogger(Xor.class).debug("Testing condition: " + this);
		int positive = 0;
		for(Condition condition : conditions)
		{
			if(condition.test(context))
				positive++;
			if(positive > 1)
				return false;
		}
		return positive == 1;
	}

	@Override
	public String toString()
	{
		return "Xor{" +
				"conditions=" + conditions +
				'}';
	}
}
