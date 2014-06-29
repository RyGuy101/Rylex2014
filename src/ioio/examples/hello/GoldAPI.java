package ioio.examples.hello;

import java.util.ArrayList;
import android.os.SystemClock;
import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

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
		ra = m.ra;
	}

	public void goldRush() throws Exception
	{
		sonar.read();
		ra.goForward(200, sonar.getFrontDistance() / 2);
		spinScan(200);
	}

	public void spinScan(int speed) throws Exception
	{
		l.rightMotorDirection.write(ra.rightForward);
		l.leftMotorDirection.write(ra.leftBackward);
		double pulses = 360 * ra.degreesLeftX;// (20.0/9.0);
		for (int i = 0; i < pulses; i++)
		{
			ra.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
			if (readIR() > 0)
			{
				m.log("");
				m.log("SEES IR");
				m.log("");
				goldRush();
			}
		}
		goldRush();
	}

	public float readIR() throws Exception
	{
		float r = 0;
		float t = 0;
		ArrayList<Float> readings = new ArrayList<Float>();
		for (int i = 0; i < 50; i++)
		{
			r = m.readIR();
			if (r != 1)
			{
				readings.add(r);
			}
		}
		for (int i = 0; i < readings.size(); i++)
		{
			t += readings.get(i);
		}
		return t;
	}
}
