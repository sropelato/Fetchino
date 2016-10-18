package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import fetchino.workflow.Action;
import fetchino.workflow.Context;
import lightdom.Element;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Request implements Action
{
	private final URL url;
	private final HttpMethod method;
	private final List<NameValuePair> params = new ArrayList<>();

	public Request(Element requestElement)
	{
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
	}

	@Override
	public void execute(Context context)
	{
		try
		{
			WebRequest webRequest = new WebRequest(url, method);
			webRequest.setRequestParameters(params);
			context.setCurrentPage(context.getWebClient().getPage(webRequest));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
