package fetchino.action;

import fetchino.context.Context;

/**
 * This interface defines an action to be executed as part of a workflow.
 *
 * @version 1.0-SNAPSHOT
 */
public interface Action
{
	/**
	 * Executes the action.
	 *
	 * @param context The context in which the action is executed.
	 */
	void execute(Context context);
}
