package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The {@code AddToMap} action adds a new entry to a map.
 *
 * @version 1.0-SNAPSHOT
 */
public class AddToMap implements Action
{
	private final String mapName;
	private final String keyPath;
	private final String valuePath;

	/**
	 * Constructor.
	 *
	 * @param mapName   Name of the map
	 * @param keyPath   XPath expression defining the keys of the map entries.
	 * @param valuePath XPath expression defining the values of the map entries.
	 */
	public AddToMap(String mapName, String keyPath, String valuePath)
	{
		this.mapName = mapName;
		this.keyPath = keyPath;
		this.valuePath = valuePath;

		Util.validateVariableName(mapName);
		Util.validateXPathExpression(keyPath);
		Util.validateXPathExpression(valuePath);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(AddToMap.class).debug("Executing action: " + this);
		List<DomNode> keyElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(keyPath, context), DomNode.class);
		List<DomNode> valueElements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(valuePath, context), DomNode.class);

		if(keyElements.size() != valueElements.size())
			throw new RuntimeException("Number of keys and values does not match for key expression " + Util.replacePlaceholders(keyPath, context) + " and value expression " + Util.replacePlaceholders(valuePath, context));

		for(int i = 0; i < keyElements.size(); i++)
			context.addToMap(mapName, (keyElements.get(i) instanceof DomAttr) ? keyElements.get(i).getNodeValue() : keyElements.get(i).asText(), (valueElements.get(i) instanceof DomAttr) ? valueElements.get(i).getNodeValue() : valueElements.get(i).asText());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "AddToMap{" +
				"mapName='" + mapName + '\'' +
				", keyPath='" + keyPath + '\'' +
				", valuePath='" + valuePath + '\'' +
				'}';
	}
}
