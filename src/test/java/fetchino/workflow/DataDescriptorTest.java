package fetchino.workflow;

import fetchino.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DataDescriptorTest
{
	@Before
	public void setUp() throws Exception
	{
		Util.setupLogging();
	}

	// Retrieve the names of the Google founders from the Wikipedia article
	@Test
	public void test1() throws Exception
	{
		// Searches Wikipedia article about Wikipedia
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor1.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("founders").containsAll(Arrays.asList("Larry Page", "Sergey Brin")));
	}
}