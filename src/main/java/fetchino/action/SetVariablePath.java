package fetchino.action;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import fetchino.util.Util;
import fetchino.context.Context;
import org.slf4j.LoggerFactory;

/**
 * The {@code SetVariable} action assigns a the result of an XPath query to a variable.
 *
 * @version 1.0-SNAPSHOT
 */
public class SetVariablePath extends SetVariable
{
	private final String variableName;
	private final String path;

	/**
	 * Constructor.
	 *
	 * @param variableName Name of the variable.
	 * @param path         XPath expression. This must return exactly one element.
	 */
	public SetVariablePath(String variableName, String path)
	{
		this.variableName = variableName;
		this.path = path;

		Util.validateVariableName(variableName);
		Util.validateXPathExpression(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(SetVariablePath.class).debug("Executing action: " + this);
		DomNode element = context.getXPathProcessor().getSingleElementOfType(Util.getCurrentPage(context), Util.replacePlaceholders(path, context), DomNode.class);
		context.setVariable(variableName, (element instanceof DomAttr) ? element.getNodeValue() : element.asText());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "SetVariablePath{" +
				"variableName='" + variableName + '\'' +
				", path='" + path + '\'' +
				'}';
	}
}
