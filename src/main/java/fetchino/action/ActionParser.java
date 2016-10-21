package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import fetchino.condition.Condition;
import fetchino.condition.ConditionParser;
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
			case "setVar":
				return parseSetVar(actionElement);
			case "addToList":
				return parseAddToList(actionElement);
			case "addToMap":
				return parseAddToMap(actionElement);
			case "openLink":
				return parseOpenLink(actionElement);
			case "forEach":
				return parseForEach(actionElement);
			case "if":
				return parseIf(actionElement);
			default:
				throw new RuntimeException("Unknown action: " + actionElement.getName());
		}
	}

	private static Request parseRequest(Element requestElement)
	{
		String url;
		HttpMethod method;
		List<NameValuePair> params = new ArrayList<>();

		// url
		if(!requestElement.hasAttributeWithName("url"))
			throw new RuntimeException("request has no url attribute");
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
					throw new RuntimeException("param has no key attribute");

				if(!paramElement.hasAttributeWithName("value"))
					throw new RuntimeException("param has no value attribute");

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
			throw new RuntimeException("fillForm has no path attribute");
		else
			path = fillFormElement.getAttribute("path");

		if(!fillFormElement.hasAttributeWithName("value"))
			throw new RuntimeException("fillForm has no value attribute");
		else
			value = fillFormElement.getAttribute("value");

		return new FillForm(path, value);
	}

	private static ClickElement parseClickElement(Element clickElementElement)
	{
		String path;

		if(!clickElementElement.hasAttributeWithName("path"))
			throw new RuntimeException("clickElement has no path attribute");
		else
			path = clickElementElement.getAttribute("path");

		return new ClickElement(path);
	}

	private static SetVariable parseSetVar(Element setVarElement)
	{
		String variableName;
		String path = null;
		String value = null;

		if(!setVarElement.hasAttributeWithName("var"))
			throw new RuntimeException("setVar has no var attribute");
		else
			variableName = setVarElement.getAttribute("var");

		if(!setVarElement.hasAttributeWithName("path") && !setVarElement.hasAttributeWithName("value"))
			throw new RuntimeException("setVar must have either a path or a value attribute");
		else if(setVarElement.hasAttributeWithName("path") && setVarElement.hasAttributeWithName("value"))
			throw new RuntimeException("setVar cannot have a path and a value attribute");

		if(setVarElement.hasAttributeWithName("path"))
			path = setVarElement.getAttribute("path");
		else
			value = setVarElement.getAttribute("value");

		if(path != null)
			return new SetVariablePath(variableName, path);
		else
			return new SetVariableValue(variableName, value);
	}

	private static AddToList parseAddToList(Element addToListElement)
	{
		String listName;
		String path;

		if(!addToListElement.hasAttributeWithName("list"))
			throw new RuntimeException("addToList has no list attribute");
		else
			listName = addToListElement.getAttribute("list");

		if(!addToListElement.hasAttributeWithName("path"))
			throw new RuntimeException("addToList has no path attribute");
		else
			path = addToListElement.getAttribute("path");

		return new AddToList(listName, path);
	}

	private static AddToMap parseAddToMap(Element addToMapElement)
	{
		String mapName;
		String keyPath;
		String valuePath;

		if(!addToMapElement.hasAttributeWithName("map"))
			throw new RuntimeException("addToMap has no map attribute");
		else
			mapName = addToMapElement.getAttribute("map");

		if(!addToMapElement.hasElementWithName("key"))
			throw new RuntimeException("addToMap has no key element");
		else if(!addToMapElement.getElementByName("key").hasAttributeWithName("path"))
			throw new RuntimeException("key has no path attribute");
		else
			keyPath = addToMapElement.getElementByName("key").getAttribute("path");

		if(!addToMapElement.hasElementWithName("value"))
			throw new RuntimeException("addToMap has no value element");
		else if(!addToMapElement.getElementByName("value").hasAttributeWithName("path"))
			throw new RuntimeException("value has no path attribute");
		else
			valuePath = addToMapElement.getElementByName("value").getAttribute("path");

		return new AddToMap(mapName, keyPath, valuePath);
	}

	private static OpenLink parseOpenLink(Element openLinkElement)
	{
		String path;

		if(!openLinkElement.hasAttributeWithName("path"))
			throw new RuntimeException("openLink has no path attribute");
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
			throw new RuntimeException("forEach must have either a path or a list attribute");
		else if(forEachElement.hasAttributeWithName("path") && forEachElement.hasAttributeWithName("list"))
			throw new RuntimeException("forEach cannot have a path and a list attribute");

		if(forEachElement.hasAttributeWithName("path"))
			path = forEachElement.getAttribute("path");
		else
			listName = forEachElement.getAttribute("list");

		if(!forEachElement.hasAttributeWithName("var"))
			throw new RuntimeException("forEach has no var attribute");
		else
			var = forEachElement.getAttribute("var");

		forEachElement.getElements().forEach(actionElement -> nestedActions.add(parse(actionElement)));

		if(path != null)
			return new ForEachPath(path, var, nestedActions);
		else
			return new ForEachList(listName, var, nestedActions);
	}

	private static If parseIf(Element ifElement)
	{
		Condition condition;
		List<Action> thenActions = new ArrayList<>();
		List<Action> elseActions = new ArrayList<>();

		if(ifElement.hasElementWithName("condition"))
		{
			if(ifElement.getElementByName("condition").getElements().size() == 0)
				throw new RuntimeException("condition cannot be empty");
			else if(ifElement.getElementByName("condition").getElements().size() > 1)
				throw new RuntimeException("only one condition is allowed");
			else
				condition = ConditionParser.parse(ifElement.getElementByName("condition").getElements().get(0));
		}
		else
			throw new RuntimeException("if has no condition element");

		if(ifElement.hasElementWithName("then"))
			ifElement.getElementByName("then").getElements().forEach(actionElement -> thenActions.add(parse(actionElement)));
		else
			throw new RuntimeException("if has no then element");

		if(ifElement.hasElementWithName("else"))
			ifElement.getElementByName("else").getElements().forEach(actionElement -> elseActions.add(parse(actionElement)));

		return new If(condition, thenActions, elseActions);
	}
}
