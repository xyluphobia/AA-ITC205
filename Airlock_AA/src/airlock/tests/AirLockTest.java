package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("")
    void testConstructor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isUnsealed());
            assertTrue(airlock.isInManualMode());

            AirLock airlock2 = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));
            assertTrue(airlock2.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testOpenOuterDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.openOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to open the outerDoor while it is already open. Exception not thrown.");
            // Ensures that the doorException caused by opening an already open door is encapsulated by an AirLockException and is thrown.

            airlock.closeOuterDoor();
            assertTrue(airlock.isOuterDoorClosed());
            assertTrue(airlock.isInManualMode());
            airlock.openOuterDoor();
            assertTrue(airlock.isOuterDoorOpen());
            // Ensures that openOuterDoor attempts to open the OuterDoor while in MANUAL mode.

            airlock.closeOuterDoor();
            assertTrue(airlock.isSealed());
            airlock.openOuterDoor();
            assertTrue(airlock.isUnsealed());
            // Ensures that airlock state becomes UNSEALED after opening OuterDoor.

            airlock.closeOuterDoor();
            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());
            airlock.openInnerDoor();
            assertTrue(airlock.isInnerDoorOpen());
            airlock.openOuterDoor();
            assertTrue(airlock.isInnerDoorClosed());
            // Ensures that while in operation mode AUTO, attempting to open the OuterDoor while the innerDoor is open results in the closing of the innerDoor.

            AirLock airlock2 = new AirLock(new Door(new PressureSensor(3), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(15));
            assertTrue(airlock2.isSealed());
            airlock2.toggleOperationMode();
            assertTrue(airlock2.isInAutoMode());
            assertThrows(AirLockException.class, () -> airlock2.openOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to open the outerDoor while the pressure difference is > tolerance. Exception not thrown.");
            assertTrue(airlock2.isSealed());
            // Ensures that the airlock remains sealed even when exceptions are thrown, also ensures that while in AUTO mode, an attempt is made to equalise pressure
            // after calling openOuterDoor.

            AirLock airlock3 = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));
            airlock3.toggleOperationMode();
            assertTrue(airlock3.isInAutoMode());
            airlock3.openOuterDoor();
            assertTrue(airlock3.isOuterDoorOpen());
            // Ensures that while in AUTO mode, if pressure is equalised and innerDoor is closed, the outerDoor is opened when openOuterDoor is called.

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testOpenInnerDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.openInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to open the innerDoor while it is already open. Exception not thrown.");
            // Ensures that the doorException caused by opening an already open door is encapsulated by an AirLockException and is thrown.

            airlock.closeInnerDoor();
            assertTrue(airlock.isInnerDoorClosed());
            assertTrue(airlock.isInManualMode());
            airlock.openInnerDoor();
            assertTrue(airlock.isInnerDoorOpen());
            // Ensures that openInnerDoor attempts to open the innerDoor while in MANUAL mode.

            airlock.closeInnerDoor();
            assertTrue(airlock.isSealed());
            airlock.openInnerDoor();
            assertTrue(airlock.isUnsealed());
            // Ensures that airlock state becomes UNSEALED after opening innerDoor.

            airlock.closeInnerDoor();
            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());
            airlock.openOuterDoor();
            assertTrue(airlock.isOuterDoorOpen());
            airlock.openInnerDoor();
            assertTrue(airlock.isOuterDoorClosed());
            // Ensures that while in operation mode AUTO, attempting to open the innerDoor while the outerDoor is open results in the closing of the outerDoor.

            AirLock airlock2 = new AirLock(new Door(new PressureSensor(12), new PressureSensor(12), DoorState.CLOSED), 
            new Door(new PressureSensor(12), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));
            assertTrue(airlock2.isSealed());
            airlock2.toggleOperationMode();
            assertTrue(airlock2.isInAutoMode());
            assertThrows(AirLockException.class, () -> airlock2.openInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to open the innerDoor while the pressure difference is > tolerance. Exception not thrown.");
            assertTrue(airlock2.isSealed());
            // Ensures that the airlock remains sealed even when exceptions are thrown, also ensures that while in AUTO mode, an attempt is made to equalise pressure
            // after calling openInnerDoor.

            AirLock airlock3 = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));
            airlock3.toggleOperationMode();
            assertTrue(airlock3.isInAutoMode());
            airlock3.openInnerDoor();
            assertTrue(airlock3.isInnerDoorOpen());
            // Ensures that while in AUTO mode, if pressure is equalised and outerDoor is closed, the innerDoor is opened when openInnerDoor is called.

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testCloseOuterDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.closeOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to close the outerDoor while it is already closed. Exception not thrown.");
            // If the exception is thrown and the assert is passed this proves that the closeOuterDoor() method attempts to close the outer door.
            // This also ensures that the doorException caused by closing an already closed door is encapsulated by an AirLockException and is thrown.

            airlock.openOuterDoor();
            assertThrows(AirLockException.class, () -> airlock.openOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to open the outerDoor while it is already open. Exception not thrown.");
            // Ensures that the doorException caused by opening an already open door is encapsulated by an AirLockException and is thrown.

            assertFalse(airlock.isSealed());
            
            airlock.closeOuterDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testCloseInnerDoor() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.closeInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to close the innerDoor while it is already closed. Exception not thrown.");
            // If the exception is thrown and the assert is passed this proves that the closeInnerDoor() method attempts to close the inner door.
            // This also ensures that the doorException caused by closing an already closed door is encapsulated by an AirLockException and is thrown.

            airlock.openInnerDoor();
            assertFalse(airlock.isSealed());
            
            airlock.closeInnerDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
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
            fail(e);
        }
    }

    @Test
    @DisplayName("")
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
            fail(e);
        }
    }

    @Test
    @DisplayName("")
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
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testIsInManualMode() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isInManualMode());

            airlock.toggleOperationMode();
            assertFalse(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testIsInAutoMode() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertFalse(airlock.isInAutoMode());

            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("")
    void testIsOuterDoorClosed() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isOuterDoorClosed());

            airlock.openOuterDoor();
            assertFalse(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
    
    @Test
    @DisplayName("")
    void testIsInnerDoorClosed() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), new PressureSensor(10));

            assertTrue(airlock.isInnerDoorClosed());

            airlock.openInnerDoor();
            assertFalse(airlock.isInnerDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
}
