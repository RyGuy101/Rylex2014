package ioio.examples.hello;

import android.os.IInterface;
import android.os.SystemClock;
import android.util.Log;
import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

public class DragAPI
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
	private static final int MOTOR_HALF_FULL_STEP_PIN = 7;// For both motors
	private static final int MOTOR_RESET = 22;// For both motors
	// Acceleration Variables
	private boolean startedAcceleration = false;
	private int leftMotorPWMfrequency = 200;// used to be both 200;
	private int rightMotorPWMfrequency = 195;
	private IOIO ioio;
	private static final int MOTOR_CLOCK_LEFT_PIN = 27;
	private static final int MOTOR_CLOCK_RIGHT_PIN = 28;
	private int pulseWidth = 10;// microseconds
	private final static String LOGTAG = "IOIOLooper";
	private PwmOutput rightMotorClock;
	private PwmOutput leftMotorClock;
	RylexAPI ra;

	public DragAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz, IOIO ioio)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		this.ioio = ioio;
		rightForward = !hazFenderz;
		leftForward = !rightForward;
		rightBackward = !rightForward;
		leftBackward = !leftForward;
		ra = m.ra;
	}

	public void accelerateTo(int initialSpeed, int finalSpeed, double rate) throws Exception
	{
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftForward);
		double currentSpeed = initialSpeed;
		while (true)
		{
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
			ra.sleep((long) (1000000000 / currentSpeed));
			if (currentSpeed <= finalSpeed - rate)
			{
				currentSpeed += rate;
			}
		}
	}
}
