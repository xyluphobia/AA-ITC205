package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import airlock.entities.Door;
import airlock.entities.DoorState;
import airlock.entities.PressureSensor;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

class DoorTest {

	@Test
	void testInvalidConstructor() {
		assertThrows(DoorException.class,
				() -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.OPEN),
				"Expected Door constructor to throw DoorException, but no exception thrown");

	}
	
	@Test
	void testValidConstructor1() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN);

			assertEquals(10, door.getExternalPressure());
			assertEquals(10, door.getInternalPressure());

			assertTrue(door.isOpen());
			assertFalse(door.isClosed());

			door.close();
			assertFalse(door.isOpen());
			assertTrue(door.isClosed());

			door.open();
			assertTrue(door.isOpen());
			assertFalse(door.isClosed());
			assertEquals(10, door.getPressure());

			door.setPressure(10.001);
			assertEquals(10.001, door.getPressure());
			assertEquals("Door: state: OPEN, external pressure: 10.0 bar, internal pressure: 10.0 bar", door.toString());

			assertThrows(PressureException.class, () -> new Door(new PressureSensor(-2), new PressureSensor(0), DoorState.CLOSED),
			"Expected Constructor to throw a PressureException due to given sensor values being invalid, no exception given.");

			assertThrows(DoorException.class, () -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.OPEN),
			"Expected Constructor to throw a DoorException due to DoorState being OPEN while the difference in pressure is > tolerance, no exception given.");

		} catch (DoorException | PressureException e) {
			fail();
			throw new AirLockException(e);
		}
	}
	
	@Test
	void testValidConstructor2() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED);
			door.open();
		} catch (DoorException | PressureException e) {
			fail();
			throw new AirLockException(e);
		}
	}
	
	@Test
	void testInvalidOpen() {
		assertThrows(DoorException.class,
				() -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.OPEN).open(),
				"Expected Door open to throw DoorException, but no exception thrown");

	}
	
	@Test
	void testValidOpen() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);
			assertTrue(door.isOpen());
		} catch (DoorException | PressureException e) {
			fail();
			throw new AirLockException(e);
		} 
	}

	@Test
	void testIsOpen() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);
			assertEquals(true, door.isOpen());

			door.close();
			assertEquals(false, door.isOpen());

		} catch (PressureException | DoorException e) {
			fail();
			throw new AirLockException(e);
		}
	
	}

	@Test
	void testIsClosed() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);
			assertEquals(true, door.isClosed());

			door.open();
			assertEquals(false, door.isClosed());

		} catch (PressureException | DoorException e) {
			fail();
			throw new AirLockException(e);
		}
	}

	@Test
	void testGetInternalPressure() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(12), DoorState.CLOSED);
			assertEquals(12, door.getInternalPressure());

		} catch (DoorException | PressureException e) {
			fail();
			throw new AirLockException(e);
		}
	}

	@Test
	void testGetExternalPressure() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(12), DoorState.CLOSED);
			assertEquals(10, door.getExternalPressure());

		} catch (DoorException | PressureException e) {
			fail();
			throw new AirLockException(e);
		}
	}

	@Test
	void testClose() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.CLOSED);
			
			assertThrows(DoorException.class, () -> door.close(),
			"Expected Door close to throw DoorException for closing a closed door, but no exception is thrown.");

			door.open();
			assertTrue(door.isOpen());

			door.close();
			assertTrue(door.isClosed());

			// ensure that if no exception is thrown that the Door's state becomes CLOSED. the door is opened initially and checked
			// to ensure that the DoorException won't be thrown due to an already closed door, it is then closed and checked 
			// ensuring it is now closed.
			
		} catch (DoorException | PressureException e ) {
			fail();
			throw new AirLockException(e);
		}
	}

	@Test
	void testOpen() throws AirLockException {
		try {
			Door door = new Door(new PressureSensor(10), new PressureSensor(10.001), DoorState.OPEN);

			assertThrows(DoorException.class, () -> door.open(),
			"Expected Door open to throw DoorException for opening an open door, but no exception is thrown.");

			door.close();
			door.setPressure(12);
		
			assertThrows(DoorException.class, () -> door.open(),
			"Expected Door open to throw DoorException for opening when pressure difference > tolerance, but no exception is thrown.");

			door.setPressure(10.001);
			assertTrue(door.isClosed());

			door.open();
			assertTrue(door.isOpen());

		} catch (DoorException | PressureException e ) {
			fail();
			throw new AirLockException(e);
		}
	}

}
