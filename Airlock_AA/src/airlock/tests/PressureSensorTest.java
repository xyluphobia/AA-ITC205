package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import airlock.entities.PressureSensor;
import airlock.exceptions.PressureException;

class PressureSensorTest {

	@Test
	void testConstructorValid() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(10.2);
			assertEquals(10.2, sensor.getPressure());
		} catch (PressureException e) {
			fail();
			throw new PressureException(e);
		}
	}

	@Test
	void testConstructorInvalid() {
		assertThrows(
				PressureException.class,
		           () -> new PressureSensor(-1),
		           "Expected PressureSensor constructor to throw PressureException, but no exception thrown"
		    );
	}

	@Test
	void testGetPressure() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(6.5);
			assertEquals(6.5, sensor.getPressure());
			sensor.setPressure(3.7);
			assertEquals(3.7, sensor.getPressure());
		} catch (PressureException e) {
			fail();
			throw new PressureException(e);
		}
	}

	@Test
	void testSetPressure() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(8.4);
			assertEquals(8.4, sensor.getPressure());
			assertThrows(PressureException.class, () -> sensor.setPressure(-5.0), "Expected setPressure to throw an exception as negative pressure is not possible. Exception not thrown.");
			sensor.setPressure(3.7);
			assertEquals(3.7, sensor.getPressure());
		} catch (PressureException e) {
			fail();
			throw new PressureException(e);
		}
	}
	
	@Test
	void testToString() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(10.2);
			assertEquals("PressureSensor: pressure: 10.2 bar", sensor.toString());
		} catch (PressureException e) {
			fail();
			throw new PressureException(e);
		}
	}
}
