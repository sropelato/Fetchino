package fetchino.condition;

import fetchino.workflow.Context;

public interface Condition
{
	boolean test(Context context);
}
