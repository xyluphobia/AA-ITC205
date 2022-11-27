package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import airlock.entities.PressureSensor;
import airlock.exceptions.PressureException;

class PressureSensorTest {

	@Test
	void testConstructorValid() {
		try {
			PressureSensor sensor = new PressureSensor(10.2);
			assertEquals(10.2, sensor.getPressure());
		} catch (PressureException e) {
			e.printStackTrace();
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
	void testToString() {
		try {
			PressureSensor sensor = new PressureSensor(10.2);
			assertEquals("PressureSensor: pressure: 10.2 bar", sensor.toString());
		} catch (PressureException e) {
			e.printStackTrace();
		}
	}
}
