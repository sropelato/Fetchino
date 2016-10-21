package fetchino.workflow;

import fetchino.action.Action;
import fetchino.action.ActionParser;
import lightdom.Document;
import lightdom.Element;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataDescriptor
{
	private final RootContext context = new RootContext();
	private final List<Action> actions = new ArrayList<>();

	public DataDescriptor(InputStream dataDescriptorInputStream)
	{
		this(Document.fromInputStream(dataDescriptorInputStream));
	}

	public DataDescriptor(File dataDescriptorFile)
	{
		this(Document.fromFile(dataDescriptorFile));
	}

	public DataDescriptor(String dataDescriptorFilename)
	{
		this(Document.fromFile(dataDescriptorFilename));
	}

	private DataDescriptor(Document doc)
	{
		if(doc.getRootElement().getElementByName("workflow") == null)
			throw new RuntimeException("Data descriptor has no workflow element");

		Element workflowElement = doc.getRootElement().getElementByName("workflow");

		for(Element actionElement : workflowElement.getElements())
		{
			actions.add(ActionParser.parse(actionElement));
		}
	}

	public void fetch()
	{
		for(Action action : actions)
		{
			action.execute(context);
		}
	}

	public RootContext getContext()
	{
		return context;
	}
}
