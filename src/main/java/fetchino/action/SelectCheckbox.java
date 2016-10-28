package fetchino.action;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import fetchino.context.Context;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

/**
 * The {@code SelectCheckbox} action checks / unchecks a checkbox element.
 *
 * @version 1.0-SNAPSHOT
 */
public class SelectCheckbox implements Action
{
	private final String path;
	private final String checked;

	/**
	 * Constructor.
	 *
	 * @param path    XPath expression defining the checkbox element to be checked / unchecked. This must return exactly one element.
	 * @param checked Value determining whether the checkbox is to be checked or unchecked. This may contain variable placeholders and must evaluate to {@code "true"} for checked or {@code "false"} for unchecked.
	 */
	public SelectCheckbox(String path, String checked)
	{
		this.path = path;
		this.checked = checked;

		Util.validateXPathExpression(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(SelectCheckbox.class).debug("Executing action: " + this);
		HtmlCheckBoxInput checkboxInput = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlCheckBoxInput.class);
		checkboxInput.setChecked(Boolean.parseBoolean(Util.replacePlaceholders(checked, context)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "SelectCheckbox{" +
				"path='" + path + '\'' +
				", checked='" + checked + '\'' +
				'}';
	}
}
