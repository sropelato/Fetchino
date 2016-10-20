package fetchino.workflow;

import fetchino.util.XPathProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempContext implements Context
{
	private final Context parent;
	private final Map<String, String> tempVariables = new HashMap<>();

	public TempContext(Context parent)
	{
		this.parent = parent;
	}

	public XPathProcessor getXPathProcessor()
	{
		return parent.getXPathProcessor();
	}

	public void setTempVariable(String name, String value)
	{
		tempVariables.put(name, value);
	}

	public boolean hasVariable(String name)
	{
		return tempVariables.containsKey(name) || parent.hasVariable(name);
	}

	public String getVariable(String name)
	{
		if(tempVariables.containsKey(name))
			return tempVariables.get(name);
		else
			return parent.getVariable(name);
	}

	public void setVariable(String name, String value)
	{
		if(tempVariables.containsKey(name))
			throw new RuntimeException("Variable '" + name + "' is a temporary variable of this context and cannot be overwritten");
		parent.setVariable(name, value);
	}

	public boolean hasList(String name)
	{
		return parent.hasList(name);
	}

	public List<String> getList(String name)
	{
		return parent.getList(name);
	}

	public void addToList(String name, String value)
	{
		parent.addToList(name, value);
	}

	public boolean hasMap(String name)
	{
		return parent.hasMap(name);
	}

	public Map<String, String> getMap(String name)
	{
		return parent.getMap(name);
	}

	public void addToMap(String name, String key, String value)
	{
		parent.addToMap(name, key, value);
	}
}
