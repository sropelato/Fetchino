package fetchino.util;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fetchino.workflow.Context;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util
{
	private static final String VARIABLE_NAME_PATTERN = "[A-Za-z_][A-Za-z0-9_]*";

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
		if(!variableName.matches("^" + VARIABLE_NAME_PATTERN + "$"))
			throw new RuntimeException("Invalid variable name: " + variableName);

	}

	public static HtmlPage getCurrentPage(WebClient webClient)
	{
		Page page = webClient.getCurrentWindow().getEnclosedPage();
		if(!page.isHtmlPage())
			throw new RuntimeException("Not an HtmlPage");
		return (HtmlPage)page;
	}

	public static String replacePlaceholders(String string, Context context)
	{
		String result = string;
		Pattern pattern = Pattern.compile(".*\\$\\{(" + VARIABLE_NAME_PATTERN + ")\\}.*$");

		boolean noPlaceholders = false;
		while(!noPlaceholders)
		{
			Matcher matcher = pattern.matcher(result);
			if(matcher.find())
			{
				String variableName = matcher.group(1);
				if(context.hasVariable(variableName))
					result = result.replaceAll("\\$\\{" + variableName + "\\}", context.getVariable(variableName));
				else
					throw new RuntimeException("Variable does not exist: " + variableName);
			}
			else
			{
				noPlaceholders = true;
			}
		}

		return result;
	}
}
