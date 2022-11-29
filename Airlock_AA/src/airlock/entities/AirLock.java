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

	private DoorState doorState;

	Door door = new Door(exSensor, inSensor, doorState) throws DoorException;
	

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
				lockSensor = getPressure();
			}
			outerDoor.open();
			state = AirLockState.UNSEALED;
		} catch (PressureException | DoorException e) {
			throw new AirLockException(e);
		}
	}
		
	@Override
	public void closeOuterDoor() throws AirLockException {
		//TODO - implement method
	}
	
	@Override
	public void openInnerDoor() throws AirLockException {
		//TODO - implement method
	}
	
	@Override
	public void closeInnerDoor() throws AirLockException {
		//TODO - implement method
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
		//TODO - implement method
		return false;
	}

	@Override
	public boolean isInnerDoorClosed() {
		//TODO - implement method
		return false;
	}

	@Override
	public boolean isInManualMode() {
		return mode == OperationMode.MANUAL;
	}

	@Override
	public boolean isInAutoMode() {
		return mode == OperationMode.AUTO;
	}

	@Override
	public boolean isSealed() {
		return state == AirLockState.SEALED;
	}

	@Override
	public boolean isUnsealed() {
		return state == AirLockState.UNSEALED;
	}

	public String toString() {
		return String.format(
			"Airlock: state: %s, mode: %s", 
			state, mode);
	}
	

}
