package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import lightdom.Element;

import java.util.List;

public class SaveAll implements Action
{
	private final String path;
	private final String var;

	public SaveAll(Element saveAllElement)
	{
		if(!saveAllElement.hasAttributeWithName("path"))
			throw new RuntimeException("SaveAll has no path attribute");
		else
			path = saveAllElement.getAttribute("path");
		Util.validateXPathExpression(path);

		if(!saveAllElement.hasAttributeWithName("var"))
			throw new RuntimeException("SaveAll has no var attribute");
		else
			var = saveAllElement.getAttribute("var");
		Util.validateVariableName(var);
	}

	@Override
	public void execute(Context context)
	{
		List<HtmlElement> elements = Util.getElementsOfType(context.getCurrentPage(), path, HtmlElement.class);
		elements.forEach(element -> context.addToList(var, element.asText()));
	}
}
