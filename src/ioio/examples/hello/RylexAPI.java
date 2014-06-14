package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.exception.ConnectionLostException;
import android.os.SystemClock;

public class RylexAPI
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;
	public boolean rightForward;
	public boolean leftForward;
	public boolean rightBackward;
	public boolean leftBackward;
	private int counter = 0;

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

	public void test() throws Exception
	{
		m.log("--- STARTING TEST ---");
		sonar.read();
		m.log("Left Sensor: " + sonar.getLeftDistance());
		m.log("Front Sensor: " + sonar.getFrontDistance());
		goForward(100);
		// goBackward(100); TODO: Add goBackward Method
		m.log("--- TEST COMPLETED ---");
	}

	public void spinRight(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(true);
		l.leftMotorDirection.write(true);
		l.rightMotorClock.close();
		l.leftMotorClock.close();
	}

	public void spinLeft(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(false);
		l.leftMotorDirection.write(false);
		l.rightMotorClock.close();
		l.leftMotorClock.close();
	}

	public void spinRight(int speed, double degrees) throws Exception
	{
		l.rightMotorDirection.write(false);
		l.leftMotorDirection.write(false);
		double pulses = degrees * 2.54;// (20.0/9.0);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void spinLeft(int speed, double degrees) throws Exception
	{
		l.rightMotorDirection.write(true);
		l.leftMotorDirection.write(true);
		double pulses = degrees * 2.46;// (20.0/9.0);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
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

	public void goForward(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(false);
		l.leftMotorDirection.write(true);
		SystemClock.sleep(1000 / speed);
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void goForward(int speed, int centimeters) throws ConnectionLostException
	{
		double pulses = centimeters * 10.95;
		l.rightMotorDirection.write(false);
		l.leftMotorDirection.write(true);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	
}
