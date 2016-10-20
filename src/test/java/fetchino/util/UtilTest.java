package fetchino.util;

import fetchino.workflow.Context;
import fetchino.workflow.RootContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest
{
	@Before
	public void setUp() throws Exception
	{
		Util.setupLogging();
	}

	@Test
	public void replacePlaceholders() throws Exception
	{
		Context context = new RootContext();
		context.setVariable("var1", "value1");
		context.setVariable("var2", "value2");
		context.setVariable("var3", "value3");

		String stringWithPlaceholders = "aaa${var1}bbb${var2}ccc${var3}";
		assertEquals("aaavalue1bbbvalue2cccvalue3", Util.replacePlaceholders(stringWithPlaceholders, context));
	}
}