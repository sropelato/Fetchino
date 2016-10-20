package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;

import java.io.IOException;

public class ClickElement implements Action
{
	private final String path;

	public ClickElement(String path)
	{
		this.path = path;
		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		HtmlElement element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(webClient), path, HtmlElement.class);
		try
		{
			element.click();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
