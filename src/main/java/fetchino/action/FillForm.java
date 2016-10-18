package fetchino.action;

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

	public FillForm(Element fillFormElement)
	{
		if(!fillFormElement.hasAttributeWithName("formPath"))
			throw new RuntimeException("FillForm has no formPath attribute");
		else
			formPath = fillFormElement.getAttribute("formPath");
		Util.validateXPathExpression(formPath);

		if(!fillFormElement.hasAttributeWithName("inputName"))
			throw new RuntimeException("FillForm has no inputName attribute");
		else
			inputName = fillFormElement.getAttribute("inputName");

		if(!fillFormElement.hasAttributeWithName("value"))
			throw new RuntimeException("FillForm has no value attribute");
		else
			value = fillFormElement.getAttribute("value");
	}

	@Override
	public void execute(Context context)
	{
		HtmlForm form = Util.getSingleElementOfType(context.getCurrentPage(), formPath, HtmlForm.class);
		HtmlInput input = form.getInputByName(inputName);
		input.setValueAttribute(value);
	}
}
