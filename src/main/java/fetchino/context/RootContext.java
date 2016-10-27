package fetchino.context;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.Util;
import fetchino.util.XPathProcessor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code RootContext} holds all non-temporary variables, lists, maps, a {@link WebClient} and an {@link XPathProcessor}. The context object assigned to a {@link fetchino.main.Fetchino} instance is always a {@code RootContext}.
 *
 * @version 1.0-SNAPSHOT
 */
public class RootContext implements Context
{
	private final WebClient webClient;
	private final XPathProcessor xPathProcessor;
	private final Map<String, String> variables = new HashMap<>();
	private final Map<String, List<String>> lists = new HashMap<>();
	private final Map<String, Map<String, String>> maps = new HashMap<>();

	private final Map<String, Type> variableTypes = new HashMap<>();
	private final Map<String, Type> listTypes = new HashMap<>();
	private final Map<String, Pair<Type, Type>> mapTypes = new HashMap<>();

	/**
	 * The {@code Type} enumerator represents the type of a variable, list element or the key or value of a map.
	 */
	public enum Type
	{
		STRING, INT, FLOAT, BOOLEAN;

		/**
		 * Returns the {@code Type} for a given type string. The comparison is case-insensitive.
		 *
		 * @param typeString The name of the type.
		 * @return The enum value corresponding to {@code typeString}
		 */
		public static Type fromString(String typeString)
		{
			if("string".equalsIgnoreCase(typeString))
				return STRING;
			else if("int".equalsIgnoreCase(typeString))
				return INT;
			else if("float".equalsIgnoreCase(typeString))
				return FLOAT;
			else if("boolean".equalsIgnoreCase(typeString))
				return BOOLEAN;
			else
				throw new RuntimeException("Invalid type: " + typeString);
		}

		/**
		 * Returns the {@code Type} representation for a given class. If no compatible type for {@code cls} can be found, a RuntimeException will be thrown.
		 *
		 * @param cls Type class corresponding to the desired type.
		 * @return The type corresponding to the given class.
		 */
		public static Type fromClass(Class<?> cls)
		{
			if(cls.isAssignableFrom(String.class))
				return STRING;
			else if(cls.isAssignableFrom(Integer.class))
				return INT;
			else if(cls.isAssignableFrom(Float.class))
				return FLOAT;
			else if(cls.isAssignableFrom(Boolean.class))
				return BOOLEAN;
			else
				throw new RuntimeException("No suitable type found for class " + cls.getName());
		}

		/**
		 * Returns the default value for this type:
		 * <table summary="default values for types">
		 * <tr>
		 * <td>STRING</td>
		 * <td>""</td>
		 * </tr>
		 * <tr>
		 * <td>INT</td>
		 * <td>"0"</td>
		 * </tr>
		 * <tr>
		 * <td>FLOAT</td>
		 * <td>"0.0"</td>
		 * </tr>
		 * <tr>
		 * <td>BOOLEAN</td>
		 * <td>"false"</td>
		 * </tr>
		 * </table>
		 *
		 * @return The default value for this type.
		 */
		public String getDefaultValue()
		{
			switch(this)
			{
				case STRING:
					return "";
				case INT:
					return "0";
				case FLOAT:
					return "0.0";
				case BOOLEAN:
					return "false";
				default:
					throw new IllegalArgumentException();
			}
		}

