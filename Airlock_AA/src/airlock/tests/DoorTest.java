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
	void testInvalidClose() {
		assertThrows(DoorException.class,
				() -> new Door(new PressureSensor(10), new PressureSensor(12), DoorState.CLOSED).close(),
				"Expected Door close to throw DoorException, but no exception thrown");

	}

}
