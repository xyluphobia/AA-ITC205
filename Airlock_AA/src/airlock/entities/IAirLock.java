package airlock.entities;

import airlock.exceptions.AirLockException;

public interface IAirLock {

	void openOuterDoor() throws AirLockException;
	void closeOuterDoor() throws AirLockException;

	void openInnerDoor() throws AirLockException;
	void closeInnerDoor() throws AirLockException;

	void equaliseWithCabinPressure() throws AirLockException;
	void equaliseWithEnvironmentPressure() throws AirLockException;
	
	void toggleOperationMode() throws AirLockException;
	
	boolean isOuterDoorClosed();
	boolean isOuterDoorOpen();
	boolean isInnerDoorClosed();
	boolean isInnerDoorOpen();
	
	boolean isSealed();
	boolean isUnsealed();
	
	boolean isInManualMode();
	boolean isInAutoMode();
}


