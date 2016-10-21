package fetchino.condition;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

import java.util.ArrayList;
import java.util.List;

public class And extends Matches
{
	private final List<Condition> conditions = new ArrayList<>();

	public And(List<Condition> conditions)
	{
		this.conditions.addAll(conditions);

		if(conditions.isEmpty())
			throw new RuntimeException("conditions cannot be empty");
	}

	@Override
	public boolean test(Context context)
	{
		for(Condition condition : conditions)
		{
			if(!condition.test(context))
				return false;
		}
		return true;
	}
}
