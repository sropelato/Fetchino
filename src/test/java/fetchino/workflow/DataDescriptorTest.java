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

	// open Wikipedia main page and search for Google article
	// retrieve the names of the founders and store them in a list
	@Test
	public void test1() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor1.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("founders").containsAll(Arrays.asList("Larry Page", "Sergey Brin")));
	}

	// retrieve title and date of all compositions by Johann Sebastian Bach
	// and store them in maps using the BWV index as key
	@Test
	public void test2() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor2.xml"));
		dataDescriptor.fetch();

		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("565"), "Toccata and Fugue");
		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("933"), "Six Little Preludes No. 1");
		assertEquals(dataDescriptor.getContext().getMap("compositionNames").get("1007"), "Suite for cello No. 1");

		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("1"), "1725-03-25");
		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("151"), "1725-12-27");
		assertEquals(dataDescriptor.getContext().getMap("compositionDates").get("214"), "1733-12-08");
	}

	// forEach loop
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3a() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor3a.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-1.0.jar"));
		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-2.0.jar"));
		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-3.0.jar"));
	}

	// forEach loop over list
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3b() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor3b.xml"));
		dataDescriptor.fetch();

		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-1.0.jar"));
		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-2.0.jar"));
		assertTrue(dataDescriptor.getContext().getList("jarFiles").contains("commons-httpclient-3.0.jar"));
	}
}