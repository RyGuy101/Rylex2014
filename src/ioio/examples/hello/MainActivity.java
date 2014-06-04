package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application. It displays a toggle button on the screen, which enables control of the on-board LED. Modified by Vic rev 130402A
 */
public class MainActivity extends IOIOActivity implements SensorEventListener
{
	private String challenge;
	private boolean hazFenderz;
	private Runnable urban = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				// if (sensorReader.getRead() != true)
				// {
				// sensorReader.setRead(true);
				// }
				ra.goForward();
				SystemClock.sleep(10);
				// SystemClock.sleep(1000);
				// sonar.read();
				// log("Distance F = " + String.valueOf(sonar.getFrontDistance()));
				// log("Distance L = " + String.valueOf(sonar.getLeftDistance()));
				// /ra.hugLeftDistance(200);
				// Thread.sleep(10);
				// led_.write(true);
				// rightMotorClock.write(true);
				// rightMotorClock.write(false);
				// leftMotorClock.write(true);
				// leftMotorClock.write(false);
				// difference = (int) (targetDirection - azimuth);
				// if (difference < -180)
				// {
				// difference += 360;
				// } else if (difference > 180)
				// {
				// difference -= 360;
				// }
				// if (difference > 3)
				// {
				// ra.turnLeft();
				// } else if (difference < 3)
				// {
				// ra.turnRight();
				// } else
				// {
				// ra.goForward();
				// log("STRAIGHT");
				// }
				// log("");
			} catch (Exception e)
			{
			}
		}
	};
	private Runnable gold = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO gold stuff goes here
		}
	};
	private Runnable drag = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO drag stuff goes here
		}
	};
	private Runnable test = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO test stuff goes here
		}
	};
	private Runnable theChallenge;
	private ToggleButton button_;
	private int targetDirection = 90;
	private int difference;
	private UltraSonicSensor sonar;
	RylexAPI ra;
	private TextView title;
	private TextView mText;
	private ScrollView mScroller;
	MainActivity m;
	// SensorReader sensorReader;
	// Thread sr;
	// Looper l;
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
	private boolean logAzimuth = false;
	public Button startButton;

	/**
	 * Called when the activity is first created. Here we normally initialize our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button_ = (ToggleButton) findViewById(R.id.button);
		mScroller = (ScrollView) findViewById(R.id.scroller);
		mText = (TextView) findViewById(R.id.logText);
		title = (TextView) findViewById(R.id.title);
		m = new MainActivity();
		// l = new Looper();
		// Compass stuff
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];
		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];
		// logAzimuth = false;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Intent intent = getIntent();
		challenge = intent.getExtras().getString(Setup.CHALLENGE);
		title.append(" Running challenge: " + challenge);
		hazFenderz = intent.getExtras().getBoolean(Setup.FENDERZ);
		title.append(", Haz fenderz: " + hazFenderz);
		if (challenge == Setup.URBAN)
		{
			theChallenge = urban;
		} else if (challenge == Setup.GOLD)
		{
			theChallenge = gold;
		} else if (challenge == Setup.DRAG)
		{
			theChallenge = drag;
		} else if (challenge == Setup.TEST)
		{
			theChallenge = test;
		}
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
		sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	public void toSetup(View v)
	{
		onBackPressed();
	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run every time the application is resumed and aborted when it is paused. The method setup() will be called right after a connection with the IOIO has been established (which might happen several times!). Then, loop() will be called repetitively until the IOIO gets disconnected
	 */
	class Looper extends BaseIOIOLooper
	{
		public DigitalOutput led_;// The on-board LED
		public DigitalOutput motorEnable; // Both motors
		public DigitalOutput rightMotorClock; // Step right motor
		public DigitalOutput leftMotorClock; // Step left motor
		public DigitalOutput motorCongtrollerReset;
		public DigitalOutput rightMotorControl; // Motor decay mode
		public DigitalOutput rightMotorDirection;
		public DigitalOutput leftMotorDirection;

		/**
		 * Called every time a connection with IOIO has been established. Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             when IOIO connection is lost. when IOIO connection is lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException
		{
			sonar = new UltraSonicSensor(ioio_);
			ra = new RylexAPI(m, this, sonar, hazFenderz);
			// sensorReader = new SensorReader(sonar, false);
			// sr = new Thread(sensorReader);
			// sr.start();
			led_ = ioio_.openDigitalOutput(0, true);
			rightMotorDirection = ioio_.openDigitalOutput(20, ra.rightForward);// Goes forward
			leftMotorDirection = ioio_.openDigitalOutput(21, ra.leftForward);// Goes forward
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
				led_.write(false); // turns light on
				try
				{
					theChallenge.run();
				} catch (Exception e)
				{
					log(e.getClass().getName() + ", " + e.getMessage());
				}
			} else
			{
				led_.write(true);
			}
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
		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI, valuesAccelerometer, valuesMagneticField);
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
					if (logAzimuth)
					{
						log("azimuth = " + (int) (azimuth));
					}
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