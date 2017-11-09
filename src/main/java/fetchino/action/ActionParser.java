package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import fetchino.condition.Condition;
import fetchino.condition.ConditionParser;
import lightdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to create {@link Action} objects defined by DOM elements.
 *
 * @version 1.0-SNAPSHOT
 */
public class ActionParser
{
	/**
	 * Parses a DOM element and creates an action.
	 *
	 * @param actionElement The DOM element to be parsed.
	 * @return The {@link Action} object defined by the DOM element.
	 */
	public static Action parse(Element actionElement)
	{
		switch(actionElement.getName())
		{
			case "request":
				return parseRequest(actionElement);
			case "fillForm":
				return parseFillForm(actionElement);
			case "selectDropdown":
				return parseSelectDropdown(actionElement);
			case "selectCheckbox":
				return parseSelectCheckbox(actionElement);
			case "selectRadioButton":
				return parseSelectRadioButton(actionElement);
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
			case "sleep":
				return parseSleep(actionElement);
			default:
				throw new RuntimeException("Unknown action: " + actionElement.getName());
		}
	}

	/**
	 * Parses a DOM element representing a {@link Request} action.
	 *
	 * @param requestElement The DOM element to be parsed.
	 * @return The {@link Request} action object defined by the DOM element.
	 */
	private static Request parseRequest(Element requestElement)
	{
		String url;
		HttpMethod method;
		List<NameValuePair> params = new ArrayList<>();
		NameValuePair credentials;

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

		// credentials
		if(requestElement.hasElementWithName("credentials"))
		{
			Element credentialsElement = requestElement.getElementByName("credentials");
			if(!credentialsElement.hasAttributeWithName("username"))
				throw new RuntimeException("credentials has no username attribute");
			if(!credentialsElement.hasAttributeWithName("password"))
				throw new RuntimeException("credentials has no password attribute");
			credentials = new NameValuePair(credentialsElement.getAttribute("username"), credentialsElement.getAttribute("password"));
		}
		else
			credentials = null;

		return new Request(url, method, params, credentials);
	}

	/**
	 * Parses a DOM element representing a {@link FillForm} action.
	 *
	 * @param fillFormElement The DOM element to be parsed.
	 * @return The {@link FillForm} action object defined by the DOM element.
	 */
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

	/**
	 * Parses a DOM element representing a {@link SelectDropdown} action.
	 *
	 * @param selectDropdownElement The DOM element to be parsed.
	 * @return The {@link SelectDropdown} action object defined by the DOM element.
	 */
	private static SelectDropdown parseSelectDropdown(Element selectDropdownElement)
	{
		String path;
		String value;

		if(!selectDropdownElement.hasAttributeWithName("path"))
			throw new RuntimeException("selectDropdown has no path attribute");
		else
			path = selectDropdownElement.getAttribute("path");

		if(!selectDropdownElement.hasAttributeWithName("value"))
			throw new RuntimeException("selectDropdown has no value attribute");
		else
			value = selectDropdownElement.getAttribute("value");

		return new SelectDropdown(path, value);
	}

	/**
	 * Parses a DOM element representing a {@link SelectCheckbox} action.
	 *
	 * @param selectCheckboxElement The DOM element to be parsed.
	 * @return The {@link SelectCheckbox} action object defined by the DOM element.
	 */
	private static SelectCheckbox parseSelectCheckbox(Element selectCheckboxElement)
	{
		String path;
		String checked;

		if(!selectCheckboxElement.hasAttributeWithName("path"))
			throw new RuntimeException("selectCheckbox has no path attribute");
		else
			path = selectCheckboxElement.getAttribute("path");

		if(!selectCheckboxElement.hasAttributeWithName("checked"))
			throw new RuntimeException("selectCheckbox has no checked attribute");
		else
			checked = selectCheckboxElement.getAttribute("checked");

		return new SelectCheckbox(path, checked);
	}

	/**
	 * Parses a DOM element representing a {@link SelectRadioButton} action.
	 *
	 * @param selectRadioButtonElement The DOM element to be parsed.
	 * @return The {@link SelectRadioButton} action object defined by the DOM element.
	 */
	private static SelectRadioButton parseSelectRadioButton(Element selectRadioButtonElement)
	{
		String path;
		String checked;

		if(!selectRadioButtonElement.hasAttributeWithName("path"))
			throw new RuntimeException("selectRadioButtonhas no path attribute");
		else
			path = selectRadioButtonElement.getAttribute("path");

		if(!selectRadioButtonElement.hasAttributeWithName("checked"))
			throw new RuntimeException("selectRadioButtonhas has no checked attribute");
		else
			checked = selectRadioButtonElement.getAttribute("checked");

		return new SelectRadioButton(path, checked);
	}

