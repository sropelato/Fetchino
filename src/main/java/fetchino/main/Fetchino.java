package fetchino.main;

import fetchino.action.Action;
import fetchino.action.ActionParser;
import fetchino.context.RootContext;
import fetchino.util.Util;
import lightdom.Document;
import lightdom.Element;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Fetchino} class holds a {@link RootContext} and a list of {@link Action}s.
 */
public class Fetchino
{
	private final RootContext context;
	private final List<Action> actions;

	/**
	 * Constructor. Creates a new context and initializes the list of actions.
	 */
	public Fetchino()
	{
		context = new RootContext();
		actions = new ArrayList<>();
	}

	/**
	 * Creates a {@code Fetchino} instance from a configuration file.
	 *
	 * @param configInputStream The input stream of the configuration file.
	 * @return A new {@code Fetchino} instance.
	 */
	public static Fetchino fromConfig(InputStream configInputStream)
	{
		return fromConfig(Document.fromInputStream(configInputStream));
	}

	/**
	 * Creates a {@code Fetchino} instance from a configuration file.
	 *
	 * @param configFile The configuration file.
	 * @return A new {@code Fetchino} instance.
	 */
	public static Fetchino fromConfig(File configFile)
	{
		return fromConfig(Document.fromFile(configFile));
	}

	/**
	 * Creates a {@code Fetchino} instance from a configuration file.
	 *
	 * @param configFilename The filename the configuration file.
	 * @return A new {@code Fetchino} instance.
	 */
	public static Fetchino fromConfig(String configFilename)
	{
		return fromConfig(Document.fromFile(configFilename));
	}

	/**
	 * Creates a {@code Fetchino} instance from a configuration file.
	 *
	 * @param configDoc The configuration file document.
	 * @return A new {@code Fetchino} instance.
	 */
	public static Fetchino fromConfig(Document configDoc)
	{
		Fetchino fetchino = new Fetchino();

		if(configDoc.getRootElement().hasElementWithName("data"))
		{
			// variables
			for(Element varElement : configDoc.getRootElement().getElementsByName("data/var"))
			{
				String name;
				String type;

				if(!varElement.hasAttributeWithName("name"))
					throw new RuntimeException("var has no name attribute");
				else
					name = varElement.getAttribute("name");
				Util.validateVariableName(name);

				if(!varElement.hasAttributeWithName("type"))
					throw new RuntimeException("var has no type attribute");
				else
					type = varElement.getAttribute("type");

				fetchino.getContext().addVariable(name, RootContext.Type.fromString(type));
			}

			// lists
			for(Element listElement : configDoc.getRootElement().getElementsByName("data/list"))
			{
				String name;
				String type;

				if(!listElement.hasAttributeWithName("name"))
					throw new RuntimeException("list has no name attribute");
				else
					name = listElement.getAttribute("name");
				Util.validateVariableName(name);

				if(!listElement.hasAttributeWithName("type"))
					throw new RuntimeException("list has no type attribute");
				else
					type = listElement.getAttribute("type");

				fetchino.getContext().addList(name, RootContext.Type.fromString(type));
			}

			// maps
			for(Element mapElement : configDoc.getRootElement().getElementsByName("data/map"))
			{
				String name;
				String keyType;
				String valueType;

				if(!mapElement.hasAttributeWithName("name"))
					throw new RuntimeException("map has no name attribute");
				else
					name = mapElement.getAttribute("name");
				Util.validateVariableName(name);

				if(!mapElement.hasAttributeWithName("keyType"))
					throw new RuntimeException("map has no keyType attribute");
				else
					keyType = mapElement.getAttribute("keyType");

				if(!mapElement.hasAttributeWithName("valueType"))
					throw new RuntimeException("map has no valueType attribute");
				else
					valueType = mapElement.getAttribute("valueType");

				fetchino.getContext().addMap(name, RootContext.Type.fromString(keyType), RootContext.Type.fromString(valueType));
			}
		}

		if(!configDoc.getRootElement().hasElementWithName("workflow"))
			throw new RuntimeException("Data descriptor has no workflow element");

		Element workflowElement = configDoc.getRootElement().getElementByName("workflow");
		for(Element actionElement : workflowElement.getElements())
		{
			fetchino.getActions().add(ActionParser.parse(actionElement));
		}

		return fetchino;
	}

	public void fetch()
	{
		for(Action action : actions)
		{
			action.execute(context);
		}
	}

	/**
	 * @return The context of this instance.
	 */
	public RootContext getContext()
	{
		return context;
	}

	/**
	 * @return The action list of this instance.
	 */
	public List<Action> getActions()
	{
		return actions;
	}
}
