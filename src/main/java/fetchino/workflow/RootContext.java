package fetchino.workflow;

import fetchino.util.XPathProcessor;

import java.util.*;

public class RootContext implements Context
{
	private final XPathProcessor xPathProcessor;
	private final Map<String, String> variables = new HashMap<>();
	private final Map<String, List<String>> lists = new HashMap<>();
	private final Map<String, Map<String, String>> maps = new HashMap<>();

	public RootContext()
	{
		xPathProcessor = new XPathProcessor();
	}

	public XPathProcessor getXPathProcessor()
	{
		return xPathProcessor;
	}

	public boolean hasVariable(String name)
	{
		return variables.containsKey(name);
	}

	public String getVariable(String name)
	{
		return variables.get(name);
	}

	public void setVariable(String name, String value)
	{
		variables.put(name, value);
	}

	public boolean hasList(String name)
	{
		return lists.containsKey(name);
	}

	public List<String> getList(String name)
	{
		return lists.get(name);
	}

	public void addToList(String name, String value)
	{
		if(!hasList(name))
			lists.put(name, new ArrayList<>());
		getList(name).add(value);
	}

	public boolean hasMap(String name)
	{
		return maps.containsKey(name);
	}

	public Map<String, String> getMap(String name)
	{
		return maps.get(name);
	}

	public void addToMap(String name, String key, String value)
	{
		if(!hasMap(name))
			maps.put(name, new LinkedHashMap<>());
		getMap(name).put(key, value);
	}
}
