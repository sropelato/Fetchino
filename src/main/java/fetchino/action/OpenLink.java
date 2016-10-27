package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The {@code AddToList} action opens a link.
 *
 * @version 1.0-SNAPSHOT
 */
public class OpenLink implements Action
{
	private final String path;

	/**
	 * Constructor.
	 *
	 * @param path XPath expression defining the link element. This needs to return exactly one HTML anchor element.
	 */
	public OpenLink(String path)
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
		LoggerFactory.getLogger(OpenLink.class).debug("Executing action: " + this);
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

	@Override
	public String toString()
	{
		return "OpenLink{" +
				"path='" + path + '\'' +
				'}';
	}
}
