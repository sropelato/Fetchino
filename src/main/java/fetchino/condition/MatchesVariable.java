package fetchino.condition;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

public class MatchesVariable extends Matches
{
	private final String variableName;
	private final String regex;

	public MatchesVariable(String variableName, String regex)
	{
		this.variableName = variableName;
		this.regex = regex;
	}

	@Override
	public boolean test(Context context)
	{
		return context.getVariable(variableName).matches(regex);
	}
}
