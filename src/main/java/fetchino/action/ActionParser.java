package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import fetchino.workflow.Action;
import lightdom.Element;

import java.util.ArrayList;
import java.util.List;

public class ActionParser
{
	public static Action parse(Element actionElement)
	{
		switch(actionElement.getName())
		{
			case "request":
				return parseRequest(actionElement);
			case "fillForm":
				return parseFillForm(actionElement);
			case "clickElement":
				return parseClickElement(actionElement);
			case "save":
				return parseSave(actionElement);
			case "saveAll":
				return parseSaveAll(actionElement);
			case "saveMap":
				return parseSaveMap(actionElement);
			case "openLink":
				return parseOpenLink(actionElement);
			case "forEach":
				return parseForEach(actionElement);
			default:
				throw new RuntimeException("Unknown element: " + actionElement.getName());
		}
	}

	private static Request parseRequest(Element requestElement)
	{
		String url;
		HttpMethod method;
		List<NameValuePair> params = new ArrayList<>();

		if(!requestElement.hasAttributeWithName("url"))
			throw new RuntimeException("Request has no url attribute");

		// url
		url = requestElement.getAttribute("url");

		// method
		if(requestElement.hasAttributeWithName("method"))
		{
			String methodString = requestElement.getAttribute("method");
			switch(methodString)
			{
				case "GET":
					method = HttpMethod.GET;
					break;
				case "POST":
					method = HttpMethod.POST;
					break;
				default:
					throw new RuntimeException("Unknown method: " + methodString);
			}
		}
		else
		{
			method = HttpMethod.GET;
		}

		// params
		if(requestElement.hasElementWithName("param"))
		{
			for(Element paramElement : requestElement.getElementsByName("param"))
			{
				if(!paramElement.hasAttributeWithName("key"))
					throw new RuntimeException("Param has no key attribute");

				if(!paramElement.hasAttributeWithName("value"))
					throw new RuntimeException("Param has no value attribute");

				params.add(new NameValuePair(paramElement.getAttribute("key"), paramElement.getAttribute("value")));
			}
		}

		return new Request(url, method, params);
	}

	private static FillForm parseFillForm(Element fillFormElement)
	{
		String path;
		String value;

		if(!fillFormElement.hasAttributeWithName("path"))
			throw new RuntimeException("FillForm has no path attribute");
		else
			path = fillFormElement.getAttribute("path");

		if(!fillFormElement.hasAttributeWithName("value"))
			throw new RuntimeException("FillForm has no value attribute");
		else
			value = fillFormElement.getAttribute("value");

		return new FillForm(path, value);
	}

	private static ClickElement parseClickElement(Element clickElementElement)
	{
		String path;

		if(!clickElementElement.hasAttributeWithName("path"))
			throw new RuntimeException("ClickElement has no path attribute");
		else
			path = clickElementElement.getAttribute("path");

		return new ClickElement(path);
	}

	private static Save parseSave(Element saveElement)
	{
		String path;
		String var;

		if(!saveElement.hasAttributeWithName("path"))
			throw new RuntimeException("Save has no path attribute");
		else
			path = saveElement.getAttribute("path");

		if(!saveElement.hasAttributeWithName("var"))
			throw new RuntimeException("Save has no var attribute");
		else
			var = saveElement.getAttribute("var");

		return new Save(path, var);
	}

	private static SaveAll parseSaveAll(Element saveAll)
	{
		String path;
		String var;

		if(!saveAll.hasAttributeWithName("path"))
			throw new RuntimeException("SaveAll has no path attribute");
		else
			path = saveAll.getAttribute("path");

		if(!saveAll.hasAttributeWithName("var"))
			throw new RuntimeException("SaveAll has no var attribute");
		else
			var = saveAll.getAttribute("var");

		return new SaveAll(path, var);
	}

	private static SaveMap parseSaveMap(Element saveMapElement)
	{
		String var;
		String keyPath;
		String valuePath;

		if(!saveMapElement.hasAttributeWithName("var"))
			throw new RuntimeException("SaveMap has no var attribute");
		else
			var = saveMapElement.getAttribute("var");

		if(!saveMapElement.hasElementWithName("key"))
			throw new RuntimeException("SaveMap has no key element");
		else if(!saveMapElement.getElementByName("key").hasAttributeWithName("path"))
			throw new RuntimeException("Key has no path attribute");
		else
			keyPath = saveMapElement.getElementByName("key").getAttribute("path");

		if(!saveMapElement.hasElementWithName("value"))
			throw new RuntimeException("SaveMap has no value element");
		else if(!saveMapElement.getElementByName("value").hasAttributeWithName("path"))
			throw new RuntimeException("Value has no path attribute");
		else
			valuePath = saveMapElement.getElementByName("value").getAttribute("path");

		return new SaveMap(var, keyPath, valuePath);
	}

	private static OpenLink parseOpenLink(Element openLinkElement)
	{
		String path;

		if(!openLinkElement.hasAttributeWithName("path"))
			throw new RuntimeException("OpenLink has no path attribute");
		else
			path = openLinkElement.getAttribute("path");

		return new OpenLink(path);
	}

	private static ForEach parseForEach(Element forEachElement)
	{
		String path = null;
		String listName = null;
		String var;
		List<Action> nestedActions = new ArrayList<>();

		if(!forEachElement.hasAttributeWithName("path") && !forEachElement.hasAttributeWithName("list"))
			throw new RuntimeException("ForEach must have either a path or a list attribute");
		else if(forEachElement.hasAttributeWithName("path") && forEachElement.hasAttributeWithName("list"))
			throw new RuntimeException("ForEach cannot have a path and a list attribute");

		if(forEachElement.hasAttributeWithName("path"))
			path = forEachElement.getAttribute("path");
		else
			listName = forEachElement.getAttribute("list");

		if(!forEachElement.hasAttributeWithName("var"))
			throw new RuntimeException("ForEach has no var attribute");
		else
			var = forEachElement.getAttribute("var");

		forEachElement.getElements().forEach(actionElement -> nestedActions.add(parse(actionElement)));

		if(path != null)
			return new ForEachPath(path, var, nestedActions);
		else
			return new ForEachList(listName, var, nestedActions);
	}
}
