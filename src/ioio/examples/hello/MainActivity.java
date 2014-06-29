package ioio.examples.hello;

import ioio.examples.hello.R.string;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.content.Intent;
import android.graphics.DashPathEffect;
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
	private final static String LOGTAG = "IOIOLooper";
	private PwmOutput rightMotorClock;
	private PwmOutput leftMotorClock;
	private boolean startedAcceleration = false;
	private int leftMotorPWMfrequency = 200;// used to be both 200;
	private int rightMotorPWMfrequency = 195;
	private AnalogInput IR;
	private Runnable urban = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				ua.urbanChallange();
			} catch (Exception e)
			{
			}
		}
	};
	private Runnable urbanRight = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				ua.urbanChallangeRight();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	private Runnable gold = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				ga.goldRush();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	private Runnable drag = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				da.YOLO();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	private Runnable test = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				// ra.test();
				ra.spinRight(200, 360);
				SystemClock.sleep(1000);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	private Runnable testUltra = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				ra.testUltrasonic();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	private Runnable theChallenge;
	private ToggleButton button_;
	private int targetDirection = 90;
	private int difference;
	private UltraSonicSensor sonar;
	private SensorMonitor sensorMonitor;
	public Button endLearn;
	public Button startMap;
	RylexAPI ra;
	UrbanAPI ua;
	DragAPI da;
	GoldAPI ga;
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
	public double azimuth;
	private int lastAzimuth;
	private double pitch;
	private double roll;
	private boolean logAzimuth = false;

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
		m = this;
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
		title.setText(R.string.hello);
		challenge = intent.getExtras().getString(Setup.CHALLENGE);
		title.append("\nRunning challenge: " + challenge);
		hazFenderz = intent.getExtras().getBoolean(Setup.FENDERZ);
		title.append("\nHaz fenderz: " + hazFenderz);
		if (challenge.equals(Setup.URBAN))
		{
			theChallenge = urban;
		} else if (challenge.equals(Setup.URBAN_RIGHT))
		{
			theChallenge = urbanRight;
		} else if (challenge.equals(Setup.GOLD))
		{
			theChallenge = gold;
		} else if (challenge.equals(Setup.DRAG))
		{
			theChallenge = drag;
		} else if (challenge.equals(Setup.TEST))
		{
			theChallenge = test;
		} else if (challenge.equals(Setup.TEST_ULTRA))
		{
			theChallenge = testUltra;
		}
	}

	public void mapMaze(View v) throws Exception
	{
		while (!ua.doneSolution)
		{
			ua.mazeMapper();
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
			// sensorMonitor = new SensorMonitor(ioio_, m);
			// sensorMonitor.setupAllSensors(true, false, false, false, false);
			ra = new RylexAPI(m, this, sonar, sensorMonitor, hazFenderz);
			ua = new UrbanAPI(m, this, sonar, hazFenderz);
			da = new DragAPI(m, this, sonar, hazFenderz, ioio_);
			// sensorReader = new SensorReader(sonar, false);
			// sr = new Thread(sensorReader);
			// sr.start();
			IR = ioio_.openAnalogInput(31);
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
			// startMap = (Button) findViewById(R.id.restartButton);
			// startMap.setOnClickListener(new View.OnClickListener()
			// {
			// public void onClick(View v)
			// {
			// try
			// {
			// mapMaze();
			// } catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			// }
			// });
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

	public float readIR() throws Exception
	{
		return IR.read();
	}

	public void accelerateTo(final int finalPWMfrequency)
	{
		if (startedAcceleration)
			return;
		new Thread(new Runnable()
		{
			public void run()
			{
				int counter = 0;
				while (leftMotorPWMfrequency < finalPWMfrequency)
				{
					try
					{
						counter++;
						SystemClock.sleep(1);
						log("Setting Motor frequency : " + leftMotorPWMfrequency);
						// rightMotorClock.setFrequency(rightMotorPWMfrequency);
						// leftMotorClock.setFrequency(leftMotorPWMfrequency);
						leftMotorPWMfrequency += 2;
						rightMotorPWMfrequency += 2;
						// if(frontSensor() < 100 && counter >= 100) {
						// stop();
						// goForward(450);
						// counter = 0;
						// }
					} catch (Exception ex)
					{
						log("Motor clock pulsing hiccup");
					}
				}
			}
		}).start();
		startedAcceleration = true;
	}
}