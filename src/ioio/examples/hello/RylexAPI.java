package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.exception.ConnectionLostException;
import android.annotation.SuppressLint;
import android.os.SystemClock;

@SuppressLint("NewApi")
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
	public double degreesLeftX = 2.1;
	public double centimetersX = 13.7;
	private SensorMonitor sensorMonitor;

	public RylexAPI(MainActivity m, Looper l, UltraSonicSensor sonar, SensorMonitor sensorMonitor, boolean hazFenderz)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		this.sensorMonitor = sensorMonitor;
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
		log("--- STARTING TEST ---");
		log("Reading Sensors");
		sonar.read();
		log("Left Sensor: " + sonar.getLeftDistance());
		log("Front Sensor: " + sonar.getFrontDistance());
		log("IR: " + getIR());
		log("goForward at defaultSpeed for 10 cm");
		goForward(defaultSpeed, 10);
		log("spinRight at defaultSpeed for 90�");
		spinRight(defaultSpeed, 90);
		SystemClock.sleep(500);
		log("spinLeft at defaultSpeed for 90�");
		spinLeft(defaultSpeed, 90);
		log("goBackward at defaultSpeed for 10 cm");
		goBackward(defaultSpeed, 10);
		log("--- TEST COMPLETED ---");
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

	public void spinRight(int speed, double degrees) throws Exception
	{
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftForward);
		double pulses = degrees * degreesRightX;// (20.0/9.0);
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void spinRightAzi(int speed, double degrees) throws Exception
	{
		log("initial azimuth = " + m.azimuth);
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftForward);
		double goal = m.azimuth + degrees;
		double prevAzi = 1;
		double fix = 0;
		double azi = m.azimuth + fix;
		while (azi < goal)
		{
			prevAzi = azi - fix;
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
			if (prevAzi * m.azimuth <= 0 && m.azimuth < 0)
			{
				fix = 360;
			}
			azi = m.azimuth + fix;
		}
		log("final azimuth = " + m.azimuth);
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

	// public void spinLeft(int speed, double degrees) throws Exception
	// {
	// l.rightMotorDirection.write(rightBackward);
	// l.leftMotorDirection.write(leftForward);
	// double goal = m.azimuth - degrees;
	// if (goal < -180)
	// {
	// goal += 360;
	// }
	// double fix;
	// if (goal + degrees > 180)
	// {
	// fix = 360;
	// } else
	// {
	// fix = 0;
	// }
	// double azi = m.azimuth + fix;
	// log("current angle: " + azi);
	// log("goal: " + goal);
	// while (azi < goal)
	// {
	// log("azi = " + azi);
	// azi = m.azimuth + fix;
	// SystemClock.sleep(1000 / speed);
	// l.rightMotorClock.write(true);
	// l.rightMotorClock.write(false);
	// l.leftMotorClock.write(true);
	// l.leftMotorClock.write(false);
	// }
	// }
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
		SystemClock.sleep(1000 / speed);
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

	public void log(String msg)
	{
		m.log(msg);
	}

	public void testUltrasonic() throws Exception
	{
		sonar.read();
		log("left = " + sonar.getLeftDistance());
		log("front = " + sonar.getFrontDistance());
		log("right = " + sonar.getRightDistance());
		log("rear = " + sonar.getRearDistance());
		SystemClock.sleep(1000);
	}

	public void victoryDance() throws Exception
	{
		spinRight(defaultSpeed, 360);
	}

	public double getIR()
	{
		return sensorMonitor.getFrontIRPulseDuration();
	}

	public void sleep(long nanos)
	{
		long initialTime = System.nanoTime();
		while (System.nanoTime() - initialTime < nanos)
		{
		}
	}

	public void spinToAzi(int speed, double goalAzi) throws Exception
	{
		double prevAzi = 1;
		double fix = 0;
		double azi = m.azimuth + fix;
		if (goalAzi > azi)
		{
			l.rightMotorDirection.write(rightBackward);
			l.leftMotorDirection.write(leftForward);
			while (azi < goalAzi)
			{
				prevAzi = azi - fix;
				SystemClock.sleep(1000 / speed);
				l.rightMotorClock.write(true);
				l.rightMotorClock.write(false);
				l.leftMotorClock.write(true);
				l.leftMotorClock.write(false);
				if (prevAzi * m.azimuth <= 0 && m.azimuth < 0)
				{
					fix += 360;
				} else if (prevAzi * m.azimuth <= 0 && m.azimuth > 0)
				{
					fix -= 360;
				}
				azi = m.azimuth + fix;
			}
		}
		if (goalAzi < azi)
		{
			l.rightMotorDirection.write(rightForward);
			l.leftMotorDirection.write(leftBackward);
			while (azi > goalAzi)
			{
				prevAzi = azi - fix;
				SystemClock.sleep(1000 / speed);
				l.rightMotorClock.write(true);
				l.rightMotorClock.write(false);
				l.leftMotorClock.write(true);
				l.leftMotorClock.write(false);
				if (prevAzi * m.azimuth <= 0 && m.azimuth > 0)
				{
					fix -= 360;
				} else if (prevAzi * m.azimuth <= 0 && m.azimuth < 0)
				{
					fix += 360;
				}
				azi = m.azimuth + fix;
			}
		}
	}
}
