package fetchino.action;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import fetchino.context.Context;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

/**
 * The {@code SelectDropdown} action sets the value of a dropdown element.
 *
 * @version 1.0-SNAPSHOT
 */
public class SelectDropdown implements Action
{
	private final String path;
	private final String value;

	/**
	 * Constructor.
	 *
	 * @param path  XPath expression defining the dropdown element to be selected. This must return exactly one element.
	 * @param value Value to be set in the dropdown element. This may contain variable placeholders.
	 */
	public SelectDropdown(String path, String value)
	{
		this.path = path;
		this.value = value;

		Util.validateXPathExpression(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(SelectDropdown.class).debug("Executing action: " + this);
		try
		{
			HtmlSelect select = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlSelect.class);
			HtmlOption option = select.getOptionByValue(Util.replacePlaceholders(value, context));
			select.setSelectedAttribute(option, true);
		}
		catch(ElementNotFoundException e)
		{
			throw new RuntimeException("No option found for value " + value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "SelectDropdown{" +
				"path='" + path + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
