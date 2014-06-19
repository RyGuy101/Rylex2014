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
	private int defaultSpeed = 250;
	public double degreesRightX = 2.22;
	public double degreesLeftX = 2.08;
	public double centimetersX = 13.7;

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
		m.log("Reading Sensors");
		sonar.read();
		m.log("Left Sensor: " + sonar.getLeftDistance());
		m.log("Front Sensor: " + sonar.getFrontDistance());
		m.log("goForward at defaultSpeed for 10 cm");
		goForward(defaultSpeed, 10);
		m.log("spinRight at defaultSpeed for 90¡");
		spinRight(defaultSpeed, 90);
		SystemClock.sleep(500);
		m.log("spinLeft at defaultSpeed for 90¡");
		spinLeft(defaultSpeed, 90);
		m.log("goBackward at defaultSpeed for 10 cm");
		goBackward(defaultSpeed, 10);
		m.log("--- TEST COMPLETED ---");
	}

	public void spinRight(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftForward);
		l.rightMotorClock.close();
		l.leftMotorClock.close();
	}

	public void spinLeft(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftBackward);
		l.rightMotorClock.close();
		l.leftMotorClock.close();
	}

	// public void spinRight(int speed, double degrees) throws Exception
	// {
	// l.rightMotorDirection.write(rightBackward);
	// l.leftMotorDirection.write(leftForward);
	// double pulses = degrees * degreesRightX;// (20.0/9.0);
	// for (int i = 0; i < pulses; i++)
	// {
	// SystemClock.sleep(1000 / speed);
	// l.rightMotorClock.write(true);
	// l.rightMotorClock.write(false);
	// l.leftMotorClock.write(true);
	// l.leftMotorClock.write(false);
	// }
	// }
	public void spinRight(int speed, double degrees) throws Exception
	{
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftForward);
		double goal = m.azimuth + degrees;
		if (goal > 180)
		{
			goal -= 360;
		}
		double fix;
		double azi = m.azimuth;
		while (m.azimuth < goal)
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
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftBackward);
		double pulses = degrees * degreesLeftX;// (20.0/9.0);
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
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftForward);
		SystemClock.sleep(1000 / speed);
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void goForward(int speed, int centimeters) throws ConnectionLostException
	{
		double pulses = centimeters * centimetersX;
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftForward);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void goBackward(int speed) throws ConnectionLostException
	{
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftBackward);
		// SystemClock.sleep(1000 / speed);
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void goBackward(int speed, int centimeters) throws ConnectionLostException
	{
		double pulses = centimeters * centimetersX;
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftBackward);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	void goStraight(double azimuth) throws ConnectionLostException
	{
		if (m.getDegrees() < (azimuth))
		{
			while (m.getDegrees() < (azimuth))
			{
				spinRight(defaultSpeed);
			}
		} else if (m.getDegrees() > (azimuth))
		{
			while (m.getDegrees() > (azimuth))
			{
				spinLeft(defaultSpeed);
			}
		} else
		{
			goForward(defaultSpeed);
		}
	}
}
