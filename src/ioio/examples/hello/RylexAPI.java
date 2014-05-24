package ioio.examples.hello;

public class RylexAPI extends MainActivity
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;

	public RylexAPI(MainActivity m, Looper l, UltraSonicSensor sonar)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;

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
		l.rightMotorClock.write(true);
		l.rightMotorClock.write(false);
		l.leftMotorClock.write(true);
		l.leftMotorClock.write(false);
	}

	public void hugRightDistance(int distance) throws Exception
	{
		log("Going to read the sensors");
		sonar.read();
		log("Read the sensors");
		log(String.valueOf(sonar.getRightDistance()));
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
	
	public void hugLeftDistance(int distance) throws Exception
	{
		log("Going to read the sensors");
		sonar.read();
		log("Read the sensors");
		log(String.valueOf(sonar.getLeftDistance()));
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
