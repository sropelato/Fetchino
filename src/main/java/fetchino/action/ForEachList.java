package fetchino.action;

import fetchino.util.Util;
import fetchino.context.Context;
import fetchino.context.TempContext;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
	private final boolean parallel;

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param var      Name of the temporary variable containing the element of the current iteration.
	 * @param actions  List of actions executed in each iteration.
	 */
	public ForEachList(String listName, String var, List<Action> actions)
	{
		this(listName, var, actions, null, false);
	}

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param var      Name of the temporary variable containing the list element of the current iteration.
	 * @param actions  List of actions executed in each iteration.
	 * @param limit    Mamimum number of iterations to be executed. This may contain variable placeholders. A value of {@code null} or an expression evaluated to a number less than 1 means no limit.
	 * @param parallel Process actions parallel.
	 */
	public ForEachList(String listName, String var, List<Action> actions, String limit, boolean parallel)
	{
		this.listName = listName;
		this.var = var;
		this.actions = actions;
		this.limit = limit;
		this.parallel = parallel;

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
		List<Thread> threads = new ArrayList<>();

		int count = 0;
		for(String value : context.getList(listName))
		{
			if(limit != null && Integer.parseInt(Util.replacePlaceholders(limit, context)) > 0 && count >= Integer.parseInt(Util.replacePlaceholders(limit, context)))
				break;

			final TempContext tempContext = new TempContext(context, false);
			tempContext.setTempVariable(var, value);

			if(parallel)
			{
				threads.add(new Thread(() ->
				{
					for(Action action : actions)
					{
						action.execute(tempContext);
					}
				}));
			}
			else
			{
				for(Action action : actions)
				{
					action.execute(tempContext);
				}
			}

			count++;
		}

		if(parallel)
		{
			threads.forEach(Thread::start);
			for(Thread thread : threads)
			{
				try
				{
					thread.join();
				}
				catch(InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
