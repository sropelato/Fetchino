package fetchino.condition;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;

/**
 * The {@code Matches} condition is satisfied iff the result of an XPath matches a given regex pattern.
 *
 * @version 1.0-SNAPSHOT
 */
public class MatchesPath extends Matches
{
	private final String path;
	private final String regex;

	/**
	 * Constructor.
	 *
	 * @param path  XPath expression to be tested for a match.
	 * @param regex Regular expression.
	 */
	public MatchesPath(String path, String regex)
	{
		this.path = path;
		this.regex = regex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean test(Context context)
	{
		DomNode element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		String elementValue = (element instanceof DomAttr) ? element.getNodeValue() : element.asText();
		return elementValue.matches(regex);
	}
}
