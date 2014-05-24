package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application. It displays a
 * toggle button on the screen, which enables control of the on-board LED.
 * Modified by Vic rev 130402A
 */
public class MainActivity extends IOIOActivity implements SensorEventListener,
		RylexAPI
{
	private ToggleButton button_;
	private int targetDirection = 90;
	private int difference;
	private UltraSonicSensor sonar;
	private TextView mText;
	private ScrollView mScroller;
	/**
	 * Compass stuff
	 */
	SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;
	private float[] valuesAccelerometer;
	private float[] valuesMagneticField;
	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;
	private double azimuth;
	private int lastAzimuth;
	private double pitch;
	private double roll;

	/**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button_ = (ToggleButton) findViewById(R.id.button);
		mScroller = (ScrollView) findViewById(R.id.scroller);
		mText = (TextView) findViewById(R.id.logText);

		// Compass stuff
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];

		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];
	}

	@Override
	public void onPause()
	{
		sensorManager.unregisterListener(this, sensorAccelerometer);
		sensorManager.unregisterListener(this, sensorMagneticField);
		super.onPause();
	}

	@Override
	protected void onResume()
	{

		sensorManager.registerListener(this, sensorAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run
	 * every time the application is resumed and aborted when it is paused. The
	 * method setup() will be called right after a connection with the IOIO has
	 * been established (which might happen several times!). Then, loop() will
	 * be called repetitively until the IOIO gets disconnected
	 */
	class Looper extends BaseIOIOLooper
	{
		private DigitalOutput led_;// The on-board LED
		private DigitalOutput motorEnable; // Both motors
		private DigitalOutput rightMotorClock; // Step right motor
		private DigitalOutput leftMotorClock; // Step left motor
		private DigitalOutput motorCongtrollerReset;
		private DigitalOutput rightMotorControl; // Motor decay mode
		private DigitalOutput rightMotorDirection;
		private DigitalOutput leftMotorDirection;
		
		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             when IOIO connection is lost. when IOIO connection is
		 *             lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		
		@Override
		protected void setup() throws ConnectionLostException 
		{
			sonar = new UltraSonicSensor(ioio_);
			led_ = ioio_.openDigitalOutput(0, true);
			rightMotorDirection = ioio_.openDigitalOutput(20, false);
			leftMotorDirection = ioio_.openDigitalOutput(21, true);
			motorCongtrollerReset = ioio_.openDigitalOutput(22, true);
			motorEnable = ioio_.openDigitalOutput(3, true);// Must be true for
															// motors to run
			rightMotorClock = ioio_.openDigitalOutput(28, false);// Each pulse
																	// moves
																	// motor one
																	// step
			leftMotorClock = ioio_.openDigitalOutput(27, false);// Each pulse
																// moves motor
																// one step
			rightMotorControl = ioio_.openDigitalOutput(6, false);// Both
																	// motors,
																	// low =>
																	// fast
																	// motor
																	// decay
																	// mode
			log("Hello world!");
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             when IOIO connection is lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException
		{
			if (button_.isChecked())
			{
				led_.write(false); //turns light on
				try
				{
					SystemClock.sleep(1000);
					sonar.read();
					log(String.valueOf(sonar.getFrontDistance()));
					hugRightDistance(20);
					Thread.sleep(10);
					led_.write(true);
					// rightMotorClock.write(true);
					// rightMotorClock.write(false);
					// leftMotorClock.write(true);
					// leftMotorClock.write(false);
					difference = (int) (targetDirection - azimuth);
					if (difference < -180)
					{
						difference += 360;
					} else if (difference > 180)
					{
						difference -= 360;
					}
					if (difference > 3)
					{
						turnLeft();
					} else if (difference < 3)
					{
						turnRight();
					} else
					{
						goForward();
						log("STRAIGHT");
					}
				} catch (Exception e)
				{
					log(e.getClass().getName() + ", " + e.getMessage());
				}
			} else
			{
				led_.write(true);
			}
		}

		public void goForward() throws Exception
		{
			rightMotorClock.write(true);
			rightMotorClock.write(false);
			leftMotorClock.write(true);
			leftMotorClock.write(false);
		}
		
		public void hugRightDistance(int distance) throws Exception
		{
			log("Going to read the sensors");
			sonar.read();
			log("Read the sensors");
			log(String.valueOf(sonar.getRightDistance()));
//			if (sonar.getRightDistance() < distance)
//			{
//				turnTo(-3);
//			} else if (sonar.getRightDistance() > distance)
//			{
//				turnTo(3);
//			} else
//			{
//				goForward();
//			}
		}

		public void turnRight() throws Exception
		{
			rightMotorClock.write(false);
			leftMotorClock.write(true);
			leftMotorClock.write(false);
		}

		public void turnLeft() throws Exception
		{
			rightMotorClock.write(true);
			rightMotorClock.write(false);
			leftMotorClock.write(false);
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	public void log(final String msg)
	{
		runOnUiThread(new Runnable()
		{

			public void run()
			{
				mText.append(msg);
				mText.append("\n");
				mScroller.smoothScrollTo(0, mText.getBottom());
			}
		});
	}

	public void onSensorChanged(SensorEvent event)
	{
		switch (event.sensor.getType())
		{
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++)
			{
				valuesAccelerometer[i] = event.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++)
			{
				valuesMagneticField[i] = event.values[i];
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success)
		{
			SensorManager.getOrientation(matrixR, matrixValues);
			synchronized (this)
			{
				azimuth = Math.toDegrees(matrixValues[0]);
				pitch = Math.toDegrees(matrixValues[1]);
				roll = Math.toDegrees(matrixValues[2]);
				if (Math.abs(lastAzimuth - (int) azimuth) > 2)
				{
					log("azimuth = " + (int) (azimuth));
					lastAzimuth = (int) (azimuth);
				}
			}
		}

	}

	@Override
	protected IOIOLooper createIOIOLooper()
	{
		return new Looper();
	}

	public void turnTo(int degrees)
	{

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	public void forfeit()
	{
		log("NullPointerException\nThank you for using the forfeit method!");
		throw new NullPointerException();
	}

}