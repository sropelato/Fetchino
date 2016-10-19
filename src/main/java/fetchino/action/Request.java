package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
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

	public Request(URL url, HttpMethod method)
	{
		this.url = url;
		this.method = method;
	}

	public Request(URL url, HttpMethod method, List<NameValuePair> params)
	{
		this(url, method);
		this.params.addAll(params);
	}

	@Override
	public void execute(WebClient webClient, Context context)
	{
		try
		{
			WebRequest webRequest = new WebRequest(url, method);
			webRequest.setRequestParameters(params);
			webClient.getPage(url);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
