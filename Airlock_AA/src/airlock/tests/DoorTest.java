package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airlock.entities.Door;
import airlock.entities.DoorState;
import airlock.entities.PressureSensor;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

class DoorTest {

	@Test
	@DisplayName("Ensures that, with valid inputs, the Door constructor returns a valid instance of door.")
	void testConstructorReturnsValidDoor() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN);
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
		assertThrows(PressureException.class, () -> new Door(new PressureSensor(-2), new PressureSensor(0), DoorState.CLOSED),
		"Expected Constructor to throw a PressureException due to given sensor values being invalid, no exception given.");
	}

	@Test
	@DisplayName("Ensures a DoorException is thrown if door is initalised with an incompatible doorstate and pressure tolerence.")
	void testConstructorThrowsExceptionIfGivenInvalidDoorStateToleranceValues() throws AirLockException {
		assertThrows(DoorException.class, () -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.OPEN),
		"Expected Constructor to throw a DoorException due to DoorState being OPEN while the difference in pressure is > tolerance, no exception given.");
	}
	
	@Test
	@DisplayName("Ensures that attempting to call open while door is already open will throw a DoorException.")
	void testAttemptingToOpenAnOpenDoorThrowsException() {
		assertThrows(DoorException.class,
				() -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.OPEN).open(),
				"Expected Door open to throw DoorException due to attempting to open an already open door. No exception thrown.");

	}

	@Test
	@DisplayName("Ensures that open throws an exception if called when external and internal pressure difference is greater than tolerance.")
	void testOpenThrowsExceptionIfPressureDifferenceGreaterThanTolerance() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(15), DoorState.CLOSED);
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
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);

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
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);
			
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
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);
			
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
			Door door = new Door(new PressureSensor(10), new PressureSensor(12), DoorState.CLOSED);
			assertEquals(10, door.getExternalPressure());

		} catch (DoorException | PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures that getInternalPressure returns the correct pressure value initalised in the constructor.")
	void testGetInternalPressure() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(12), DoorState.CLOSED);
			assertEquals(12, door.getInternalPressure());

		} catch (DoorException | PressureException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures isOpen returns true when called on an open door.")
	void testIsOpenOnOpen() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);
			assertTrue(door.isOpen());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	
	}

	@Test
	@DisplayName("Ensures isOpen returns false when called on a closed door.")
	void testIsOpenOnClosed() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);
			assertFalse(door.isOpen());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	
	}

	@Test
	@DisplayName("Ensures isClosed returns false when called on an open door.")
	void testIsClosedOnOpen() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);
			assertFalse(door.isClosed());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Ensures isClosed returns true when called on a closed door.")
	void testIsClosedOnClosed() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);
			assertTrue(door.isClosed());

		} catch (PressureException | DoorException e) {
			fail(e);
		}
	}
}
