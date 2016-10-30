package fetchino.action;

import fetchino.context.Context;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

/**
 * The {@code AddToListValue} action adds a new element to a list.
 *
 * @version 1.0-SNAPSHOT
 */
public class AddToListValue extends AddToList
{
	private final String listName;
	private final String value;

	/**
	 * Constructor.
	 *
	 * @param listName Name of the list.
	 * @param value    XPath expression defining the elements to be added to the list.
	 */
	public AddToListValue(String listName, String value)
	{
		this.listName = listName;
		this.value = value;

		Util.validateVariableName(listName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context)
	{
		LoggerFactory.getLogger(AddToListValue.class).debug("Executing action: " + this);
		context.addToList(listName, Util.replacePlaceholders(value, context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "AddToListValue{" +
				"listName='" + listName + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
