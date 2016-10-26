package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;

import java.util.List;

/**
 * The {@code AddToList} action adds a new element to a list.
 *
 * @version 1.0-SNAPSHOT
 */
public class AddToList implements Action
{
	private final String path;
	private final String listName;

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param path     XPath expression defining the elements to be added to the list.
	 */
	public AddToList(String listName, String path)
	{
		this.listName = listName;
		this.path = path;

		Util.validateVariableName(listName);
		Util.validateXPathExpression(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		elements.forEach(element -> context.addToList(listName, (element instanceof DomAttr) ? element.getNodeValue() : element.asText()));
	}
}
