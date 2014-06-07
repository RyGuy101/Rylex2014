package ioio.examples.hello;

import android.os.SystemClock;
import ioio.examples.hello.MainActivity.Looper;

public class RylexAPI
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;
	public boolean rightForward;
	public boolean leftForward;
	public boolean rightBackward;
	public boolean leftBackward;

	public RylexAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		rightForward = !hazFenderz;
		leftForward = !rightForward;
		rightBackward = !rightForward;
		leftBackward = !leftForward;
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
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftForward);
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}
	
	public void goBackward() throws Exception
	{
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftBackward);
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
	
	public void test() throws Exception {
		m.log("--- STARTING TEST ---");
		sonar.read();
		m.log("Left Sensor: " + sonar.getLeftDistance());
		m.log("Front Sensor: " + sonar.getFrontDistance());
		for (int i = 0; i < 15; i++)
		{
			goForward();
		}
		for (int i = 0; i < 15; i++)
		{
			goBackward();
		}
		m.log("--- TEST COMPLETED ---");
	}
	
	public void spinRight() {
		
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
