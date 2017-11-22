package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import fetchino.util.Util;
import fetchino.context.Context;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Request} action performs an HTTP request.
 *
 * @version 1.0-SNAPSHOT
 */
public class Request implements Action
{
	private final String url;
	private final HttpMethod method;
	private final List<NameValuePair> params = new ArrayList<>();
	private NameValuePair credentials = null;

	/**
	 * Constructor
	 *
	 * @param url    URL of the requested site.
	 * @param method Method of the HTTP request.
	 */
	public Request(String url, HttpMethod method)
	{
		this.url = url;
		this.method = method;
	}

	/**
	 * Constructor
	 *
	 * @param url         URL of the requested site.
	 * @param method      Method of the HTTP request.
	 * @param params      List of parameters added to the request.
	 * @param credentials NameValuePair with username and password.
	 */
	public Request(String url, HttpMethod method, List<NameValuePair> params, NameValuePair credentials)
	{
		this(url, method);
		if(params != null)
			this.params.addAll(params);
		this.credentials = credentials;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(Request.class).debug("Executing action: " + this);
		try
		{
			WebRequest webRequest = new WebRequest(UrlUtils.toUrlUnsafe(Util.replacePlaceholders(url, context)), method);
			List<NameValuePair> replacedParams = new ArrayList<>();
			params.forEach(param -> replacedParams.add(new NameValuePair(param.getName(), param.getValue())));
			webRequest.setRequestParameters(replacedParams);
			if(credentials != null)
				webRequest.setCredentials(new UsernamePasswordCredentials(credentials.getName(), credentials.getValue()));
			context.getWebClient().getPage(webRequest);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Request{" +
				"url='" + url + '\'' +
				", method=" + method +
				", params=" + params +
				'}';
	}
}
