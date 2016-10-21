package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import fetchino.util.Util;
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
	public void execute(Context context)
	{
		HtmlAnchor element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlAnchor.class);

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
