package fetchino.util;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public class Util
{
	public static void setupLogging()
	{
		System.setProperty("fetchino.log.level", "debug");
		//System.setProperty("fetchino.log.level", "info");
		//System.setProperty("fetchino.log.level", "default");
	}

	/**
	 * Validates an XPath expression.
	 *
	 * @param expression The expression to be validated
	 */
	public static void validateXPathExpression(String expression)
	{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try
		{
			xpath.compile(expression);
		}
		catch(XPathExpressionException e)
		{
			LoggerFactory.getLogger(Util.class).error("The following XPath expression is not valid: " + expression);
			LoggerFactory.getLogger(Util.class).error("Reason: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static void validateVariableName(String variableName)
	{
		if(!variableName.matches("^[A-Za-z_][A-Za-z0-9_]*$"))
			throw new RuntimeException("Invalid variable name: " + variableName);

	}

	/**
	 * Retrieves element from HtmlPage by XPath expression.
	 * If exactly one object of the expected type is returned, this method returns the cast object.
	 * An exception is thrown if no object is found, more than one object is found or the object found is not of a subtype of {@code T}.
	 *
	 * @param page       HtmlPage from which the element is retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @return Retrieved object cast into type {@code T}
	 */
	public static <T> T getSingleElementOfType(HtmlPage page, String expression, Class<T> type)
	{
		List<?> elements = page.getByXPath(expression);
		if(elements.size() == 0)
			throw new RuntimeException("No element has been found for expression: " + expression);
		else if(elements.size() > 1)
			throw new RuntimeException("Expecting exatly one element but " + elements.size() + " have been found for expression: " + expression);
		else if(!(type.isInstance(elements.get(0))))
			throw new RuntimeException("Element is not of type " + type.getName() + " found for expression: " + expression);
		else
			return (T)elements.get(0);
	}

	/**
	 * Retrieves elements from HtmlPage by XPath expression.
	 * If all objects are of a type compatible with {@code T}, this method returns a {@code List<T>}.
	 * An exception is thrown if at least one of the retrieved objects is not of a subtype of {@code T}.
	 *
	 * @param page       HtmlPage from which the elements are retrieved
	 * @param expression XPath expression
	 * @param type       Class of expected type of returned object
	 * @param <T>        Expected type of returned object
	 * @return List of objects cast into type {@code T}
	 */
	public static <T> List<T> getElementsOfType(HtmlPage page, String expression, Class<T> type)
	{
		List<T> result = new ArrayList<T>();
		List<?> elements = page.getByXPath(expression);
		elements.forEach(element ->
		{
			if(!(type.isInstance(element)))
				throw new RuntimeException("Elements are not of type " + type.getName() + " found for expression: " + expression);
			result.add((T)element);
		});
		return result;
	}
}
