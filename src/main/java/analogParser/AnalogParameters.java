package analogParser;

public interface AnalogParameters {
	
	public enum Parameters {
		
		ISE30A(0.6, 1.0, 1000.0);
		
		Parameters(double voltageLow, double voltageHigh, double pressureRange) {
			this.voltageLow = voltageLow;
			this.voltageHigh = voltageHigh;
			this.pressureRange = pressureRange;
		}
		
		final double voltageLow;
		final double voltageHigh;
		final double pressureRange;
	}
	
	public double getVoltageLow();
	
	public double getVoltageHigh();
	
	public double getPressureRange();
	
	public void setVoltageLow(double voltage);
	
	public void setVoltageHigh(double voltage);
	
	public void setPressureRange(double range);
}
