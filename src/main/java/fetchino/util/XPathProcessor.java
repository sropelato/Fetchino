package fetchino.util;

import com.gargoylesoftware.htmlunit.html.DomNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class XPathProcessor
{
	private Map<Pair<String, String>, Object> resultCache = new HashMap<>();
	private Set<String> cachedExpressions = new HashSet<>();

	/**
	 * Retrieves single element from DomNode by XPath expression.
	 * If exactly one object of the expected type is returned, this method returns the cast object.
	 * An exception is thrown if no object is found, more than one object is found or the object found is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the element is retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @return Retrieved object cast into type {@code T}
	 */
	public <T> T getSingleElementOfType(DomNode node, String expression, Class<T> type)
	{
		return getSingleElementOfType(node, expression, type, false);
	}

	/**
	 * Retrieves single element from DomNode by XPath expression.
	 * If exactly one object of the expected type is returned, this method returns the cast object.
	 * An exception is thrown if no object is found, more than one object is found or the object found is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the element is retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @param useCache   Use cached result if available
	 * @return Retrieved object cast into type {@code T}
	 */
	public <T> T getSingleElementOfType(DomNode node, String expression, Class<T> type, boolean useCache)
	{
		// return cached result if available
		String nodeAsXml = null;
		if(useCache && cachedExpressions.contains(expression))
		{
			nodeAsXml = node.asXml();
			if(resultCache.containsKey(Pair.of(nodeAsXml, expression)))
				return (T)resultCache.get(Pair.of(nodeAsXml, expression));
		}

		List<?> elements = node.getByXPath(expression);
		if(elements.size() == 0)
			throw new RuntimeException("No element has been found for expression: " + expression);
		else if(elements.size() > 1)
			throw new RuntimeException("Expecting exatly one element but " + elements.size() + " have been found for expression: " + expression);
		else if(!(type.isInstance(elements.get(0))))
			throw new RuntimeException("Element for expression '" + expression + "' is not of type " + type.getName());

		if(useCache)
		{
			if(nodeAsXml == null)
				nodeAsXml = node.asXml();
			cachedExpressions.add(expression);
			resultCache.put(Pair.of(nodeAsXml, expression), elements.get(0));
		}

		return (T)elements.get(0);

	}

	/**
	 * Retrieves elements from DomNode by XPath expression.
	 * If all objects are of a type compatible with {@code T}, this method returns a {@code List<T>}.
	 * An exception is thrown if at least one of the retrieved objects is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the elements are retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @return List of objects cast into type {@code T}
	 */
	public <T> List<T> getElementsOfType(DomNode node, String expression, Class<T> type)
	{
		return getElementsOfType(node, expression, type, false);
	}

	/**
	 * Retrieves elements from DomNode by XPath expression.
	 * If all objects are of a type compatible with {@code T}, this method returns a {@code List<T>}.
	 * An exception is thrown if at least one of the retrieved objects is not of a subtype of {@code T}.
	 *
	 * @param node       DomNode from which the elements are retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @param useCache   Use cached result if available
	 * @return List of objects cast into type {@code T}
	 */
	public <T> List<T> getElementsOfType(DomNode node, String expression, Class<T> type, boolean useCache)
	{
		// return cached result if available
		String nodeAsXml = null;
		if(useCache && cachedExpressions.contains(expression))
		{
			nodeAsXml = node.asXml();
			if(resultCache.containsKey(Pair.of(nodeAsXml, expression)))
				return (List<T>)resultCache.get(Pair.of(nodeAsXml, expression));
		}

		List<T> result = new ArrayList<T>();
		List<?> elements = node.getByXPath(expression);
		elements.forEach(element ->
		{
			if(!(type.isInstance(element)))
				throw new RuntimeException("Elements for expression '" + expression + "' are not of type " + type.getName());
			result.add((T)element);
		});

		if(useCache)
		{
			if(nodeAsXml == null)
				nodeAsXml = node.asXml();
			cachedExpressions.add(expression);
			resultCache.put(Pair.of(nodeAsXml, expression), result);
		}

		return result;
	}
}
