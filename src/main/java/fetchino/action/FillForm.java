package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import fetchino.util.Util;
import fetchino.workflow.Action;
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
	public void execute(WebClient webClient, Context context)
	{
		HtmlInput input = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(webClient), path, HtmlInput.class);
		input.setValueAttribute(Util.replacePlaceholders(value, context));
	}
}
