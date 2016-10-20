package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;

public class Save implements Action
{
	private final String path;
	private final String var;

	public Save(String path, String var)
	{
		this.path = path;
		this.var = var;

		Util.validateXPathExpression(path);
		Util.validateVariableName(var);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		DomNode element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(webClient), path, DomNode.class);
		context.setVariable(var, element.asText());
	}
}
