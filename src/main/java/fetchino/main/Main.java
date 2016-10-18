package fetchino.main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.html.*;
import fetchino.util.Util;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main
{
	public static void main(String[] args)
	{
		// setup logging
		Util.setupLogging();

		try
		{
			WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
			HtmlPage page1 = webClient.getPage("https://en.wikipedia.org/wiki/Main_Page");

			// get form by id
			HtmlForm form = (HtmlForm)page1.getByXPath("//form[@id='searchform']").get(0);

			// get search field and submit button
			HtmlTextInput searchTextField = form.getInputByName("search");
			HtmlSubmitInput button = form.getInputByName("go");

			// set search value
			searchTextField.setValueAttribute("Wikipedia");

			// submit form by clicking button
			HtmlPage page2 = button.click();

			// get description
			String description = ((HtmlElement)page2.getByXPath("//div[@id='mw-content-text']/p[1]").get(0)).asText();
			LoggerFactory.getLogger(Main.class).info(description);

			// get creators
			List<String> creators = new ArrayList<>();
			page2.getByXPath("//div[@id='mw-content-text']/table[@class='infobox']/tbody/tr/th[contains(., 'Created by')]/following-sibling::td/a").forEach(creator -> creators.add(((HtmlElement)creator).asText()));
			creators.forEach(creator -> LoggerFactory.getLogger(Main.class).info((creator.equals(creators.get(0))) ? "Created by: " + creator : "            " + creator));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
