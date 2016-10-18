package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import lightdom.Element;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClickElement implements Action
{
	private final String path;

	public ClickElement(Element clickFormButtonElement)
	{
		if(!clickFormButtonElement.hasAttributeWithName("path"))
			throw new RuntimeException("ClickFormButton has no path attribute");
		else
			path = clickFormButtonElement.getAttribute("path");
		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(Context context)
	{
		HtmlElement element = Util.getSingleElementOfType(context.getCurrentPage(), path, HtmlElement.class);
		try
		{
			context.setCurrentPage(element.click());
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