		/**
		 * Checks if a string value is compatible with (i.e. can be assigned to a variable of) this type.
		 *
		 * @param value The value to be checked.
		 * @return {@code true} iff the value is compatible with this type.
		 */
		public boolean isValueCompatible(String value)
		{
			switch(this)
			{
				case STRING:
					return true;
				case INT:
					try
					{
						Integer.parseInt(value);
					}
					catch(NumberFormatException e)
					{
						return false;
					}
					return true;
				case FLOAT:
					try
					{
						Float.parseFloat(value);
					}
					catch(NumberFormatException e)
					{
						return false;
					}
					return true;
				case BOOLEAN:
					return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"));
				default:
					return false;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			switch(this)
			{
				case STRING:
					return "string";
				case INT:
					return "int";
				case FLOAT:
					return "float";
				case BOOLEAN:
					return "boolean";
				default:
					throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * Constructor. Creates default {@link WebClient} and {@link XPathProcessor} instances.
	 */
	public RootContext()
	{
		// create web client
		webClient = Util.createWebClient();

		// create XPath processor
		xPathProcessor = new XPathProcessor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebClient getWebClient()
	{
		return webClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized XPathProcessor getXPathProcessor()
	{
		return xPathProcessor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean hasVariable(String variableName)
	{
		return variableTypes.containsKey(variableName);
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not exist, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized String getVariable(String variableName)
	{
		if(!hasVariable(variableName))
			throw new RuntimeException("Variable does not exist: " + variableName);
		return variables.get(variableName);
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not have the correct type, a RuntimeException will be thrown.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getVariable(String variableName, Class<T> type)
	{
		if(!hasVariable(variableName))
			throw new RuntimeException("Variable does not exist: " + variableName);
		if(Type.fromClass(type) != getVariableType(variableName))
			throw new RuntimeException("Variable " + variableName + " is not of type " + Type.fromClass(type));

		switch(Type.fromClass(type))
		{
			case STRING:
				return (T)getVariable(variableName);
			case INT:
				return (T)Integer.valueOf(getVariable(variableName));
			case FLOAT:
				return (T)Float.valueOf(getVariable(variableName));
			case BOOLEAN:
				return (T)Boolean.valueOf(getVariable(variableName));
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not have the type {@link fetchino.context.RootContext.Type#INT}, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized int getIntVariable(String variableName)
	{
		return getVariable(variableName, Integer.class);
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not have the type {@link fetchino.context.RootContext.Type#FLOAT}, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized float getFloatVariable(String variableName)
	{
		return getVariable(variableName, Float.class);
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not have the type {@link fetchino.context.RootContext.Type#BOOLEAN}, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized boolean getBooleanVariable(String variableName)
	{
		return getVariable(variableName, Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not exist or is not of a compatible type, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized <T> void setVariable(String variableName, T value)
	{
		if(!hasVariable(variableName))
			throw new RuntimeException("Variable does not exist: " + variableName);
		if(value == null)
			throw new RuntimeException("Value cannot be null");
		if(!getVariableType(variableName).isValueCompatible(value.toString()))
			throw new RuntimeException("Value is not compatible with type " + getVariableType(variableName) + ": " + value);

		variables.put(variableName, value.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean hasList(String listName)
	{
		return listTypes.containsKey(listName);
	}

	/**
	 * {@inheritDoc}
	 * If the list does not exist, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized List<String> getList(String listName)
	{
		if(!hasList(listName))
			throw new RuntimeException("List does not exist: " + listName);
		return new ArrayList<>(lists.get(listName));
	}

	/**
	 * {@inheritDoc}
	 * If the list does not have the correct type, a RuntimeException will be thrown.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> List<T> getList(String listName, Class<T> type)
	{
		if(!hasList(listName))
			throw new RuntimeException("List does not exist: " + listName);
		if(Type.fromClass(type) != getListType(listName))
			throw new RuntimeException("Elements in list " + listName + " are not of type " + Type.fromClass(type));

		List<T> result = new ArrayList<>();
		switch(Type.fromClass(type))
		{
			case STRING:
				return (List<T>)getList(listName);
			case INT:
				getList(listName).forEach(element -> result.add((T)Integer.valueOf(element)));
				return result;
			case FLOAT:
				getList(listName).forEach(element -> result.add((T)Float.valueOf(element)));
				return result;
			case BOOLEAN:
				getList(listName).forEach(element -> result.add((T)Boolean.valueOf(element)));
				return result;
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 * If the list does not have the type {@link fetchino.context.RootContext.Type#INT}, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized List<Integer> getIntList(String listName)
	{
		return getList(listName, Integer.class);
	}

	/**
	 * {@inheritDoc}
	 * If the list does not have the type {@link fetchino.context.RootContext.Type#FLOAT}, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized List<Float> getFloatList(String listName)
	{
		return getList(listName, Float.class);
	}

	/**
	 * {@inheritDoc}
	 * If the list does not have the type {@link fetchino.context.RootContext.Type#BOOLEAN}, a RuntimeException will be thrown.
	 */
	@Override
	public List<Boolean> getBooleanList(String listName)
	{
		return getList(listName, Boolean.class);
	}

	/**
	 * {@inheritDoc}
	 * If the list does not exist or is not of a compatible type, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized <T> void addToList(String listName, T value)
	{
		if(!hasList(listName))
			throw new RuntimeException("List does not exist: " + listName);
		if(value == null)
			throw new RuntimeException("Value cannot be null");
		if(!getListType(listName).isValueCompatible(value.toString()))
			throw new RuntimeException("Value is not compatible with type " + getListType(listName) + ": " + value);

		lists.get(listName).add(value.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean hasMap(String mapName)
	{
		return mapTypes.containsKey(mapName);
	}

	/**
	 * {@inheritDoc}
	 * If the map does not exist, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized Map<String, String> getMap(String mapName)
	{
		if(!hasMap(mapName))
			throw new RuntimeException("Map does not exist: " + mapName);
		return new HashMap<>(maps.get(mapName));
	}

	/**
	 * {@inheritDoc}
	 * If the map does not have the correct key and value types, a RuntimeException will be thrown.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T1, T2> Map<T1, T2> getMap(String mapName, Class<T1> keyType, Class<T2> valueType)
	{
		if(!hasMap(mapName))
			throw new RuntimeException("Map does not exist: " + mapName);
		if(Type.fromClass(keyType) != getMapKeyType(mapName) || Type.fromClass(valueType) != getMapValueType(mapName))
			throw new RuntimeException("Entries in map " + mapName + " are not of type (" + Type.fromClass(keyType) + ", " + Type.fromClass(valueType) + ")");

		Map<T1, T2> result = new HashMap<>();
		getMap(mapName).forEach((key, value) ->
		{
			T1 newKey;
			switch(Type.fromClass(keyType))
			{
				case STRING:
					newKey = (T1)key;
					break;
				case INT:
					newKey = (T1)Integer.valueOf(key);
					break;
				case FLOAT:
					newKey = (T1)Float.valueOf(key);
					break;
				case BOOLEAN:
					newKey = (T1)Boolean.valueOf(key);
					break;
				default:
					throw new IllegalArgumentException();
			}

			T2 newValue;
			switch(Type.fromClass(keyType))
			{
				case STRING:
					newValue = (T2)value;
					break;
				case INT:
					newValue = (T2)Integer.valueOf(value);
					break;
				case FLOAT:
					newValue = (T2)Float.valueOf(value);
					break;
				case BOOLEAN:
					newValue = (T2)Boolean.valueOf(value);
					break;
				default:
					throw new IllegalArgumentException();
			}

			result.put(newKey, newValue);
		});

		return result;
	}

	/**
	 * {@inheritDoc}
	 * If the map does not exist or is not of a compatible type, a RuntimeException will be thrown.
	 */
	@Override
	public synchronized <T1, T2> void addToMap(String mapName, T1 key, T2 value)
	{
		if(!hasMap(mapName))
			throw new RuntimeException("Map does not exist: " + mapName);
		if(key == null)
			throw new RuntimeException("Key cannot be null");
		if(value == null)
			throw new RuntimeException("Value cannot be null");
		if(!getMapKeyType(mapName).isValueCompatible(key.toString()))
			throw new RuntimeException("Key is not compatible with type " + getMapKeyType(mapName) + ": " + key);
		if(!getMapValueType(mapName).isValueCompatible(value.toString()))
			throw new RuntimeException("Value is not compatible with type " + getMapValueType(mapName) + ": " + value);

		maps.get(mapName).put(key.toString(), value.toString());
	}

	/**
	 * {@inheritDoc}
	 * If a variable with the same name already exists, a RuntimeException will be thrown.
	 */
	@Override
	public void addVariable(String variableName, Type variableType)
	{
		if(variableTypes.containsKey(variableName))
			throw new RuntimeException("Variable already exists: " + variableName);

		variableTypes.put(variableName, variableType);
		variables.put(variableName, variableType.getDefaultValue());
	}

	/**
	 * {@inheritDoc}
	 * If the variable does not exist, a RuntimeException will be thrown.
	 */
	@Override
	public Type getVariableType(String variableName)
	{
		if(!variableTypes.containsKey(variableName))
			throw new RuntimeException("Variable does not exist: " + variableName);

		return variableTypes.get(variableName);
	}

	/**
	 * {@inheritDoc}
	 * If a list with the same name already exists, a RuntimeException will be thrown.
	 */
	@Override
	public void addList(String listName, Type listType)
	{
		if(listTypes.containsKey(listName))
			throw new RuntimeException("List already exists: " + listName);

		listTypes.put(listName, listType);
		lists.put(listName, new ArrayList<>());
	}

	/**
	 * {@inheritDoc}
	 * If the list does not exist, a RuntimeException will be t
	 */
	@Override
	public Type getListType(String listName)
	{
		if(!listTypes.containsKey(listName))
			throw new RuntimeException("List does not exist: " + listName);
		return listTypes.get(listName);
	}

	/**
	 * {@inheritDoc}
	 * If a map with the same name already exists, a RuntimeException will be thrown.
	 */
	@Override
	public void addMap(String mapName, Type mapKeyType, Type mapValueType)
	{
		if(mapTypes.containsKey(mapName))
			throw new RuntimeException("Map already exists: " + mapName);

		mapTypes.put(mapName, Pair.of(mapKeyType, mapValueType));
		maps.put(mapName, new HashMap<>());
	}

	/**
	 * {@inheritDoc}
	 * If the map does not exist, a RuntimeException will be t
	 */
	@Override
	public Type getMapKeyType(String mapName)
	{
		if(!mapTypes.containsKey(mapName))
			throw new RuntimeException("Map does not exist: " + mapName);

		return mapTypes.get(mapName).getKey();
	}

	/**
	 * {@inheritDoc}
	 * If the map does not exist, a RuntimeException will be t
	 */
	@Override
	public Type getMapValueType(String mapName)
	{
		if(!mapTypes.containsKey(mapName))
			throw new RuntimeException("Map does not exist: " + mapName);

		return mapTypes.get(mapName).getValue();
	}
}
