package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.context.Context;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The {@code AddToListPath} action adds elements matching an XPath query to a list.
 *
 * @version 1.0-SNAPSHOT
 */
public class AddToListPath extends AddToList
{
	private final String listName;
	private final String path;

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param path     XPath expression defining the elements to be added to the list.
	 */
	public AddToListPath(String listName, String path)
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
		LoggerFactory.getLogger(AddToListPath.class).debug("Executing action: " + this);
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		elements.forEach(element -> context.addToList(listName, (element instanceof DomAttr) ? element.getNodeValue().trim() : element.asText().trim()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "AddToListPath{" +
				"listName='" + listName + '\'' +
				", path='" + path + '\'' +
				'}';
	}
}
