package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import fetchino.util.Util;
import fetchino.context.Context;

import java.io.IOException;

/**
 * The {@code ClickElement} action clicks a website element.
 *
 * @version 1.0-SNAPSHOT
 */
public class ClickElement implements Action
{
	private final String path;

	/**
	 * Constructor.
	 *
	 * @param path XPath expression defining the element to be clicked. This must return exactly one element.
	 */
	public ClickElement(String path)
	{
		this.path = path;
		Util.validateXPathExpression(path);
	}

	/**
	 * {@inheritDoc}
	 */
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
