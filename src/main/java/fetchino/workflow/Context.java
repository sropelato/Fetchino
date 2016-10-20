package fetchino.workflow;

import fetchino.util.XPathProcessor;

import java.util.List;
import java.util.Map;

public interface Context
{
	XPathProcessor getXPathProcessor();

	boolean hasVariable(String name);

	String getVariable(String name);

	void setVariable(String name, String value);

	boolean hasList(String name);

	List<String> getList(String name);

	void addToList(String name, String value);

	boolean hasMap(String name);

	Map<String, String> getMap(String name);

	void addToMap(String name, String key, String value);
}
