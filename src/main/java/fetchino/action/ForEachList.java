package fetchino.action;

import fetchino.util.Util;
import fetchino.context.Context;
import fetchino.context.TempContext;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The {@code ForEachList} action executes nested actions for each element in a list.
 *
 * @version 1.0-SNAPSHOT
 */
public class ForEachList extends ForEach
{
	private final String listName;
	private final String var;
	private final List<Action> actions;
	private final String limit;

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param var      Name of the temporary variable containing the element of the current iteration.
	 * @param actions  List of actions executed in each iteration.
	 */
	public ForEachList(String listName, String var, List<Action> actions)
	{
		this(listName, var, actions, null);
	}

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param var      Name of the temporary variable containing the list element of the current iteration.
	 * @param actions  List of actions executed in each iteration.
	 * @param limit    Mamimum number of iterations to be executed. This may contain variable placeholders. A value of {@code null} or an expression evaluated to a number less than 1 means no limit.
	 */
	public ForEachList(String listName, String var, List<Action> actions, String limit)
	{
		this.listName = listName;
		this.var = var;
		this.actions = actions;
		this.limit = limit;

		Util.validateVariableName(var);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(ForEachList.class).debug("Executing action: " + this);
		if(!context.hasList(listName))
			throw new RuntimeException("List does not exist: " + listName);

		int count = 0;
		for(String value : context.getList(listName))
		{
			if(limit != null && Integer.parseInt(Util.replacePlaceholders(limit, context)) > 0 && count >= Integer.parseInt(Util.replacePlaceholders(limit, context)))
				break;

			TempContext tempContext = new TempContext(context, false);
			tempContext.setTempVariable(var, value);

			for(Action action : actions)
			{
				action.execute(tempContext);
			}

			count++;
		}
	}

	@Override
	public String toString()
	{
		return "ForEachList{" +
				"listName='" + listName + '\'' +
				", var='" + var + '\'' +
				", actions=" + actions +
				", limit='" + limit + '\'' +
				'}';
	}
}
