package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import fetchino.util.Util;
import fetchino.workflow.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Request implements Action
{
	private final String urlString;
	private final HttpMethod method;
	private final List<NameValuePair> params = new ArrayList<>();

	public Request(String url, HttpMethod method)
	{
		this.urlString = url;
		this.method = method;
	}

	public Request(String url, HttpMethod method, List<NameValuePair> params)
	{
		this(url, method);
		this.params.addAll(params);
	}

	@Override
	public void execute(Context context)
	{
		try
		{
			URL url = UrlUtils.toUrlUnsafe(Util.replacePlaceholders(urlString, context));

			WebRequest webRequest = new WebRequest(url, method);
			List<NameValuePair> replacedParams = new ArrayList<>();
			params.forEach(param -> replacedParams.add(new NameValuePair(param.getName(), param.getValue())));
			webRequest.setRequestParameters(replacedParams);
			context.getWebClient().getPage(url);
		}
		catch(MalformedURLException e)
		{
			throw new RuntimeException(e);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
