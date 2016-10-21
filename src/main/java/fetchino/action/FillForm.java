package fetchino.action;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import fetchino.util.Util;
import fetchino.workflow.Context;

public class FillForm implements Action
{
	private final String path;
	private final String value;

	public FillForm(String path, String value)
	{
		this.path = path;
		this.value = value;

		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(Context context)
	{
		HtmlInput input = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), HtmlInput.class);
		input.setValueAttribute(Util.replacePlaceholders(Util.replacePlaceholders(value, context), context));
	}
}
