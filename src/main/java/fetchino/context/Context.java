package fetchino.context;

import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.util.XPathProcessor;

import java.util.List;
import java.util.Map;

/**
 * The {@code Context} class holds all variables, lists and maps. It is modified by actions executed when fetchin data.
 *
 * @version 1.0-SNAPSHOT
 * @see fetchino.action.Action
 */
public interface Context
{
	/**
	 * @return The {@link WebClient} assigned to this context.
	 */
	WebClient getWebClient();

	/**
	 * @return The {@link XPathProcessor} assigned to the context.
	 */
	XPathProcessor getXPathProcessor();

	/**
	 * @param variableName The name of the variable.
	 * @return {@code true} iff a variable with the given name exists.
	 */
	boolean hasVariable(String variableName);

	/**
	 * Returns the string value of the variable.
	 *
	 * @param variableName The name of the variable.
	 * @return The string value of the variable.
	 */
	String getVariable(String variableName);

	/**
	 * Returns the variable value converted to the given type.
	 * The type {@code T} must match a corresponding {@link fetchino.context.RootContext.Type}.
	 *
	 * @param variableName The name of the variable.
	 * @param type         Class of the return type.
	 * @param <T>          Return type.
	 * @return Variable value converted to the given type.
	 */
	<T> T getVariable(String variableName, Class<T> type);

	/**
	 * Returns the variable value converted to an integer value.
	 *
	 * @param variableName The name of the variable.
	 * @return Variable value converted to int.
	 */
	int getIntVariable(String variableName);

	/**
	 * Returns the variable value converted to a float value.
	 *
	 * @param variableName The name of the variable.
	 * @return Variable value converted to float.
	 */
	float getFloatVariable(String variableName);

	/**
	 * Returns the variable value converted to a boolean value.
	 *
	 * @param variableName The name of the variable.
	 * @return Variable value converted to boolean.
	 */
	boolean getBooleanVariable(String variableName);

	/**
	 * Assigns a value to a variable. The type {@code T} must match the corresponding {@link fetchino.context.RootContext.Type} of the variable.
	 *
	 * @param variableName The name of the variable.
	 * @param value        The value to be assigned.
	 * @param <T>          The type of the assigned value.
	 */
	<T> void setVariable(String variableName, T value);

	/**
	 * @param listName The name of the list.
	 * @return {@code true} iff a list with the given name exists.
	 */
	boolean hasList(String listName);

	/**
	 * @param listName The name of the list.
	 * @return A new list containing the string representations of the list's elements.
	 */
	List<String> getList(String listName);

	/**
	 * Returns a list of elements converted to the given type.
	 * The type {@code T} must match a corresponding {@link fetchino.context.RootContext.Type}.
	 *
	 * @param listName The name of the list.
	 * @param type     Class of the return type.
	 * @param <T>      Return type.
	 * @return A new list containing the list's elements converted to the given type.
	 */
	<T> List<T> getList(String listName, Class<T> type);

	/**
	 * Returns a list of elements converted to integer values.
	 *
	 * @param listName The name of the list.
	 * @return A new list containing the list's elements converted to int.
	 */
	List<Integer> getIntList(String listName);

	/**
	 * Returns a list of elements converted to float values.
	 *
	 * @param listName The name of the list.
	 * @return A new list containing the list's elements converted to float.
	 */
	List<Float> getFloatList(String listName);

	/**
	 * Returns a list of elements converted to boolean values.
	 *
	 * @param listName The name of the list.
	 * @return A new list containing the list's elements converted to boolean.
	 */
	List<Boolean> getBooleanList(String listName);

	/**
	 * Adds an element to the list with the given name. The type {@code T} must match the corresponding {@link fetchino.context.RootContext.Type} of the list.
	 *
	 * @param listName The name of the list.
	 * @param value    The value to be added.
	 * @param <T>      The type of the added value.
	 */
	<T> void addToList(String listName, T value);

	/**
	 * @param mapName The name of the map.
	 * @return {@code true} iff a map with the given name exists.
	 */
	boolean hasMap(String mapName);

	/**
	 * @param mapName The name of the list.
	 * @return A new map containing the {@code (<String>, <String>)} entries of the map.
	 */
	Map<String, String> getMap(String mapName);

	/**
	 * Returns a map with entries converted to the given types.
	 * The types {@code T1} and {@code T2} must match a corresponding {@link fetchino.context.RootContext.Type}.
	 *
	 * @param mapName   The name of the map.
	 * @param keyType   Class of the key type.
	 * @param valueType Class of the key type.
	 * @param <T1>      Key type.
	 * @param <T2>      Value type.
	 * @return A new map containing the map's elements converted to entries of type {@code (<T1>, <T2>)}.
	 */
	<T1, T2> Map<T1, T2> getMap(String mapName, Class<T1> keyType, Class<T2> valueType);

	/**
	 * Adds an entry to the map with the given name. The types {@code T1} and {@code T2} must match the corresponding key and value types ({@link fetchino.context.RootContext.Type}) of the map.
	 *
	 * @param mapName The name of the list.
	 * @param key     The key to be added.
	 * @param value   The value to be added.
	 * @param <T1>    The type of the added key.
	 * @param <T2>    The type of the added value.
	 */
	<T1, T2> void addToMap(String mapName, T1 key, T2 value);

	/**
	 * Adds a new variable of a given type to the context and initializes it with a default value.
	 *
	 * @param variableName The name of the variable.
	 * @param variableType The type of the variable.
	 * @see RootContext.Type#getDefaultValue()
	 */
	void addVariable(String variableName, RootContext.Type variableType);

	/**
	 * @param variableName The name of the variable
	 * @return The type of the variable.
	 */
	RootContext.Type getVariableType(String variableName);

	/**
	 * Adds a new list of a given type to the context.
	 *
	 * @param listName The name of the list.
	 * @param listType The type of the list.
	 */
	void addList(String listName, RootContext.Type listType);

	/**
	 * @param listName The name of the list.
	 * @return The type of the list.
	 */
	RootContext.Type getListType(String listName);

	/**
	 * Adds a new map with a given key and value type to the context.
	 *
	 * @param mapName      The name of the map.
	 * @param mapKeyType   The key type of the map.
	 * @param mapValueType The value type of the map.
	 */
	void addMap(String mapName, RootContext.Type mapKeyType, RootContext.Type mapValueType);

	/**
	 * @param mapName The name of the map.
	 * @return The key type of the map.
	 */
	RootContext.Type getMapKeyType(String mapName);

	/**
	 * @param mapName The name of the map.
	 * @return The value type of the map.
	 */
	RootContext.Type getMapValueType(String mapName);
}
