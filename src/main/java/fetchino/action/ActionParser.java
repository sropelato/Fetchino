package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import fetchino.util.Util;
import fetchino.workflow.Action;
import fetchino.workflow.DataDescriptor;
import lightdom.Element;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
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
			default:
				LoggerFactory.getLogger(DataDescriptor.class).warn("Not implemented: " + actionElement.getName());
				return null;
		}
	}

	private static Request parseRequest(Element requestElement)
	{
		URL url;
		HttpMethod method;
		List<NameValuePair> params = new ArrayList<>();

		if(requestElement.getElementByName("url") == null)
			throw new RuntimeException("Request has no url element");

		// url
		try
		{
			url = UrlUtils.toUrlUnsafe(requestElement.getElementByName("url").getText());
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException(e);
		}

		// method
		if(requestElement.getElementByName("method") == null)
		{
			method = HttpMethod.GET;
		}
		else
		{
			String methodString = requestElement.getElementByName("method").getText();
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

		// params
		if(requestElement.getElementByName("params") != null)
		{
			for(Element paramElement : requestElement.getElementByName("params").getElementsByName("param"))
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
		String formPath;
		String inputName;
		String value;

		if(!fillFormElement.hasAttributeWithName("formPath"))
			throw new RuntimeException("FillForm has no formPath attribute");
		else
			formPath = fillFormElement.getAttribute("formPath");

		if(!fillFormElement.hasAttributeWithName("inputName"))
			throw new RuntimeException("FillForm has no inputName attribute");
		else
			inputName = fillFormElement.getAttribute("inputName");

		if(!fillFormElement.hasAttributeWithName("value"))
			throw new RuntimeException("FillForm has no value attribute");
		else
			value = fillFormElement.getAttribute("value");

		return new FillForm(formPath, inputName, value);
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
}
