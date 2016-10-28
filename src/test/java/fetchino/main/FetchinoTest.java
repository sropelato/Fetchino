package fetchino.main;

import fetchino.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FetchinoTest
{
	@Before
	public void setUp() throws Exception
	{
		Util.setupLogging(Util.LogLevel.DEBUG);
	}

	// open Wikipedia main page and search for Google article
	// retrieve the names of the founders and store them in a list
	@Test
	public void test1() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("GoogleFounders.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getList("founders").containsAll(Arrays.asList("Larry Page", "Sergey Brin")));
	}

	// retrieve title and date of all compositions by Johann Sebastian Bach
	// and store them in maps using the BWV index as key
	@Test
	public void test2() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("BachCompositions.xml"));
		fetchino.fetch();

		assertEquals("Toccata and Fugue", fetchino.getContext().getMap("compositionNames").get("565"));
		assertEquals("Six Little Preludes No. 1", fetchino.getContext().getMap("compositionNames").get("933"));
		assertEquals("Suite for cello No. 1", fetchino.getContext().getMap("compositionNames").get("1007"));

		assertEquals("1725-03-25", fetchino.getContext().getMap("compositionDates").get("1"));
		assertEquals("1725-12-27", fetchino.getContext().getMap("compositionDates").get("151"));
		assertEquals("1733-12-08", fetchino.getContext().getMap("compositionDates").get("214"));
	}

	// forEach loop
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3a() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("CommonsHttpClientReleases.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-1.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-2.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-3.0.jar"));
	}

	// forEach loop over list
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3b() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("CommonsHttpClientReleases2.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-1.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-2.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-3.0.jar"));
	}

	// forEach loop with limit
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3c() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("CommonsHttpClientReleases3.xml"));
		fetchino.getContext().setVariable("limit", 3);
		fetchino.fetch();

		assertEquals(3, fetchino.getContext().getList("metadataFiles").size());
	}

	// parallel forEach loop
	// get list of all versions of commons-httpclient in the Maven Central Repository
	// for each version, get the list of all JAR files and store them in a list
	@Test
	public void test3d() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("CommonsHttpClientReleases4.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-1.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-2.0.jar"));
		assertTrue(fetchino.getContext().getList("jarFiles").contains("commons-httpclient-3.0.jar"));
	}

	// if-then-else
	// check if result from XPath query matches regex pattern and set variable accordingly
	@Test
	public void test4a() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("Queen.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddie"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("notFoundMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieAndBrian"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundFreddieAndMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieOrMickey"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundMickeyOrMary"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieOrMickeyOrMary"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieXorMickeyXorMary"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundFreddieXorBrianXorMary"));
	}

	// if-then-else
	// check if variable matches regex pattern and set variable accordingly
	@Test
	public void test4b() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("Queen2.xml"));
		fetchino.fetch();

		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddie"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("notFoundMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieAndBrian"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundFreddieAndMickey"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieOrMickey"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundMickeyOrMary"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieOrMickeyOrMary"));
		assertTrue(fetchino.getContext().getBooleanVariable("foundFreddieXorMickeyXorMary"));
		assertFalse(fetchino.getContext().getBooleanVariable("foundFreddieXorBrianXorMary"));
	}

	// test input on HTML page.
	@Test
	public void test5() throws Exception
	{
		Fetchino fetchino = Fetchino.fromConfig(this.getClass().getClassLoader().getResourceAsStream("InputTest.xml"));
		fetchino.fetch();

		assertEquals("Option 3", fetchino.getContext().getVariable("option"));
		assertEquals("checked", fetchino.getContext().getVariable("checkbox1"));
		assertEquals("unchecked", fetchino.getContext().getVariable("checkbox2"));
		assertEquals("Selection 2", fetchino.getContext().getVariable("selection"));
	}
}
