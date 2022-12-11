package airlock.entities;

import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

public class Door implements IDoor, IPressureSensor{
	
	public static double TOLERANCE = 0.001;
	
	IPressureSensor inSensor;
	IPressureSensor exSensor;
	
	private DoorState state;
	
	public Door(IPressureSensor exSensor, IPressureSensor inSensor, 
	            DoorState initialState) throws DoorException {
		this.inSensor = inSensor;
		this.exSensor = exSensor;
		this.state = initialState;
		if (this.inSensor != null && this.exSensor != null) {
			if (initialState == DoorState.OPEN && (Math.abs(inSensor.getPressure() - exSensor.getPressure()) > TOLERANCE)) {
				throw new DoorException("Door initial state cannot be open with difference in pressure is greater than the tolerance");
			} 
		} else {
			throw new DoorException("Sensors are non-valid instances of IPressureSensor.");
		}

	}
	
	@Override
	public void open() throws DoorException {
		if (state == DoorState.OPEN) {
			throw new DoorException("Door is already open");
		} else {
			if (Math.abs(inSensor.getPressure() - exSensor.getPressure()) > TOLERANCE) {
				throw new DoorException("Difference in pressure is greater than the tolerance");
			}
			state = DoorState.OPEN;
		}
	}
	
	@Override
	public void close() throws DoorException {
		if (state == DoorState.CLOSED) {
			throw new DoorException("Door is already closed");
		}
		state = DoorState.CLOSED;
	}

	@Override
	public double getExternalPressure() {
		return exSensor.getPressure();
	}

	@Override
	public double getInternalPressure() {
		return inSensor.getPressure();
	}

	@Override
	public boolean isOpen() {
		if (state == DoorState.OPEN) return true;
		else return false;
	}

	@Override
	public boolean isClosed() {
		if (state == DoorState.CLOSED) return true;
		else return false;
	}

	public String toString() {
		return String.format(
			"Door: state: %s, external pressure: %3.1f bar, internal pressure: %3.1f bar", 
			state, exSensor.getPressure(), inSensor.getPressure());
	}

	@Override
	public double getPressure() {
		return exSensor.getPressure();
	}

	@Override
	public void setPressure(double newPressure) throws PressureException {
		inSensor.setPressure(newPressure);
	}	

}
