package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;
import fetchino.context.TempContext;
import org.slf4j.LoggerFactory;

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

	/**
	 * Constructor.
	 *
	 * @param path    XPath expression.
	 * @param var     Name of the temporary variable containing the element of the current iteration.
	 * @param actions List of actions executed in each iteration.
	 */
	public ForEachPath(String path, String var, List<Action> actions)
	{
		this(path, var, actions, null);
	}

	/**
	 * Constructor.
	 *
	 * @param path    XPath expression.
	 * @param var     Name of the temporary variable containing the element of the current iteration.
	 * @param actions List of actions executed in each iteration.
	 * @param limit   Mamimum number of iterations to be executed. This may contain variable placeholders. A value of {@code null} or an expression evaluated to a number less than 1 means no limit.
	 */
	public ForEachPath(String path, String var, List<Action> actions, String limit)
	{
		this.path = path;
		this.var = var;
		this.actions = actions;
		this.limit = limit;

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
		int count = 0;
		for(DomNode element : elements)
		{
			if(limit != null && Integer.parseInt(Util.replacePlaceholders(limit, context)) > 0 && count >= Integer.parseInt(Util.replacePlaceholders(limit, context)))
				break;

			String elementValue = (element instanceof DomAttr) ? element.getNodeValue() : element.asText();
			TempContext tempContext = new TempContext(context, false);
			tempContext.setTempVariable(var, elementValue);

			for(Action action : actions)
			{
				action.execute(tempContext);
			}

			count++;
		}
	}

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
