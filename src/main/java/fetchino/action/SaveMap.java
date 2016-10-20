package fetchino.action;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.Context;

import java.util.List;

public class SaveMap implements Action
{
	private final String var;
	private final String keyPath;
	private final String valuePath;

	public SaveMap(String var, String keyPath, String valuePath)
	{
		this.var = var;
		this.keyPath = keyPath;
		this.valuePath = valuePath;

		Util.validateVariableName(var);
		Util.validateXPathExpression(keyPath);
		Util.validateXPathExpression(valuePath);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		List<DomNode> keyElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(webClient), keyPath, DomNode.class, true);
		List<DomNode> valueElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(webClient), valuePath, DomNode.class);

		if(keyElements.size() != valueElements.size())
			throw new RuntimeException("Number of keys and values does not match for key expression " + keyPath + " and value expression " + valuePath);

		for(int i = 0; i < keyElements.size(); i++)
			context.addToMap(var, (keyElements.get(i) instanceof DomAttr) ? keyElements.get(i).getNodeValue() : keyElements.get(i).asText(), (valueElements.get(i) instanceof DomAttr) ? valueElements.get(i).getNodeValue() : valueElements.get(i).asText());
	}
}
