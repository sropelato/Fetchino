package fetchino.workflow;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.Util;
import fetchino.util.XPathProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempContext implements Context
{
	private final WebClient webClient;
	private final Context parent;
	private final Map<String, String> tempVariables = new HashMap<>();

	public TempContext(Context parent, boolean createNewWebClient)
	{
		this.parent = parent;
		if(createNewWebClient)
			webClient = Util.createWebClient();
		else
			webClient = null;
	}

	@Override
	public WebClient getWebClient()
	{
		if(webClient != null)
			return webClient;
		else
			return parent.getWebClient();
	}

	@Override
	public XPathProcessor getXPathProcessor()
	{
		return parent.getXPathProcessor();
	}

	public void setTempVariable(String name, String value)
	{
		tempVariables.put(name, value);
	}

	@Override
	public boolean hasVariable(String variableName)
	{
		return tempVariables.containsKey(variableName) || parent.hasVariable(variableName);
	}

	@Override
	public String getVariable(String variableName)
	{
		if(tempVariables.containsKey(variableName))
			return tempVariables.get(variableName);
		else
			return parent.getVariable(variableName);
	}

	@Override
	public void setVariable(String variableName, String value)
	{
		if(tempVariables.containsKey(variableName))
			throw new RuntimeException("Variable '" + variableName + "' is a temporary variable of this context and cannot be overwritten");
		parent.setVariable(variableName, value);
	}

	@Override
	public boolean hasList(String listName)
	{
		return parent.hasList(listName);
	}

	@Override
	public List<String> getList(String listName)
	{
		return parent.getList(listName);
	}

	@Override
	public void addToList(String listName, String value)
	{
		parent.addToList(listName, value);
	}

	@Override
	public boolean hasMap(String mapName)
	{
		return parent.hasMap(mapName);
	}

	@Override
	public Map<String, String> getMap(String mapName)
	{
		return parent.getMap(mapName);
	}

	@Override
	public void addToMap(String mapName, String key, String value)
	{
		parent.addToMap(mapName, key, value);
	}
}
