package fetchino.util;

import fetchino.context.RootContext;
import fetchino.context.TempContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest
{
	@Before
	public void setUp() throws Exception
	{
		Util.setupLogging(Util.LogLevel.DEBUG);
	}

	@Test
	public void replacePlaceholders() throws Exception
	{
		RootContext rootContext = new RootContext();
		TempContext tempContext = new TempContext(rootContext, false);

		tempContext.addVariable("var1", RootContext.Type.STRING);
		tempContext.addVariable("var2", RootContext.Type.INT);
		tempContext.addVariable("var3", RootContext.Type.FLOAT);
		tempContext.addVariable("var4", RootContext.Type.BOOLEAN);

		tempContext.setVariable("var1", "value1");
		tempContext.setVariable("var2", 2);
		tempContext.setVariable("var3", 3.14159);
		tempContext.setVariable("var4", true);
		tempContext.setTempVariable("var5", "temp1");

		String stringWithPlaceholders = "aaa${var1}bbb${var2}ccc${var3}ddd${var4}eee${var5}";
		assertEquals("aaavalue1bbb2ccc3.14159dddtrueeeetemp1", Util.replacePlaceholders(stringWithPlaceholders, tempContext));
	}
}
