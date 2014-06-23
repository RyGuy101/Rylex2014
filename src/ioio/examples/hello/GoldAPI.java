package ioio.examples.hello;

import android.os.SystemClock;
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
	
	public void spinScan(int speed) throws Exception {
		l.rightMotorDirection.write(ra.rightForward);
		l.leftMotorDirection.write(ra.leftBackward);
		double pulses = 360 * ra.degreesLeftX;// (20.0/9.0);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
			ra.getIR();
			if(ra.getIR() < 1500) {
				m.log("");
				m.log("SEES IR");
				m.log("");
				break;
			}
		}
	}
}
