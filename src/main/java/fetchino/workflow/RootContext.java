package fetchino.workflow;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.Util;
import fetchino.util.XPathProcessor;

import java.util.*;

public class RootContext implements Context
{
	private final WebClient webClient;
	private final XPathProcessor xPathProcessor;
	private final Map<String, String> variables = new HashMap<>();
	private final Map<String, List<String>> lists = new HashMap<>();
	private final Map<String, Map<String, String>> maps = new HashMap<>();

	public RootContext()
	{
		// create web client
		webClient = Util.createWebClient();

		// create XPath processor
		xPathProcessor = new XPathProcessor();
	}

	@Override
	public WebClient getWebClient()
	{
		return webClient;
	}

	@Override
	public XPathProcessor getXPathProcessor()
	{
		return xPathProcessor;
	}

	@Override
	public boolean hasVariable(String variableName)
	{
		return variables.containsKey(variableName);
	}

	@Override
	public String getVariable(String variableName)
	{
		return variables.get(variableName);
	}

	@Override
	public void setVariable(String variableName, String value)
	{
		variables.put(variableName, value);
	}

	@Override
	public boolean hasList(String listName)
	{
		return lists.containsKey(listName);
	}

	@Override
	public List<String> getList(String listName)
	{
		return lists.get(listName);
	}

	@Override
	public void addToList(String listName, String value)
	{
		if(!hasList(listName))
			lists.put(listName, new ArrayList<>());
		getList(listName).add(value);
	}

	@Override
	public boolean hasMap(String mapName)
	{
		return maps.containsKey(mapName);
	}

	@Override
	public Map<String, String> getMap(String mapName)
	{
		return maps.get(mapName);
	}

	@Override
	public void addToMap(String mapName, String key, String value)
	{
		if(!hasMap(mapName))
			maps.put(mapName, new LinkedHashMap<>());
		getMap(mapName).put(key, value);
	}
}
