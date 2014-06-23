//Copyright 2013 Wintriss Technical Schools

package ioio.examples.hello;

import android.os.SystemClock;
import android.util.Log;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.exception.ConnectionLostException;

public class SensorMonitor
{
    private static final String LOGTAG = "SensorsMonitor";

    public enum SensorType {
        FRONT_ULTRASONIC_SENSOR,
        LEFT_ULTRASONIC_SENSOR,
        RIGHT_ULTRASONIC_SENSOR,
        REAR_ULTRASONIC_SENSOR,
        FRONT_IR_SENSOR
    }

    private PulseInput frontSensorInput;
    private PulseInput frontIRSensorInput;
    private PulseInput rearSensorInput;
    private PulseInput leftSensorInput;
    private PulseInput rightSensorInput;
    
    private DigitalOutput frontSensorOutput;
    private DigitalOutput rearSensorOutput;
    private DigitalOutput leftSensorOutput;
    private DigitalOutput rightSensorOutput;
    
    private static final int FRONT_STROBE_OUTPUT_PIN = 16;
    private static final int LEFT_STROBE_OUTPUT_PIN = 17;
    private static final int RIGHT_STROBE_OUTPUT_PIN = 15;
    private static final int REAR_STROBE_OUTPUT_PIN = 14;
    
    private static final int FRONT_SENSOR_INPUT_PIN = 12;
    private static final int LEFT_SENSOR_INPUT_PIN = 13;
    private static final int RIGHT_SENSOR_INPUT_PIN = 11;
    private static final int REAR_SENSOR_INPUT_PIN = 10;
    
    private static final int IR_SENSOR_INPUT_PIN = 4;
    
    private int mLeftDistance = 0;
    private int mFrontDistance = 0;
    private int mRightDistance = 0;
    private int mRearDistance = 0;
    
    private float m_frontIRPulseDuration = 0;    
    
    private IOIO mIoio = null;
    private Thread irThread;

    boolean mFrontIRSensor = false;
    boolean mFrontUSSensor = false;
    boolean mLeftUSSensor = false;
    boolean mRightUSSensor = false;
    boolean mRearUSSensor = false;

    MainActivity mActivity;
    
