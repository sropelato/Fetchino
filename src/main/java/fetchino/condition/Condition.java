package fetchino.condition;

import fetchino.action.If;
import fetchino.context.Context;

/**
 * This interface defines a condition which is tested in the {@link If} action.
 *
 * @version 1.0-SNAPSHOT
 */
public interface Condition
{
	/**
	 * Test the condition in the given context.
	 *
	 * @param context The context in which the condition is tested.
	 * @return {@code true} iff the condition is satisfied.
	 */
	boolean test(Context context);
}
