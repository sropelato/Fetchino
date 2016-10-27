package fetchino.util;

import com.gargoylesoftware.htmlunit.html.DomNode;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code XPathProcessor} executes XPath queries on DOM nodes.
 *
 * @version 1.0-SNAPSHOT
 */
public class XPathProcessor
{
	/**
	 * Retrieves a single element from a {@link DomNode} by a given XPath expression.
	 * If exactly one object of the expected type is returned, this method returns the cast object.
	 * An exception is thrown if no object is found, more than one object is found or the object found is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the element is retrieved.
	 * @param expression XPath expression.
	 * @param type       Class of expected type of returned object.
	 * @param <T>        Expected type of returned object.
	 * @return Retrieved object cast into type {@code T}.
	 */
	public <T> T getSingleElementOfType(DomNode node, String expression, Class<T> type)
	{
		List<?> elements = node.getByXPath(expression);
		if(elements.size() == 0)
			throw new RuntimeException("No element has been found for expression " + expression);
		else if(elements.size() > 1)
			throw new RuntimeException("Expecting exactly one element but " + elements.size() + " have been found for expression " + expression);
		else if(!(type.isInstance(elements.get(0))))
			throw new RuntimeException("Element for expression " + expression + " is not of type " + type.getName());

		LoggerFactory.getLogger(XPathProcessor.class).debug("Found 1 element matching XPath expression " + expression);
		return (T)elements.get(0);
	}

	/**
	 * Retrieves elements from a {@link DomNode} by a given XPath expression.
	 * If all objects are of a type compatible with {@code T}, this method returns a {@code List<T>}.
	 * An exception is thrown if at least one of the retrieved objects is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the elements are retrieved.
	 * @param expression XPath expression.
	 * @param type       Class of expected type of returned object.
	 * @param <T>        Expected type of returned object.
	 * @return List of objects cast into type {@code T}.
	 */
	public <T> List<T> getElementsOfType(DomNode node, String expression, Class<T> type)
	{
		List<T> result = new ArrayList<>();
		node.getByXPath(expression).forEach(element ->
		{
			if(!(type.isInstance(element)))
				throw new RuntimeException("Elements for expression " + expression + " are not of type " + type.getName());
			result.add((T)element);
		});

		LoggerFactory.getLogger(XPathProcessor.class).debug("Found " + result.size() + " " + (result.size() == 1 ? "element" : "elements") + " matching XPath expression " + expression);
		return result;
	}
}
