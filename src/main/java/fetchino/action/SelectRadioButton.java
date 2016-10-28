package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import fetchino.context.Context;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

/**
 * The {@code SelectRadiobutton} action checks / unchecks a radio button element.
 *
 * @version 1.0-SNAPSHOT
 */
public class SelectRadioButton implements Action
{
	private final String path;
	private final String checked;

	/**
	 * Constructor.
	 *
	 * @param path    XPath expression defining the radio button element to be checked / unchecked. This must return exactly one element.
	 * @param checked Value determining whether the radio button is to be checked or unchecked. This may contain variable placeholders and must evaluate to {@code "true"} for checked or {@code "false"} for unchecked.
	 */
	public SelectRadioButton(String path, String checked)
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
		LoggerFactory.getLogger(SelectRadioButton.class).debug("Executing action: " + this);
		HtmlRadioButtonInput radioButtonInput = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlRadioButtonInput.class);
		radioButtonInput.setChecked(Boolean.parseBoolean(Util.replacePlaceholders(checked, context)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "SelectRadioButton{" +
				"path='" + path + '\'' +
				", checked='" + checked + '\'' +
				'}';
	}
}
