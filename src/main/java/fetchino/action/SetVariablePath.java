package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

public class SetVariablePath extends SetVariable
{
	private final String path;
	private final String variableName;

	public SetVariablePath(String variableName, String path)
	{
		this.variableName = variableName;
		this.path = path;

		Util.validateVariableName(variableName);
		Util.validateXPathExpression(path);
	}

	@Override
	public void execute(Context context)
	{
		DomNode element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		context.setVariable(variableName, (element instanceof DomAttr) ? element.getNodeValue() : element.asText());
	}
}
