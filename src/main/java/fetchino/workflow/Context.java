package fetchino.workflow;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.XPathProcessor;

import java.util.List;
import java.util.Map;

public interface Context
{
	XPathProcessor getXPathProcessor();

	boolean hasVariable(String variableName);

	String getVariable(String variableName);

	void setVariable(String variableName, String value);

	boolean hasList(String listName);

	List<String> getList(String listName);

	void addToList(String listName, String value);

	boolean hasMap(String mapName);

	Map<String, String> getMap(String mapName);

	void addToMap(String mapName, String key, String value);

	WebClient getWebClient();
}
