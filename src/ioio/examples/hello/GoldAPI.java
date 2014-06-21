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
	
	public GoldAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz, IOIO ioio)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		this.ioio = ioio;
		ra = new RylexAPI(m, l, sonar, hazFenderz);
	}
	
	public void spinScan() {
		// TODO: Implement spinScan()
	}
	
	public void checkIR() {
		// TODO: Implement checkIR()
	}
}
