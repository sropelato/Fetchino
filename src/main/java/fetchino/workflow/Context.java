package fetchino.workflow;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context
{
	private WebClient webClient = null;
	private HtmlPage currentPage = null;
	private final Map<String, String> variables = new HashMap<>();
	private final Map<String, List<String>> lists = new HashMap<>();
	private final Map<String, Map<String, String>> maps = new HashMap<>();

	public Context()
	{
		webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
	}

	public WebClient getWebClient()
	{
		return webClient;
	}

	public void setWebClient(WebClient webClient)
	{
		this.webClient = webClient;
	}

	public HtmlPage getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(HtmlPage currentPage)
	{
		this.currentPage = currentPage;
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
			maps.put(name, new HashMap<>());
		getMap(name).put(key, value);
	}
}
