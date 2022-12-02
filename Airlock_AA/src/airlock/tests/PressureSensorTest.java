package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airlock.entities.PressureSensor;
import airlock.exceptions.PressureException;

class PressureSensorTest {

	@Test
	@DisplayName("Ensures that, with valid inputs, the PressureSensor constructor returns a valid instance of PressureSensor.")
	void testConstructorValidInputReturnsValidObject() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(10.2);
			assertInstanceOf(PressureSensor.class, sensor);

		} catch (PressureException e) {
			fail("A PressureException was thrown unexpectedly.");
		}
	}

	@Test
	@DisplayName("Checks the constructer throws an 'InvalidPressureException' if you attempt to initalise PressureSensor with a number less than 0.")
	void testConstructorInvalidPressureThrowsInvalidPressureException() {
		assertThrows(
				PressureException.class,
		           () -> new PressureSensor(-1),
		           "Expected PressureSensor constructor to throw PressureException due to using an invalid (negative) initial pressure. No exception thrown."
		    );
	}

	@Test
	@DisplayName("Ensures getPressure returns the pressure set initally by the constructor.")
	void testGetPressureReturnsInitialPressure() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(6.5);
			assertEquals(6.5, sensor.getPressure());
			sensor.setPressure(3.7);
			assertEquals(3.7, sensor.getPressure());
		} catch (PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures getPressure returns an updated pressure which is different than the pressure set by the constructor.")
	void testGetPressureReturnsUpdatedPressure() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(6.5);
			sensor.setPressure(3.7);
			assertEquals(3.7, sensor.getPressure());
		} catch (PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that setPressure throws an 'InvalidPressureException' if attempting to set a negative pressure.")
	void testSetPressureInvalidPressureThrowsInvalidPressureException() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(8.4);
			assertThrows(PressureException.class, () -> sensor.setPressure(-5.0), "Expected setPressure to throw an exception as negative pressure is not possible. Exception not thrown.");
		} catch (PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that setPressure updates the pressure variable of the object to be different than what was originally set in the constructor.")
	void testSetPressureUpdatesInitialPressure() throws PressureException {
		try {
			PressureSensor sensor = new PressureSensor(8.4);
			sensor.setPressure(2.2);
			assertEquals(2.2, sensor.getPressure());
		} catch (PressureException e) {
			fail(e);
		}
	}
}
