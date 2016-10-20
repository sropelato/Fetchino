package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import fetchino.workflow.TempContext;

import java.util.List;

public class ForEachPath extends ForEach
{
	private final String path;
	private final String var;
	private final List<Action> actions;

	public ForEachPath(String path, String var, List<Action> actions)
	{
		this.path = path;
		this.var = var;
		this.actions = actions;

		Util.validateXPathExpression(path);
		Util.validateVariableName(var);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(webClient), path, DomNode.class);
		for(DomNode element : elements)
		{
			String elementValue = (element instanceof DomAttr) ? element.getNodeValue() : element.asText();
			TempContext tempContext = new TempContext(context);
			tempContext.setTempVariable(var, elementValue);

			for(Action action : actions)
			{
				action.execute(webClient, tempContext);
			}
		}
	}
}
