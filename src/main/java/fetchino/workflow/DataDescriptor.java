package fetchino.workflow;

import fetchino.action.ClickElement;
import fetchino.action.FillForm;
import fetchino.action.Request;
import fetchino.action.SaveAll;
import lightdom.Document;
import lightdom.Element;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataDescriptor
{
	private final Context context = new Context();
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
			switch(actionElement.getName())
			{
				case "request":
					actions.add(new Request(actionElement));
					break;
				case "fillForm":
					actions.add(new FillForm(actionElement));
					break;
				case "clickElement":
					actions.add(new ClickElement(actionElement));
					break;
				case "saveAll":
					actions.add(new SaveAll(actionElement));
					break;
				default:
					LoggerFactory.getLogger(DataDescriptor.class).warn("Not implemented: " + actionElement.getName());
			}
		}
	}

	public void fetch()
	{
		actions.forEach(action -> action.execute(context));
	}

	public Context getContext()
	{
		return context;
	}
}
