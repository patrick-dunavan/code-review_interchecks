package com.interchecks.review.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

@Tag("unitTests")
@Tag("utilUnitTests")

public class UtilTest {

	@Test
	public void test_safeCollection() { 
		
		
		List<String> testList = null;
		Collection<String> coll = null;
		
		//null test
		coll = Util.safeCollection(testList);
		assertEquals(0, coll.size());
		
		//Cannot add to the returned list
		try { 
			coll.add("foo");
			fail("Should throw uoe");
		} catch (UnsupportedOperationException uoe) { 
			assertTrue(true);
		} catch (Exception e) { 
			fail("Should throw uoe");
		}
		
		//Cannot add to the returned list (emptyList)
		testList = new ArrayList<>();
		
		coll = Util.safeCollection(testList);
		assertEquals(0, coll.size());

		try { 
			coll.add("foo");
			fail("Should throw uoe");
		} catch (UnsupportedOperationException uoe) { 
			assertTrue(true);
		} catch (Exception e) { 
			fail("Should throw uoe");
		}

		// Regular, sized list; same size back and elements, but still cannot add 
		testList = new ArrayList<>();
		
		testList.add("FOO");
		testList.add("BAR");
		
		coll = Util.safeCollection(testList);
		assertEquals(2, coll.size());
		
		assertTrue(testList.get(0).equals("FOO"));
		assertTrue(testList.get(1).equals("BAR"));
		
		try { 
			coll.add("foo");
			fail("Should throw uoe");
		} catch (UnsupportedOperationException uoe) { 
			assertTrue(true);
		} catch (Exception e) { 
			fail("Should throw uoe");
		}
		
	}
	
	@Test
	public void testLoggerImpls() { 
		
		Logger mockLogger = Mockito.mock(Logger.class);
		
		when(mockLogger.isDebugEnabled()).thenReturn(true);
		
		Util.debugIf(mockLogger, () -> "foo bar");
		
		//Called the ifEnabled, AND the function
		verify(mockLogger, times(1)).isDebugEnabled();
		verify(mockLogger, times(1)).debug("foo bar");
		
		
		Mockito.clearInvocations(mockLogger);
		
		when(mockLogger.isDebugEnabled()).thenReturn(false);

		Util.debugIf(mockLogger, () -> "foo bar");

		//Called the ifEnabled, but NOT the function.
		verify(mockLogger, times(1)).isDebugEnabled();
		verify(mockLogger, times(0)).debug(anyString());
		
	}
	
	
}