	/**
	 * Parses a DOM element representing a {@link ClickElement} action.
	 *
	 * @param clickElementElement The DOM element to be parsed.
	 * @return The {@link ClickElement} action object defined by the DOM element.
	 */
	private static ClickElement parseClickElement(Element clickElementElement)
	{
		String path;

		if(!clickElementElement.hasAttributeWithName("path"))
			throw new RuntimeException("clickElement has no path attribute");
		else
			path = clickElementElement.getAttribute("path");

		return new ClickElement(path);
	}

	/**
	 * Parses a DOM element representing a {@link SetVariable} action.
	 *
	 * @param setVarElement The DOM element to be parsed.
	 * @return The {@link SetVariable} action object defined by the DOM element.
	 */
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

	/**
	 * Parses a DOM element representing an {@link AddToList} action.
	 *
	 * @param addToListElement The DOM element to be parsed.
	 * @return The {@link AddToList} action object defined by the DOM element.
	 */
	private static AddToList parseAddToList(Element addToListElement)
	{
		String listName;
		String path = null;
		String value = null;

		if(!addToListElement.hasAttributeWithName("list"))
			throw new RuntimeException("addToList has no list attribute");
		else
			listName = addToListElement.getAttribute("list");

		if(!addToListElement.hasAttributeWithName("path") && !addToListElement.hasAttributeWithName("value"))
			throw new RuntimeException("addToList must have either a path or a value attribute");
		else if(addToListElement.hasAttributeWithName("path") && addToListElement.hasAttributeWithName("value"))
			throw new RuntimeException("addToList cannot have a path and a value attribute");

		if(addToListElement.hasAttributeWithName("path"))
			path = addToListElement.getAttribute("path");
		else
			value = addToListElement.getAttribute("value");

		if(path != null)
			return new AddToListPath(listName, path);
		else
			return new AddToListValue(listName, value);
	}

	/**
	 * Parses a DOM element representing an {@link AddToMap} action.
	 *
	 * @param addToMapElement The DOM element to be parsed.
	 * @return The {@link AddToMap} action object defined by the DOM element.
	 */
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

	/**
	 * Parses a DOM element representing an {@link OpenLink} action.
	 *
	 * @param openLinkElement The DOM element to be parsed.
	 * @return The {@link OpenLink} action object defined by the DOM element.
	 */
	private static OpenLink parseOpenLink(Element openLinkElement)
	{
		String path;

		if(!openLinkElement.hasAttributeWithName("path"))
			throw new RuntimeException("openLink has no path attribute");
		else
			path = openLinkElement.getAttribute("path");

		return new OpenLink(path);
	}

	/**
	 * Parses a DOM element representing a {@link ForEach} action.
	 *
	 * @param forEachElement The DOM element to be parsed.
	 * @return The {@link ForEach} action object defined by the DOM element.
	 */
	private static ForEach parseForEach(Element forEachElement)
	{
		String path = null;
		String listName = null;
		String var;
		List<Action> nestedActions = new ArrayList<>();
		String limit;
		boolean parallel;

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

		if(forEachElement.hasAttributeWithName("limit"))
			limit = forEachElement.getAttribute("limit");
		else
			limit = null;

		parallel = forEachElement.hasAttributeWithName("parallel") && forEachElement.getAttributeAsBoolean("parallel");

		forEachElement.getElements().forEach(actionElement -> nestedActions.add(parse(actionElement)));

		if(path != null)
			return new ForEachPath(path, var, nestedActions, limit, parallel);
		else
			return new ForEachList(listName, var, nestedActions, limit, parallel);
	}

	/**
	 * Parses a DOM element representing an {@link If} action.
	 *
	 * @param ifElement The DOM element to be parsed.
	 * @return The {@link If} action object defined by the DOM element.
	 */
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

	private static Sleep parseSleep(Element sleepElement)
	{
		String millis;

		if(!sleepElement.hasAttributeWithName("millis"))
			throw new RuntimeException("sleep has no millis attribute");
		else
			millis = sleepElement.getAttribute("millis");

		return new Sleep(millis);
	}
}
