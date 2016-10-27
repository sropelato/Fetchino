package fetchino.context;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.Util;
import fetchino.util.XPathProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code TempContext} holds temporary variables and can have a {@link WebClient} of its own. All non-temporary variables, lists and maps as well as the {@link XPathProcessor} accessed via its parent context.
 *
 * @version 1.0-SNAPSHOT
 */
public class TempContext implements Context
{
	private final WebClient webClient;
	private final Context parent;
	private final Map<String, String> tempVariables = new HashMap<>();

	/**
	 * Constructor. Set parent context and create new {@link WebClient} if {@code createNewWebClient} is {@code true}.
	 *
	 * @param parent             The parent context.
	 * @param createNewWebClient Controls whether a new web client should be created for this context.
	 */
	public TempContext(Context parent, boolean createNewWebClient)
	{
		this.parent = parent;
		if(createNewWebClient)
			webClient = Util.createWebClient();
		else
			webClient = null;
	}

	/**
	 * {@inheritDoc}
	 * Returns the {@link WebClient} of this context. If this context does not have a web client of its own, the parent's web client will be returned.
	 */
	@Override
	public WebClient getWebClient()
	{
		if(webClient != null)
			return webClient;
		else
			return parent.getWebClient();
	}

	/**
	 * {@inheritDoc}
	 * Returns the parent's {@link XPathProcessor}.
	 */
	@Override
	public XPathProcessor getXPathProcessor()
	{
		return parent.getXPathProcessor();
	}

	/**
	 * Sets a temporary variable. If a variable (temporary or not) with the same name already exists, a RuntimeException will be thrown.
	 *
	 * @param variableName The name of the temporary variable.
	 * @param value        The value to be assigned.
	 */
	public void setTempVariable(String variableName, String value)
	{
		Util.validateVariableName(variableName);
		if(hasVariable(variableName))
			throw new RuntimeException("Variable " + variableName + " already exists in this context");
		tempVariables.put(variableName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasVariable(String variableName)
	{
		return tempVariables.containsKey(variableName) || parent.hasVariable(variableName);
	}

	/**
	 * {@inheritDoc}
	 * If a temporary variable with the given name exists, its value will be returned. Otherwise, the variable value of the parent context will be returned.
	 */
	@Override
	public String getVariable(String variableName)
	{
		if(tempVariables.containsKey(variableName))
			return tempVariables.get(variableName);
		else
			return parent.getVariable(variableName);
	}

	/**
	 * {@inheritDoc}
	 * If a temporary variable with the given name exists, its value will be returned. Otherwise, the variable value of the parent context will be returned.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getVariable(String variableName, Class<T> type)
	{
		if(tempVariables.containsKey(variableName))
		{
			if(RootContext.Type.fromClass(type) == RootContext.Type.STRING)
				return (T)tempVariables.get(variableName);
			else
				throw new RuntimeException("Temporary variables cannot have type " + RootContext.Type.fromClass(type));
		}
		else
			return parent.getVariable(variableName, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIntVariable(String variableName)
	{
		return getVariable(variableName, Integer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloatVariable(String variableName)
	{
		return getVariable(variableName, Float.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBooleanVariable(String variableName)
	{
		return getVariable(variableName, Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void setVariable(String variableName, T value)
	{
		if(tempVariables.containsKey(variableName))
			throw new RuntimeException("Variable " + variableName + " is a temporary variable of this context and cannot be overwritten");
		parent.setVariable(variableName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasList(String listName)
	{
		return parent.hasList(listName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getList(String listName)
	{
		return parent.getList(listName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> getList(String listName, Class<T> type)
	{
		return parent.getList(listName, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getIntList(String listName)
	{
		return getList(listName, Integer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Float> getFloatList(String listName)
	{
		return getList(listName, Float.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Boolean> getBooleanList(String listName)
	{
		return getList(listName, Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void addToList(String listName, T value)
	{
		parent.addToList(listName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMap(String mapName)
	{
		return parent.hasMap(mapName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getMap(String mapName)
	{
		return parent.getMap(mapName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T1, T2> Map<T1, T2> getMap(String mapName, Class<T1> keyType, Class<T2> valueType)
	{
		return parent.getMap(mapName, keyType, valueType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T1, T2> void addToMap(String mapName, T1 key, T2 value)
	{
		parent.addToMap(mapName, key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addVariable(String variableName, RootContext.Type variableType)
	{
		if(tempVariables.containsKey(variableName))
			throw new RuntimeException("Variable '" + variableName + "' is a temporary variable of this context and cannot be overwritten");
		else
			parent.addVariable(variableName, variableType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RootContext.Type getVariableType(String variableName)
	{
		if(tempVariables.containsKey(variableName))
			throw new RuntimeException("Temporary variables have no type");
		else
			return parent.getVariableType(variableName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addList(String listName, RootContext.Type listType)
	{
		parent.addList(listName, listType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RootContext.Type getListType(String listName)
	{
		return parent.getListType(listName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMap(String mapName, RootContext.Type mapKeyType, RootContext.Type mapValueType)
	{
		parent.addMap(mapName, mapKeyType, mapValueType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RootContext.Type getMapKeyType(String mapName)
	{
		return parent.getMapKeyType(mapName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RootContext.Type getMapValueType(String mapName)
	{
		return parent.getMapValueType(mapName);
	}
}
