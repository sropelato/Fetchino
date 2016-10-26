package fetchino.action;

import fetchino.condition.Condition;
import fetchino.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code If} action executes nested if the defined condition is satisfied.
 *
 * @version 1.0-SNAPSHOT
 */
public class If implements Action
{
	private final Condition condition;
	private final List<Action> thenActions = new ArrayList<>();
	private final List<Action> elseActions = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * @param condition   The {@link Condition} object which is tested.
	 * @param thenActions List of actions to be performed if the condition is satisfied.
	 * @param elseActions List of actions to be performed if the condition is not satisfied.
	 */
	public If(Condition condition, List<Action> thenActions, List<Action> elseActions)
	{
		this.condition = condition;
		if(thenActions != null)
			this.thenActions.addAll(thenActions);
		if(elseActions != null)
			this.elseActions.addAll(elseActions);

		if(thenActions.isEmpty())
			throw new RuntimeException("then actions cannot be empty");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		if(condition.test(context))
			thenActions.forEach(action -> action.execute(context));
		else
			elseActions.forEach(action -> action.execute(context));
	}
}
