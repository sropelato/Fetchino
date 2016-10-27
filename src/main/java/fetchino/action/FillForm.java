package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

/**
 * The {@code AddToList} action assigns a value to an input element.
 *
 * @version 1.0-SNAPSHOT
 */
public class FillForm implements Action
{
	private final String path;
	private final String value;

	/**
	 * Constructor.
	 *
	 * @param path  XPath expression defining the input element to be filled. This must return exactly one element.
	 * @param value Value to be set in the input element. This may contain variable placeholders.
	 */
	public FillForm(String path, String value)
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
		LoggerFactory.getLogger(FillForm.class).debug("Executing action: " + this);
		HtmlInput input = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlInput.class);
		input.setValueAttribute(Util.replacePlaceholders(Util.replacePlaceholders(value, context), context));
	}

	@Override
	public String toString()
	{
		return "FillForm{" +
				"path='" + path + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
