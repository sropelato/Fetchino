package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.workflow.Context;

import java.util.List;

public class AddToMap implements Action
{
	private final String mapName;
	private final String keyPath;
	private final String valuePath;

	public AddToMap(String mapName, String keyPath, String valuePath)
	{
		this.mapName = mapName;
		this.keyPath = keyPath;
		this.valuePath = valuePath;

		Util.validateVariableName(mapName);
		Util.validateXPathExpression(keyPath);
		Util.validateXPathExpression(valuePath);
	}

	@Override
	public void execute(Context context)
	{
		List<DomNode> keyElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(keyPath, context), DomNode.class, true);
		List<DomNode> valueElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(valuePath, context), DomNode.class);

		if(keyElements.size() != valueElements.size())
			throw new RuntimeException("Number of keys and values does not match for key expression " + Util.replacePlaceholders(keyPath, context) + " and value expression " + Util.replacePlaceholders(valuePath, context));

		for(int i = 0; i < keyElements.size(); i++)
			context.addToMap(mapName, (keyElements.get(i) instanceof DomAttr) ? keyElements.get(i).getNodeValue() : keyElements.get(i).asText(), (valueElements.get(i) instanceof DomAttr) ? valueElements.get(i).getNodeValue() : valueElements.get(i).asText());
	}
}
