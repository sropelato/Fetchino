package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import fetchino.util.Util;
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
	public void execute(Context context)
	{
		HtmlElement element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlElement.class);
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
