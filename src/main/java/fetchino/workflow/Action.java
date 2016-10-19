package fetchino.workflow;

import com.gargoylesoftware.htmlunit.WebClient;

public interface Action
{
	void execute(WebClient webClient, Context context);
}
