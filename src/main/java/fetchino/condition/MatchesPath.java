package fetchino.condition;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

public class MatchesPath extends Matches
{
	private final String path;
	private final String regex;

	public MatchesPath(String path, String regex)
	{
		this.path = path;
		this.regex = regex;
	}

	@Override
	public boolean test(Context context)
	{
		DomNode element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		String elementValue = (element instanceof DomAttr) ? element.getNodeValue() : element.asText();
		return elementValue.matches(regex);
	}
}
