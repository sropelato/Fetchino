package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;

import java.io.IOException;

public class OpenLink implements Action
{
	private final String path;

	public OpenLink(String path)
	{
		this.path = path;
		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		HtmlAnchor element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(webClient), path, HtmlAnchor.class);

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
