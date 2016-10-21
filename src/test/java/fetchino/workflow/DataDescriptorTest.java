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

		assertEquals("Toccata and Fugue", dataDescriptor.getContext().getMap("compositionNames").get("565"));
		assertEquals("Six Little Preludes No. 1", dataDescriptor.getContext().getMap("compositionNames").get("933"));
		assertEquals("Suite for cello No. 1", dataDescriptor.getContext().getMap("compositionNames").get("1007"));

		assertEquals("1725-03-25", dataDescriptor.getContext().getMap("compositionDates").get("1"));
		assertEquals("1725-12-27", dataDescriptor.getContext().getMap("compositionDates").get("151"));
		assertEquals("1733-12-08", dataDescriptor.getContext().getMap("compositionDates").get("214"));
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

	// if-then-else
	// check if result from XPath query matches regex pattern and set variable accordingly
	@Test
	public void test4a() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor4a.xml"));
		dataDescriptor.fetch();

		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddie"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("notFoundMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieAndBrian"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundFreddieAndMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieOrMickey"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundMickeyOrMary"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieOrMickeyOrMary"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieXorMickeyXorMary"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundFreddieXorBrianXorMary"));
	}

	// if-then-else
	// check if variable matches regex pattern and set variable accordingly
	@Test
	public void test4b() throws Exception
	{
		DataDescriptor dataDescriptor = new DataDescriptor(this.getClass().getClassLoader().getResourceAsStream("dataDescriptor4b.xml"));
		dataDescriptor.fetch();

		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddie"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("notFoundMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieAndBrian"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundFreddieAndMickey"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieOrMickey"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundMickeyOrMary"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieOrMickeyOrMary"));
		assertEquals("true", dataDescriptor.getContext().getVariable("foundFreddieXorMickeyXorMary"));
		assertEquals("false", dataDescriptor.getContext().getVariable("foundFreddieXorBrianXorMary"));
	}
}