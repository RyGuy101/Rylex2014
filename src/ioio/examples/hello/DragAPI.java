package ioio.examples.hello;

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
	}

	public void accelerateTo(final int finalPWMfrequency) throws Exception
	{
		l.rightMotorDirection.write(rightForward);
		l.leftMotorDirection.write(leftForward);
		l.rightMotorClock.close();
		l.leftMotorClock.close();
		new Thread(new Runnable()
		{
			public void run()
			{
				while (leftMotorPWMfrequency < finalPWMfrequency)
				{
					try
					{
						SystemClock.sleep(1000 / leftMotorPWMfrequency);
						m.log("Setting Motor frequency : " + leftMotorPWMfrequency);
						l.rightMotorClock = (DigitalOutput) ioio.openPwmOutput(MOTOR_CLOCK_RIGHT_PIN, rightMotorPWMfrequency);// pin
						// #,
						// frequency
						// right
						// motor//
						// pin
						// 10
						// on
						// original
						// wagon...right
						((PwmOutput) l.rightMotorClock).setPulseWidth(pulseWidth);
						l.leftMotorClock = (DigitalOutput) ioio.openPwmOutput(MOTOR_CLOCK_LEFT_PIN, leftMotorPWMfrequency);// pin
						// #,
						// frequency
						// right
						// motor//
						// pin
						// 10
						// on
						// original
						// wagon...right
						((PwmOutput) l.leftMotorClock).setPulseWidth(pulseWidth);
						leftMotorPWMfrequency++;
						rightMotorPWMfrequency++;
					} catch (Exception ex)
					{
						m.log("Motor clock pulsing hiccup");
					}
				}
			}
		}).start();
	}
}
