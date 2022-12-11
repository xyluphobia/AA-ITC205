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

public class FATTests {
    
    @Test
    @DisplayName("Pass through airlock in auto mode from inside to outside when external enviroment pressure is less than internal cabin pressure.")
    void TestAirlockAutoInToOutExternalLessThanInternal() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(12);
            IPressureSensor cabinSensor = new PressureSensor(13);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto.
            
            airlock.openInnerDoor();
            airlock.openOuterDoor();  // test steps as outlined in the master test plan docx.
            airlock.closeOuterDoor();

            assertTrue(airlock.isSealed()); // tests checks outlined in master test plan  docx, airlock should be SEALED.
            assertEquals(13.0, airlock.innerDoor.getExternalPressure()); // cabin pressure should be unchanged.
            assertEquals("PressureSensor: pressure: 13.0 bar", airlock.lockSensor.toString()); // airlock pressure should be the same as cabin pressure.
            
        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Pass through airlock in auto mode from outside to inside when external enviroment pressure is more than internal cabin pressure.")
    void TestAirlockAutoOutToInExternalMoreThanInternal() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(12);
            IPressureSensor lockSensor = new PressureSensor(11);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.toggleOperationMode(); // setting mode to auto.
            
            airlock.openOuterDoor();
            airlock.openInnerDoor();  // test steps as outlined in the master test plan docx.
            airlock.closeInnerDoor();

            assertTrue(airlock.isSealed()); // tests checks outlined in master test plan  docx, airlock should be SEALED.
            assertEquals(10.0, airlock.innerDoor.getExternalPressure()); // cabin pressure should be unchanged.
            assertEquals("PressureSensor: pressure: 10.0 bar", airlock.lockSensor.toString()); // airlock pressure should be the same as cabin pressure.
            
        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

        @Test
    @DisplayName("Pass through airlock in manual mode from inside to outside when external enviroment pressure is more than internal cabin pressure.")
    void TestAirlockManualInToOutExternalMoreThanInternal() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(12);
            IPressureSensor lockSensor = new PressureSensor(11);
            IPressureSensor cabinSensor = new PressureSensor(10);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);

            airlock.equaliseWithCabinPressure();
            airlock.openInnerDoor();
            airlock.closeInnerDoor();
            airlock.equaliseWithEnvironmentPressure();  // test steps as outlined in the master test plan docx.
            airlock.openOuterDoor();
            airlock.closeOuterDoor();

            assertTrue(airlock.isSealed()); // tests checks outlined in master test plan  docx, airlock should be SEALED.
            assertEquals(10.0, airlock.innerDoor.getExternalPressure()); // cabin pressure should be unchanged.
            assertEquals("PressureSensor: pressure: 10.0 bar", airlock.lockSensor.toString()); // airlock pressure should be the same as cabin pressure.
            
        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Pass through airlock in manual mode from outside to inside when external enviroment pressure is less than internal cabin pressure.")
    void TestAirlockManualOutToInExternalLesshanInternal() throws AirLockException {
        try {
            IPressureSensor enviromentSensor = new PressureSensor(10);
            IPressureSensor lockSensor = new PressureSensor(11);
            IPressureSensor cabinSensor = new PressureSensor(12);
            IDoor outerDoor = new Door(enviromentSensor, lockSensor, DoorState.CLOSED);
            IDoor innerDoor = new Door(cabinSensor, lockSensor, DoorState.CLOSED);

            AirLock airlock = new AirLock(outerDoor, innerDoor, lockSensor);
            
            airlock.equaliseWithEnvironmentPressure();
            airlock.openOuterDoor();
            airlock.closeOuterDoor();
            airlock.equaliseWithCabinPressure();  // test steps as outlined in the master test plan docx.
            airlock.openInnerDoor();
            airlock.closeInnerDoor();

            assertTrue(airlock.isSealed()); // tests checks outlined in master test plan  docx, airlock should be SEALED.
            assertEquals(12.0, airlock.innerDoor.getExternalPressure()); // cabin pressure should be unchanged.
            assertEquals("PressureSensor: pressure: 12.0 bar", airlock.lockSensor.toString()); // airlock pressure should be the same as cabin pressure.
            
        } catch (DoorException | PressureException e) {
            fail(e);
        }
    }
}
