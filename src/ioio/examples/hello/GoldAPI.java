package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.IOIO;

public class GoldAPI
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;
	private IOIO ioio;
	RylexAPI ra;
	private SensorMonitor sensorMonitor = null;
	
	public GoldAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz, IOIO ioio)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		this.ioio = ioio;
		ra = new RylexAPI(m, l, sonar, hazFenderz);
	}
	
	public void spinScan() throws Exception {
		ra.spinLeft(500, 360);
		// TODO: Implement spinScan()
	}
	
	public void checkIR() {
		// TODO: Implement checkIR()
	}
	
	public double getIR() {
		return sensorMonitor.getFrontIRPulseDuration();
	}
}
