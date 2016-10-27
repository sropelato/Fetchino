package fetchino.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class with various useful methods.
 *
 * @version 1.0-SNAPSHOT
 */
public class Util
{
	private static final String VARIABLE_NAME_PATTERN = "[A-Za-z_][A-Za-z0-9_]*";

	public enum LogLevel
	{
		DEFAULT, DEBUG
	}

	/**
	 * Sets the logging level.
	 */
	public static void setupLogging(LogLevel logLevel)
	{
		switch(logLevel)
		{
			case DEFAULT:
				System.setProperty("fetchino.log.level", "default");
				break;
			case DEBUG:
				System.setProperty("fetchino.log.level", "debug");
				break;
		}
	}

	/**
	 * Validates an XPath expression. If the expression is not a valid XPath expression, a RuntimeException is thrown.
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
			LoggerFactory.getLogger(Util.class).error("Invalid XPath expression: " + expression);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Validates a variable name. If the variable name is invalid, a RuntimeException is thrown. Valid variable names match the following regex pattern: [A-Za-z_][A-Za-z0-9_]*
	 *
	 * @param variableName The variable name.
	 */
	public static void validateVariableName(String variableName)
	{
		if(!variableName.matches("^" + VARIABLE_NAME_PATTERN + "$"))
			throw new RuntimeException("Invalid variable name: " + variableName);

	}

	/**
	 * @param context The context.
	 * @return The current page of this context.
	 */
	public static HtmlPage getCurrentPage(Context context)
	{
		Page page = context.getWebClient().getCurrentWindow().getEnclosedPage();
		if(!page.isHtmlPage())
			throw new RuntimeException("Not an HtmlPage");
		return (HtmlPage)page;
	}

	/**
	 * Replaces variable placeholders in a string. If a variable used in a placeholder does not exist, a RuntimeException is thrown.
	 *
	 * @param string  The string to be replaced.
	 * @param context The context holding the variables to replace the placeholders.
	 * @return The string with all placeholders replaced.
	 */
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

	/**
	 * Creates a new web client.
	 *
	 * @return A new {@link WebClient} instance.
	 */
	public static WebClient createWebClient()
	{
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		return webClient;
	}
}
