package fetchino.action;

import fetchino.condition.Condition;
import fetchino.workflow.Context;

import java.util.ArrayList;
import java.util.List;

public class If implements Action
{
	private final Condition condition;
	private final List<Action> thenActions = new ArrayList<>();
	private final List<Action> elseActions = new ArrayList<>();

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

	@Override
	public void execute(Context context)
	{
		if(condition.test(context))
			thenActions.forEach(action -> action.execute(context));
		else
			elseActions.forEach(action -> action.execute(context));
	}
}
