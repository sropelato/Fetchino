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
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor1.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("founders").containsAll(Arrays.asList("Larry Page", "Sergey Brin")));
	}

	// Retrieve title and date of all compositions from Johann Sebastian Bach
	// and store them in maps using the BWV index as key
	@Test
	public void test2() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor2.xml"));
		dataDescriptor.fetch();

		//dataDescriptor.getContext().getMap("compositionNames").forEach((key, value) -> LoggerFactory.getLogger(DataDescriptorTest.class).info(key + ": " + value));

		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("565"), "Toccata and Fugue");
		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("933"), "Six Little Preludes No. 1");
		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("1007"), "Suite for cello No. 1");

		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("1"), "1725-03-25");
		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("151"), "1725-12-27");
		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("214"), "1733-12-08");
	}

	@Test
	public void test3() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor3.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("versions").contains("3.1/"));
	}
}