package airlock.entities;

import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

public class AirLock implements IAirLock{
	
	private IDoor outerDoor;
	private IDoor innerDoor;
	private IPressureSensor lockSensor;
	
	private AirLockState state;	
	private OperationMode mode;

	IPressureSensor inSensor;
	IPressureSensor exSensor;

	public AirLock(IDoor outerDoor, IDoor innerDoor, IPressureSensor lockSensor) {
		this.outerDoor = outerDoor;
		this.innerDoor = innerDoor;
		this.lockSensor = lockSensor;
		mode = OperationMode.MANUAL;
		if (outerDoor.isClosed() && innerDoor.isClosed()) {
			state = AirLockState.SEALED;
		} else {
			state = AirLockState.UNSEALED;
		}
	}
		
	@Override
	public void openOuterDoor() throws AirLockException {
		if (outerDoor.isOpen()) throw new AirLockException("Door is already open.");
		try {
			if (mode == OperationMode.AUTO) {
				if (innerDoor.isOpen()) innerDoor.close();
				lockSensor.setPressure(outerDoor.getExternalPressure());
			}
			outerDoor.open();
			state = AirLockState.UNSEALED;
		} catch (PressureException | DoorException e) {
			throw new AirLockException(e);
		}
	}
		
	@Override
	public void closeOuterDoor() throws AirLockException {
		try {
			outerDoor.close();
			if (innerDoor.isClosed()) state = AirLockState.SEALED;
		} catch (DoorException e) {
			throw new AirLockException(e);
		}
	}
	
	@Override
	public void openInnerDoor() throws AirLockException {
		if (innerDoor.isOpen()) throw new AirLockException("Door is already open.");
		try {
			if (mode == OperationMode.AUTO) {
				if (outerDoor.isOpen()) outerDoor.close();
				lockSensor.setPressure(innerDoor.getInternalPressure());
			}
			innerDoor.open();
			state = AirLockState.UNSEALED;
		} catch (DoorException | PressureException e) {
			throw new AirLockException(e);
		}
	}
	
	@Override
	public void closeInnerDoor() throws AirLockException {
		try {
			innerDoor.close();
			if (outerDoor.isClosed()) state = AirLockState.SEALED;
		} catch (DoorException e) {
			throw new AirLockException(e);
		}
	}
	
	@Override
	public void equaliseWithCabinPressure() throws AirLockException {
		//TODO - implement method
	}

	@Override
	public void equaliseWithEnvironmentPressure()  throws AirLockException {
		//TODO - implement method
	}

	@Override
	public void toggleOperationMode() throws AirLockException{
		//TODO - implement method
	}
	
	@Override
	public boolean isOuterDoorClosed() {
		return outerDoor.isClosed();
	}

	@Override
	public boolean isOuterDoorOpen() {
		return outerDoor.isOpen();
	}

	@Override
	public boolean isInnerDoorClosed() {
		return innerDoor.isClosed();
	}

	@Override
	public boolean isInnerDoorOpen() {
		return innerDoor.isClosed();
	}

	@Override
	public boolean isInManualMode() {
		if (mode == OperationMode.MANUAL) return true;
		return false;
	}

	@Override
	public boolean isInAutoMode() {
		if (mode == OperationMode.AUTO) return true;
		return false;
	}

	@Override
	public boolean isSealed() {
		if (state == AirLockState.SEALED) return true;
		return false;
	}

	@Override
	public boolean isUnsealed() {
		if (state == AirLockState.UNSEALED) return true;
		return false;
	}

	public String toString() {
		return String.format(
			"Airlock: state: %s, mode: %s", 
			state, mode);
	}
	

}
