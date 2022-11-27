package airlock.entities;

import airlock.exceptions.PressureException;

public class PressureSensor implements IPressureSensor {
	
	double pressure;
	
	public PressureSensor(double initialPressure) throws PressureException {
		if (initialPressure < 0.0) {
			throw new PressureException("Pressure reading is negative, this is not possible.");
		} else {
			setPressure(initialPressure);
		}
	}
	
	public double getPressure() {
		return pressure;
	}
	
	public void setPressure(double newPressure) throws PressureException {
		if (newPressure < 0.0) {
			throw new PressureException("Cannot set negative pressure");
		}
		this.pressure = newPressure;
	}

	public String toString() {
		return String.format(
			"PressureSensor: pressure: %3.1f bar", getPressure());
	}

}
