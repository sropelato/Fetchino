package fetchino.action;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import fetchino.context.Context;
import fetchino.context.RootContext;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Sleep} action waits for the defined amount of time.
 *
 * @version 1.0-SNAPSHOT
 */
public class Sleep implements Action
{
	private final String millis;

	/**
	 * Constructor.
	 *
	 * @param millis The wait time in milliseconds. This may contain placeholders.
	 */
	public Sleep(String millis)
	{
		this.millis = millis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(Sleep.class).debug("Executing action: " + this);
		if(!RootContext.Type.INT.isValueCompatible(Util.replacePlaceholders(millis, context)))
			throw new RuntimeException("Not an int value: " + millis);
		try
		{
			Thread.sleep(Long.parseLong(Util.replacePlaceholders(millis, context)));
		}
		catch(InterruptedException e)
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
		return "Sleep{" +
				"millis='" + millis + '\'' +
				'}';
	}
}
