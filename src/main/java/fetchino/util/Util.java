package fetchino.util;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static HtmlPage getCurrentPage(WebClient webClient)
	{
		Page page = webClient.getCurrentWindow().getEnclosedPage();
		if(!page.isHtmlPage())
			throw new RuntimeException("Not an HtmlPage");
		return (HtmlPage)page;
	}
}