    /**
     * Constructor of a Sensor Monitor instance.
     *
     * @param ioio the IOIO instance used to communicate with the sensor
     * @throws ConnectionLostException 
     *
     */
    public SensorMonitor(IOIO ioio, MainActivity activity) throws ConnectionLostException
    {
        mIoio = ioio;
        mActivity = activity;
        leftSensorOutput = mIoio.openDigitalOutput(LEFT_STROBE_OUTPUT_PIN, false);
        leftSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(LEFT_SENSOR_INPUT_PIN),
                PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.POSITIVE, false);
    }
    
    public int readLeft() throws ConnectionLostException, InterruptedException {
    	leftSensorOutput.write(true);
    	leftSensorOutput.write(false);
		int distance = (int) (leftSensorInput.getDuration() * 18000);
		return distance;
    }
    
    public int readFront() throws ConnectionLostException, InterruptedException {
    	log("reading front sensor");
    	frontSensorOutput.write(true);
    	log("strobing");
    	frontSensorOutput.write(false);
    	log("strobed");
		int distance = (int) (frontSensorInput.getDuration() * 18000);
		log("got front distance"); //Logan's team isn't maping the mazz
		return distance;
    }
    
    public int readRear() throws ConnectionLostException, InterruptedException {
    	rearSensorOutput.write(true);
    	rearSensorOutput.write(false);
		int distance = (int) (rearSensorInput.getDuration() * 18000);
		return distance;
    }
    
    public int readRight() throws ConnectionLostException, InterruptedException {
    	rightSensorOutput.write(true);
    	rightSensorOutput.write(false);
		int distance = (int) (rightSensorInput.getDuration() * 18000);
		return distance;
    }
    
    public void log(String msg) {
    	Log.d(LOGTAG, msg);
        mActivity.log(msg);
    }
    
    void setupAllSensors(boolean frontIRSensor, boolean frontUSSensor, boolean leftUSSensor, boolean rightUSSensor,
            boolean rearUSSensor) throws ConnectionLostException {

        mFrontIRSensor = frontIRSensor;
        mFrontUSSensor = frontUSSensor;
        mLeftUSSensor = leftUSSensor;
        mRightUSSensor = rightUSSensor;
        mRearUSSensor = rearUSSensor;

        if (frontIRSensor) {
            frontIRSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(IR_SENSOR_INPUT_PIN),
                    PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.NEGATIVE, false);
            setupSensorMonitorThread(SensorType.FRONT_IR_SENSOR);
        }

        if (frontUSSensor) {
            frontSensorOutput = mIoio.openDigitalOutput(FRONT_STROBE_OUTPUT_PIN, false);
            frontSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(FRONT_SENSOR_INPUT_PIN),
                    PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.POSITIVE, false);

            //setupSensorMonitorThread(SensorType.FRONT_ULTRASONIC_SENSOR);
        }

        if (rightUSSensor) {
            rightSensorOutput = mIoio.openDigitalOutput(RIGHT_STROBE_OUTPUT_PIN, false);
            rightSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(RIGHT_SENSOR_INPUT_PIN),
                    PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.POSITIVE, false);

            //setupSensorMonitorThread(SensorType.RIGHT_ULTRASONIC_SENSOR);
        }

        if (leftUSSensor) {
            leftSensorOutput = mIoio.openDigitalOutput(LEFT_STROBE_OUTPUT_PIN, false);
            leftSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(LEFT_SENSOR_INPUT_PIN),
                    PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.POSITIVE, false);
            log("leftUS sensor is true");
            //setupSensorMonitorThread(SensorType.LEFT_ULTRASONIC_SENSOR);
        } else {
        	log("leftUS sensor is false");
        }

        if (rearUSSensor) {
            rearSensorOutput = mIoio.openDigitalOutput(REAR_STROBE_OUTPUT_PIN, false);
            rearSensorInput = mIoio.openPulseInput(new DigitalInput.Spec(REAR_SENSOR_INPUT_PIN),
                    PulseInput.ClockRate.RATE_62KHz, PulseInput.PulseMode.POSITIVE, false);

            //setupSensorMonitorThread(SensorType.REAR_ULTRASONIC_SENSOR);
        }
    }
    
    private void setupSensorMonitorThread(SensorType type) {
        switch(type) {
        case FRONT_IR_SENSOR:
            irThread = new Thread(new Runnable() {
                private double currentIRPulseDuration;
				private double previousIRPulseDuration;

				public void run() {
                    while (true) {
                    	SystemClock.sleep(100);
                        try {
                            float inputVal = frontIRSensorInput.waitPulseGetDuration();
                            
                            currentIRPulseDuration = 1.0/inputVal;
                            if (currentIRPulseDuration < (previousIRPulseDuration + 100) ) {
                            
                            setFrontIRPulseDuration((int)currentIRPulseDuration);
                            }
                            previousIRPulseDuration = currentIRPulseDuration;
                        } catch (Exception e) {
                            e.printStackTrace();
                            log("FRONT_IR_SENSOR: exception : " + e);
                            break;
                        }
                    }
                }
            });
            irThread.start();
        break;

        case FRONT_ULTRASONIC_SENSOR:
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        float inputVal = 0;
                        try {
                            inputVal = frontSensorInput.waitPulseGetDuration();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ConnectionLostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        setFrontUltrasonicPulseDuration(inputVal);
                    }
                }

            }).start();
        break;

        case LEFT_ULTRASONIC_SENSOR:
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        float inputVal = 0;
                        try {
                            inputVal = leftSensorInput.waitPulseGetDuration();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ConnectionLostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        setLeftUltrasonicPulseDuration(inputVal);
                    }
                }
            }).start();
        break;

        case RIGHT_ULTRASONIC_SENSOR:
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        float inputVal = 0;
                        try {
                            inputVal = rightSensorInput.waitPulseGetDuration();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ConnectionLostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        setRightUltrasonicPulseDuration(inputVal);
                    }
                }
            }).start();
        break;

        case REAR_ULTRASONIC_SENSOR:
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        float inputVal = 0;
                        try {
                            inputVal = rearSensorInput.waitPulseGetDuration();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ConnectionLostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        setRearUltrasonicPulseDuration(inputVal);
                    }
                }
            }).start();
        break;
        
        } // end of switch()
    }

    private synchronized void setFrontIRPulseDuration(float inputVal) {
    	//log("FRONT_IR_SENSOR: duration = " + inputVal);
        m_frontIRPulseDuration = inputVal;
    }

    public synchronized float getFrontIRPulseDuration() {
        return m_frontIRPulseDuration;
    }

    private synchronized void setFrontUltrasonicPulseDuration(float inputVal) {
        mFrontDistance = (int) (inputVal * 18000);
    }

    private synchronized void setLeftUltrasonicPulseDuration(float inputVal) {
        mLeftDistance = (int) (inputVal * 18000);
    }

    private synchronized void setRightUltrasonicPulseDuration(float inputVal) {
        mRightDistance = (int) (inputVal * 18000);
    }

    private synchronized void setRearUltrasonicPulseDuration(float inputVal) {
        mRearDistance = (int) (inputVal * 18000);
    }

    public boolean searchForIRBeam() throws ConnectionLostException,
            InterruptedException {
        frontSensorOutput.write(true);
        frontSensorOutput.write(false);
        //SystemClock.sleep(10);
        //int duration = getFrontIRPulseDuration();
        //if (duration > 0) {
        //    return true;
        //}
        return false;
    }
    
    public void readAllSensors() throws ConnectionLostException, InterruptedException
    {
        // Clear all the old sensor readings first
        setFrontIRPulseDuration(0);
//        setFrontUltrasonicPulseDuration(0);
//        setLeftUltrasonicPulseDuration(0);
//        setRightUltrasonicPulseDuration(0);
//        setRearUltrasonicPulseDuration(0);
        
        frontSensorOutput.write(true);
        frontSensorOutput.write(false);

        leftSensorOutput.write(true);
        leftSensorOutput.write(false);
//
//        rightSensorOutput.write(true);
//        rightSensorOutput.write(false);
//
//        rearSensorOutput.write(true);
//        rearSensorOutput.write(false);
        
        SystemClock.sleep(20);
    }



    public int readUltrasonicDistance(PulseInput sensorInput, DigitalOutput sensorOutput)
    {
    	int distance = 0;
    	try {
    		sensorOutput.write(true);
    		sensorOutput.write(false);
    		distance = (int) (sensorInput.waitPulseGetDuration() * 18000);

    	} catch (Exception e) {
    		log("ultrasonnic sensor hiccup");
    	}
    	return distance;
    }
    
    /**
     * Gets the last read distance in cm of the front sensor
     *
     * @return the front distance in cm
     */
    public int getFrontDistance()
    {
    	mFrontDistance = readUltrasonicDistance(frontSensorInput, frontSensorOutput);
    	return mFrontDistance;
    }

    /**
     * Gets the last read distance in cm of the right sensor
     *
     * @return the right distance in cm
     */
    public int getRightDistance()
    {
    	mRightDistance = readUltrasonicDistance(rightSensorInput, rightSensorOutput);
    	return mRightDistance;
    }

    /**
     * Gets the last read distance in cm of the left sensor
     *
     * @return the left distance in cm
     */
    public int getLeftDistance()
    {
    	log("getting left distance...");
    	mLeftDistance = readUltrasonicDistance(leftSensorInput, leftSensorOutput);
    	log("got the left distance");
    	log(mLeftDistance + "");
        return mLeftDistance;
    }

    /**
     * Gets the last read distance in cm of the rear sensor
     *
     * @return the right distance in cm
     */
    public int getRearDistance()
    {
    	mRearDistance = readUltrasonicDistance(rearSensorInput, rearSensorOutput);
        return mRearDistance;
    }
    
    /**
     * Closes all the connections to the used pins
     */
    public void closeAllSensorConnections()
    {
        frontSensorInput.close();
//        rearSensorInput.close();
        leftSensorInput.close();
//        rightSensorInput.close();
        
        frontSensorOutput.close();
//        rearSensorOutput.close();
        leftSensorOutput.close();
//        rightSensorOutput.close();
    }

    public boolean foundIRBeam() {
        float duration = getFrontIRPulseDuration();
        if (duration > 0) {
            return true;
        }
        return false;
    }
}
