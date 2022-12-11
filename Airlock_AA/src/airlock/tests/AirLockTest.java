package airlock.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import airlock.entities.Door;
import airlock.entities.DoorState;
import airlock.entities.IDoor;
import airlock.entities.IPressureSensor;
import airlock.entities.AirLock;
import airlock.entities.PressureSensor;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

public class AirLockTest {

    @Test
    @DisplayName("Ensures Airlock initialises a valid constructor if all inputs are valid.")
    void testConstructorInitalisesValid() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertInstanceOf(AirLock.class, airlock);

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
	@DisplayName("Ensures a DoorException is thrown if airlock is initalised with null values for Door.")
	void testConstructorThrowsExceptionIfGivenNullDoorValues() throws AirLockException {
		assertThrows(DoorException.class, () -> new AirLock(null, null, new PressureSensor(10)),
		"Expected Constructor to throw a DoorException due to door inputs being null, no exception given.");
	}

    @Test
    @DisplayName("Ensures Airlock initialises in the sealed state if both doors are closed.")
    void testConstructorInitalisesStateSealed() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);
            
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures Airlock initialises in the unsealed state if a door is open.")
    void testConstructorInitalisesStateUnsealed() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertTrue(airlock.isUnsealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures Airlock initialises in manual mode.")
    void testConstructorInitalisesModeManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertTrue(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that the doorException caused by opening an already open door is encapsulated by an AirLockException and is thrown.")
    void testOpenOuterDoorThrowsExceptionIfAlreadyOpen() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertThrows(AirLockException.class, () -> airlock.openOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to open the outerDoor while it is already open. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in operation mode AUTO, attempting to open the OuterDoor while the innerDoor is open results in the closing of the innerDoor.")
    void testOpenOuterDoorAutoModeClosesInnerDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto.
            airlock.openInnerDoor(); // must be set after toggling operation mode as toggleOperationMode throws an error if the airlock is not sealed.
            airlock.openOuterDoor();
            assertTrue(airlock.isInnerDoorClosed());
           
        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, an attempt is made to equalise pressure when opening outer door.")
    void testOpenOuterDoorPressureEqualisationAttemptIsMadeEnviromentLess() throws AirLockException {
        try {
            IPressureSensor environmentSensor = new PressureSensor(10.0);
            IPressureSensor lockSensor = new PressureSensor(15.0);
            IPressureSensor cabinSensor = new PressureSensor(12.0);
            IDoor outerDoor = new Door(environmentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock( outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto
            airlock.openOuterDoor();

            assertEquals("PressureSensor: pressure: 10.0 bar", airlock.lockSensor.toString());

        } catch (DoorException | PressureException e) {

            fail(e);

        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, an attempt is made to equalise pressure when opening outer door.")
    void testOpenOuterDoorPressureEqualisationAttemptIsMadeEnviromentGreater() throws AirLockException {
        try {
            IPressureSensor environmentSensor = new PressureSensor(20.0);
            IPressureSensor lockSensor = new PressureSensor(15.0);
            IPressureSensor cabinSensor = new PressureSensor(12.0);
            IDoor outerDoor = new Door(environmentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock( outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto
            airlock.openOuterDoor();

            assertEquals("PressureSensor: pressure: 20.0 bar", airlock.lockSensor.toString());

        } catch (DoorException | PressureException e) {

            fail(e);

        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, if pressure is equalised and innerDoor is closed, the outerDoor is opened when openOuterDoor is called.")
    void testOpenOuterDoorAuto() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode();
            airlock.openOuterDoor();
            assertTrue(airlock.isOuterDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that openOuterDoor attempts to open the OuterDoor while in MANUAL mode.")
    void testOpenOuterDoorOpensDoorInManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openOuterDoor();
            assertTrue(airlock.isOuterDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in MANUAL mode, if pressure is equalised and innerDoor is closed, the outerDoor is opened when openOuterDoor is called.")
    void testOpenOuterDoorManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openOuterDoor();
            assertTrue(airlock.isOuterDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that after calling OpenOuterDoor on a valid door, the airlock state becomes UNSEALED.")
    void testOpenOuterDoorMakesAirlockUnsealed() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openOuterDoor();
            assertTrue(airlock.isUnsealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that the airlock remains sealed even when exceptions are thrown.")
    void testOpenOuterDoorAirlockSealedThroughException() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(1);
            IPressureSensor cabinSensor = new PressureSensor(20);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertThrows(AirLockException.class, () -> airlock.closeOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to close outer door while it is already closed. Exception not thrown.");
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that the doorException caused by opening an already open door is encapsulated by an AirLockException and is thrown.")
    void testOpenInnerDoorThrowsExceptionIfOpeningOpenDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertThrows(AirLockException.class, () -> airlock.openInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to open the innerDoor while it is already open. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in operation mode AUTO, attempting to open the innerDoor while the outerDoor is open results in the closing of the outerDoor.")
    void testOpenInnerDoorClosesInnerDoorInAuto() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // set mode to auto
            airlock.openInnerDoor(); // must be set after toggling operation mode as toggleOperationMode throws an error if the airlock is not sealed.
            assertTrue(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, an attempt is made to equalise pressure.")
    void testOpenInnerDoorPressureEqualisationAttemptIsMadeCabinGreater() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(15);
            IPressureSensor cabinSensor = new PressureSensor(20);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto
            airlock.openInnerDoor();
            assertEquals("PressureSensor: pressure: 20.0 bar", airlock.lockSensor.toString());


        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, an attempt is made to equalise pressure.")
    void testOpenInnerDoorPressureEqualisationAttemptIsMadeCabinLess() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(15);
            IPressureSensor cabinSensor = new PressureSensor(12);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto
            airlock.openInnerDoor();
            assertEquals("PressureSensor: pressure: 12.0 bar", airlock.lockSensor.toString());


        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in AUTO mode, if pressure is equalised and innerDoor is closed, the InnerDoor is opened when openInnerDoor is called.")
    void testOpenInnerDoorAuto() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode();
            airlock.openInnerDoor();
            assertTrue(airlock.isInnerDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that openInnerDoor attempts to open the InnerDoor while in MANUAL mode.")
    void testOpenInnerDoorOpensDoorInManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openInnerDoor();
            assertTrue(airlock.isInnerDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that while in MANUAL mode, if pressure is equalised and OuterDoor is closed, the InnerDoor is opened when openInnerDoor is called.")
    void testOpenInnerDoorManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openInnerDoor();
            assertTrue(airlock.isInnerDoorOpen());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that after calling OpenInnerDoor on a valid door, the airlock state becomes UNSEALED.")
    void testOpenInneroorMakesAirlockUnsealed() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openInnerDoor();
            assertTrue(airlock.isUnsealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that the airlock remains sealed even when exceptions are thrown.")
    void testCloseInnerDoorAirlockSealedThroughException() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(0);
            IPressureSensor cabinSensor = new PressureSensor(20);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto
            assertThrows(AirLockException.class, () -> airlock.closeInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to close the InnerDoor while the door is already closed. Exception not thrown.");
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that closeOuterDoor attempts to close outer door.")
    void testCloseOuterDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.closeOuterDoor();
            assertTrue(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures closeOuterDoor sets the airlock to sealed if inner door is also closed.")
    void testCloseOuterDoorSeals() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.closeOuterDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
    
    @Test
    @DisplayName("Ensures that the doorException caused by closing an already closed door is encapsulated by an AirLockException and is thrown.")
    void testCloseOuterDoorThrowsExceptionClosingAClosedDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertThrows(AirLockException.class, () -> airlock.closeOuterDoor(), 
            "Expected to throw an AirLockException due to attempting to close the outerDoor while it is already closed. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that closeInnerDoor attempts to close Inner door.")
    void testCloseInnerDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.closeInnerDoor();
            assertTrue(airlock.isInnerDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures closeInnerDoor sets the airlock to sealed if Outer door is also closed.")
    void testCloseInnerDoorSeals() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.closeInnerDoor();
            assertTrue(airlock.isSealed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
    
    @Test
    @DisplayName("Ensures that the doorException caused by closing an already closed door is encapsulated by an AirLockException and is thrown.")
    void testCloseInnerDoorThrowsExceptionClosingAClosedDoor() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);
            assertThrows(AirLockException.class, () -> airlock.closeInnerDoor(), 
            "Expected to throw an AirLockException due to attempting to close the InnerDoor while it is already closed. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that equaliseWithEnviromentPressure throws AirlockNotSealedException if it is called when airlock is not SEALED")
    void testEqualiseWithEnviromentPressureThrowsAirlockNotSealedException() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertThrows(AirLockException.class, () -> airlock.equaliseWithCabinPressure(), 
            "Expected to throw an AirlockException due to attempting to equalise pressure while the airlock is not sealed. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that, if valid, the lock pressure is set to the same as the exterior enviroment pressure.")
    void testEqualiseWithEnviromentPressure() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(1);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(4);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.equaliseWithEnvironmentPressure();
            assertEquals(1, airlock.lockSensor.getPressure());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that equaliseWithCabinPressure throws AirlockNotSealedException if it is called when airlock is not SEALED")
    void testEqualiseWithCabinPressureThrowsAirlockNotSealedException() throws AirLockException {
        try {
            AirLock airlock = new AirLock(new Door(new PressureSensor(10), new PressureSensor(10), DoorState.CLOSED), 
            new Door(new PressureSensor(10), new PressureSensor(10), DoorState.OPEN), new PressureSensor(10));

            assertThrows(AirLockException.class, () -> airlock.equaliseWithCabinPressure(), 
            "Expected to throw an AirlockException due to attempting to equalise pressure while the airlock is not sealed. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that, if valid, the lock pressure is set to the same as the interior cabin pressure.")
    void testEqualiseWithCabinPressure() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(1);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10.001);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.equaliseWithCabinPressure();
            assertEquals(10.001, lockSensor.getPressure());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure toggleOperationMode throws an airlockexception if it is called while the airlock is not SEALED.")
    void testToggleOperationModeThrowsException() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.openOuterDoor();
            assertThrows(AirLockException.class, () -> airlock.toggleOperationMode(), 
            "Expected to throw an AirlockException due to attempting to toggle operation mode while the airlock is not sealed. Exception not thrown.");

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensures that when valid, toggleOperationMode correctly changes the operation mode.")
    void testToggleOperationMode() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());

            airlock.toggleOperationMode();
            assertTrue(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that isManualMode returns True if the airlock is in MANUAL mode.")
    void testIsInManualModeWhenManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertTrue(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that isManualMode returns False if the airlock is in AUTO mode.")
    void testIsInManualModeWhenAuto() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode();
            assertFalse(airlock.isInManualMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that isAutoMode returns True if the airlock is in AUTO mode.")
    void testIsInAutoModeWhenManual() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertFalse(airlock.isInAutoMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure that isAutoMode returns False if the airlock is in MANUAL mode.")
    void testIsInAutoModeWhenAuto() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertFalse(airlock.isInAutoMode());

            airlock.toggleOperationMode();
            assertTrue(airlock.isInAutoMode());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure isOuterDoorClosed returns True if the outerDoor is closed.")
    void testIsOuterDoorClosedWhenTrue() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertTrue(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure isOuterDoorClosed returns False if the outerDoor is open.")
    void testIsOuterDoorClosedWhenFalse() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.OPEN);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertFalse(airlock.isOuterDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
    
    @Test
    @DisplayName("Ensure isInnerDoorClosed returns True if the innerDoor is closed.")
    void testIsInnerDoorClosedWhenTrue() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertTrue(airlock.isInnerDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Ensure isInnerDoorClosed returns False if the innerDoor is open.")
    void testIsInnerDoorClosedWhenFalse() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(10);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.OPEN);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            assertFalse(airlock.isInnerDoorClosed());

        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
}
