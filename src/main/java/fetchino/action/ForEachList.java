package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import fetchino.workflow.TempContext;

import java.util.List;

public class ForEachList extends ForEach
{
	private final String listName;
	private final String var;
	private final List<Action> actions;

	public ForEachList(String listName, String var, List<Action> actions)
	{
		this.listName = listName;
		this.var = var;
		this.actions = actions;

		Util.validateVariableName(var);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		if(!context.hasList(listName))
			throw new RuntimeException("List does not exist: " + listName);
		for(String value : context.getList(listName))
		{
			TempContext tempContext = new TempContext(context);
			tempContext.setTempVariable(var, value);

			for(Action action : actions)
			{
				action.execute(webClient, tempContext);
			}
		}
	}
}
