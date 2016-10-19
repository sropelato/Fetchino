package fetchino.action;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import lightdom.Element;

public class FillForm implements Action
{
	private final String formPath;
	private final String inputName;
	private final String value;

	public FillForm(String formPath, String inputName, String value)
	{
		this.formPath = formPath;
		this.inputName = inputName;
		this.value = value;

		Util.validateXPathExpression(formPath);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		HtmlForm form = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(webClient), formPath, HtmlForm.class);
		HtmlInput input = form.getInputByName(inputName);
		input.setValueAttribute(value);
	}
}
