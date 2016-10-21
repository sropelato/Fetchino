package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

import java.util.List;

public class AddToList implements Action
{
	private final String path;
	private final String listName;

	public AddToList(String listName, String path)
	{
		this.listName = listName;
		this.path = path;

		Util.validateVariableName(listName);
		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(Context context)
	{
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		elements.forEach(element -> context.addToList(listName, (element instanceof DomAttr) ? element.getNodeValue() : element.asText()));
	}
}
