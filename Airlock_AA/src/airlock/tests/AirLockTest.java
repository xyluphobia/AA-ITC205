package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import airlock.entities.Door;
import airlock.entities.DoorState;
import airlock.entities.AirLock;
import airlock.entities.PressureSensor;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

public class AirLockTest {

    @Test
    void testCloseOuterDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.closeOuterDoor(), 
            "Expected to throw an AirlockException due to attempting to clsoe the outerDoor while it is already closed. Exception not thrown.");
            // If the exception is thrown and the assert is passed this proves that the closeOuterDoor() method attempts to close the outer door.

            airlock.openOuterDoor();
            assertFalse(airlock.isSealed());
            
            airlock.closeOuterDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            //fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testCloseInnerDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.closeInnerDoor(), 
            "Expected to throw an AirlockException due to attempting to clsoe the innerDoor while it is already closed. Exception not thrown.");
            // If the exception is thrown and the assert is passed this proves that the closeInnerDoor() method attempts to close the inner door.

            airlock.openInnerDoor();
            assertFalse(airlock.isSealed());
            
            airlock.closeInnerDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            //fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testEqualiseWithEnviromentPressure() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.equaliseWithCabinPressure(), 
            "Expected to throw an AirlockException due to attempting to equalise pressure while the airlock is not sealed. Exception not thrown.");

            AirLock airlock2 = new AirLock(new Door(new PressureSensor(1), new PressureSensor(2), DoorState.CLOSED), 
            new Door(new PressureSensor(3), new PressureSensor(4), DoorState.CLOSED), new PressureSensor(10));

            airlock2.equaliseWithEnvironmentPressure();
            assertEquals(1, airlock2.lockSensor.getPressure());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testEqualiseWithCabinPressure() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.equaliseWithCabinPressure(), 
            "Expected to throw an AirlockException due to attempting to equalise pressure while the airlock is not sealed. Exception not thrown.");

            AirLock airlock2 = new AirLock(new Door(new PressureSensor(1), new PressureSensor(2), DoorState.CLOSED), 
            new Door(new PressureSensor(3), new PressureSensor(4), DoorState.CLOSED), new PressureSensor(10));

            airlock2.equaliseWithCabinPressure();
            assertEquals(4.0, airlock2.lockSensor.getPressure());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testToggleOperationMode() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            airlock.openOuterDoor();
            assertThrows(AirLockException.class, () -> airlock.toggleOperationMode(), 
            "Expected to throw an AirlockException due to attempting to toggle operation mode while the airlock is not sealed. Exception not thrown.");

            airlock.closeOuterDoor();
            assertTrue(airlock.isInManualMode());

            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testIsInManualMode() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isInManualMode());

            airlock.toggleOperationMode();
            assertFalse(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testIsInAutoMode() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertFalse(airlock.isInAutoMode());

            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }

    @Test
    void testIsOuterDoorClosed() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isOuterDoorClosed());

            airlock.openOuterDoor();
            assertFalse(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }
    
    @Test
    void testIsInnerDoorClosed() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isInnerDoorClosed());

            airlock.openInnerDoor();
            assertFalse(airlock.isInnerDoorClosed());

        } catch (DoorException | PressureException e) {
            fail();
            throw new AirLockException(e);
        }
    }
}
