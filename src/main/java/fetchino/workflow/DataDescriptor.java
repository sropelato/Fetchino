package fetchino.workflow;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import fetchino.action.*;
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
	private final WebClient webClient;

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

		// create web client
		webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
	}

	public void fetch()
	{
		for(Action action : actions)
		{
			action.execute(webClient, context);
		}
	}

	public Context getContext()
	{
		return context;
	}
}
