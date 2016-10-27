package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;
import fetchino.context.TempContext;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ForEachPath} action executes nested actions for each element returned by an XPath query.
 *
 * @version 1.0-SNAPSHOT
 */
public class ForEachPath extends ForEach
{
	private final String path;
	private final String var;
	private final List<Action> actions;
	private final String limit;
	private final boolean parallel;

	/**
	 * Constructor.
	 *
	 * @param path    XPath expression.
	 * @param var     Name of the temporary variable containing the element of the current iteration.
	 * @param actions List of actions executed in each iteration.
	 */
	public ForEachPath(String path, String var, List<Action> actions)
	{
		this(path, var, actions, null, false);
	}

	/**
	 * Constructor.
	 *
	 * @param path     XPath expression.
	 * @param var      Name of the temporary variable containing the element of the current iteration.
	 * @param actions  List of actions executed in each iteration.
	 * @param limit    Mamimum number of iterations to be executed. This may contain variable placeholders. A value of {@code null} or an expression evaluated to a number less than 1 means no limit.
	 * @param parallel Process actions parallel.
	 */
	public ForEachPath(String path, String var, List<Action> actions, String limit, boolean parallel)
	{
		this.path = path;
		this.var = var;
		this.actions = actions;
		this.limit = limit;
		this.parallel = parallel;

		Util.validateXPathExpression(path);
		Util.validateVariableName(var);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(ForEachPath.class).debug("Executing action: " + this);
		List<DomNode> elements = context.getXPathProcessor().getElementsOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		List<Thread> threads = new ArrayList<>();

		int count = 0;
		for(DomNode element : elements)
		{
			if(limit != null && Integer.parseInt(Util.replacePlaceholders(limit, context)) > 0 && count >= Integer.parseInt(Util.replacePlaceholders(limit, context)))
				break;

			String elementValue = (element instanceof DomAttr) ? element.getNodeValue() : element.asText();
			final TempContext tempContext = new TempContext(context, parallel);
			tempContext.setTempVariable(var, elementValue);

			if(parallel)
			{
				threads.add(new Thread(() ->
				{
					for(Action action : actions)
					{
						action.execute(tempContext);
					}
				}));
			}
			else
			{
				for(Action action : actions)
				{
					action.execute(tempContext);
				}
			}

			count++;
		}

		if(parallel)
		{
			threads.forEach(Thread::start);
			for(Thread thread : threads)
			{
				try
				{
					thread.join();
				}
				catch(InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ForEachPath{" +
				"path='" + path + '\'' +
				", var='" + var + '\'' +
				", actions=" + actions +
				", limit='" + limit + '\'' +
				'}';
	}
}
