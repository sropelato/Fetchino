package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.main.Fetchino;
import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The {@code AddToList} action adds a new element to a list.
 *
 * @version 1.0-SNAPSHOT
 */
public class AddToList implements Action
{
	private final String listName;
	private final String path;

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
		LoggerFactory.getLogger(AddToList.class).debug("Executing action: " + this);
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		elements.forEach(element -> context.addToList(listName, (element instanceof DomAttr) ? element.getNodeValue() : element.asText()));
	}

	@Override
	public String toString()
	{
		return "AddToList{" +
				"listName='" + listName + '\'' +
				", path='" + path + '\'' +
				'}';
	}
}
