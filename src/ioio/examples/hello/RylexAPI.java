package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;

public class RylexAPI
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;

	public RylexAPI(MainActivity m, Looper l, UltraSonicSensor sonar)
	{
		// m.log("In RylexAPI constructor");
		this.m = m;
		// m.log("Set MainActivity field 'm' equal to paramater 'm'");
		this.l = l;
		// m.log("Set Looper field 'l' equal to paramater 'l'");
		this.sonar = sonar;
		// m.log("Set UltraSonicSensor field 'sonar' equal to paramater 'sonar'");
		// m.log("Done with RylexAPI constructor");
	}

	public void turnRight() throws Exception
	{
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void turnLeft() throws Exception
	{
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(false);
	}

	public void goForward() throws Exception
	{
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void hugRightDistance(int distance) throws Exception
	{
		sonar.read();
		if (sonar.getRightDistance() < distance)
		{
			turnLeft();
		} else if (sonar.getRightDistance() > distance)
		{
			turnRight();
		} else
		{
			goForward();
		}
	}

	public void hugLeftDistance(int distance) throws Exception
	{
		sonar.read();
		if (sonar.getLeftDistance() < distance)
		{
			turnRight();
		} else if (sonar.getLeftDistance() > distance)
		{
			turnLeft();
		} else
		{
			goForward();
		}
	}
}
