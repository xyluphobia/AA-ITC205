package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airlock.entities.Door;
import airlock.entities.DoorState;
import airlock.entities.PressureSensor;
import airlock.entities.IPressureSensor;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

class DoorTest {

	@Test
	@DisplayName("Ensures that, with valid inputs, the Door constructor returns a valid instance of door.")
	void testConstructorReturnsValidDoor() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10);
			
            Door door = new Door(exSensor, inSensor, DoorState.OPEN);

			assertInstanceOf(Door.class, door);

		} catch (DoorException | PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures a DoorException is thrown if door is initalised with null values for PressureSensors.")
	void testConstructorThrowsExceptionIfGivenNullPressureSensorValues() throws AirLockException {
		assertThrows(DoorException.class, () -> new Door(null, null, DoorState.CLOSED),
		"Expected Constructor to throw a DoorException due to sensor inputs being null, no exception given.");
	}

	@Test
	@DisplayName("Ensures a PressureException is thrown if door is initalised with invalid values for PressureSensors.")
	void testConstructorThrowsExceptionIfGivenInvalidPressureSensorValues() throws AirLockException {
		try {
			IPressureSensor inSensor = new PressureSensor(0);

		assertThrows(PressureException.class, () -> new Door(new PressureSensor(-2), inSensor, DoorState.CLOSED),
		"Expected Constructor to throw a PressureException due to given sensor values being invalid, no exception given.");

		} catch (PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures a DoorException is thrown if door is initalised with an incompatible doorstate and pressure tolerence.")
	void testConstructorThrowsExceptionIfGivenInvalidDoorStateToleranceValues() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(12);

			assertThrows(DoorException.class, () -> new Door(exSensor, inSensor, DoorState.OPEN),
		"Expected Constructor to throw a DoorException due to DoorState being OPEN while the difference in pressure is > tolerance, no exception given.");
			
		} catch (PressureException e) {
			fail(e);
		}
	}
	
	@Test
	@DisplayName("Ensures that attempting to call open while door is already open will throw a DoorException.")
	void testAttemptingToOpenAnOpenDoorThrowsException() {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(12);

			assertThrows(DoorException.class,
				() -> new Door(exSensor, inSensor, DoorState.OPEN).open(),
				"Expected Door open to throw DoorException due to attempting to open an already open door. No exception thrown.");

		} catch (PressureException e) {

		}
	}

	@Test
	@DisplayName("Ensures that open throws an exception if called when external and internal pressure difference is greater than tolerance.")
	void testOpenThrowsExceptionIfPressureDifferenceGreaterThanTolerance() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(12);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			assertThrows(DoorException.class, () -> door.open(),
			"Expected Door open to throw DoorException for opening when pressure difference > tolerance, but no exception is thrown.");

		} catch (DoorException | PressureException e ) {
			fail(e);
		}
	}
	
	@Test
	@DisplayName("Ensures that open will correctly set the state of a valid door to open.")
	void testOpenOpensAValidDoor() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);

			door.open();
			assertTrue(door.isOpen());

		} catch (DoorException | PressureException e) {
			fail(e);
		} 
	}

	@Test
	@DisplayName("")
	void testAttemptingToCloseAClosedDoorThrowsException() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			
			assertThrows(DoorException.class, () -> door.close(),
			"Expected Door close to throw DoorException for closing a closed door, but no exception is thrown.");

		} catch (DoorException | PressureException e ) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that if no exception is thrown, the door's state becomes closed.")
	void testCloseClosesAValidDoor() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.OPEN);
			
			door.close();
			assertTrue(door.isClosed());
			
		} catch (DoorException | PressureException e ) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that getExternalPressure returns the correct pressure value initalised in the constructor.")
	void testGetExternalPressure() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(12);
			
			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			assertEquals(10, door.getExternalPressure());

		} catch (DoorException | PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that getInternalPressure returns the correct pressure value initalised in the constructor.")
	void testGetInternalPressure() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(12);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			assertEquals(12, door.getInternalPressure());

		} catch (DoorException | PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures isOpen returns true when called on an open door.")
	void testIsOpenOnOpen() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.OPEN);
			assertTrue(door.isOpen());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	
	}

	@Test
	@DisplayName("Ensures isOpen returns false when called on a closed door.")
	void testIsOpenOnClosed() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			assertFalse(door.isOpen());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	
	}

	@Test
	@DisplayName("Ensures isClosed returns false when called on an open door.")
	void testIsClosedOnOpen() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.OPEN);
			assertFalse(door.isClosed());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures isClosed returns true when called on a closed door.")
	void testIsClosedOnClosed() throws AirLockException {
		try {
			IPressureSensor exSensor = new PressureSensor(10);
            IPressureSensor inSensor = new PressureSensor(10.001);

			Door door = new Door(exSensor, inSensor, DoorState.CLOSED);
			assertTrue(door.isClosed());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	}
}
