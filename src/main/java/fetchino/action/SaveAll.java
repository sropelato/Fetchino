package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
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

	public SaveAll(String path, String var)
	{
		this.path = path;
		this.var = var;

		Util.validateXPathExpression(path);
		Util.validateVariableName(var);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(webClient), path, DomNode.class);
		elements.forEach(element -> context.addToList(var, (element instanceof DomAttr) ? element.getNodeValue() : element.asText()));
	}
}
