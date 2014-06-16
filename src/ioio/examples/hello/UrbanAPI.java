package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.exception.ConnectionLostException;
import java.util.ArrayList;
import android.os.SystemClock;

public class UrbanAPI
{
	MainActivity m;
	Looper l;
	UltraSonicSensor sonar;
	public boolean rightForward;
	public boolean leftForward;
	public boolean rightBackward;
	public boolean leftBackward;
	ArrayList<GridSquare> gridSquares = new ArrayList<GridSquare>();
	ArrayList<GridSquare> gridSquares2 = new ArrayList<GridSquare>();
	private int counter = 0;
	public static final int NORTH = 1; // No
	public static final int EAST = 2; // Eating
	public static final int SOUTH = 3; // Soggy
	public static final int WEST = 4; // Waffles
	private int goalX = 0;
	private int goalY = 2;
	private int frontSensor;
	private int leftSensor;
	private int defaultSpeed = 250;
	public int tempX = 0;
	public int tempY = 0;
	public int cellSize = 70;
	RylexAPI ra;

	public UrbanAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		rightForward = !hazFenderz;
		leftForward = !rightForward;
		rightBackward = !rightForward;
		leftBackward = !leftForward;
		ra = new RylexAPI(m, l, sonar, hazFenderz);
	}

	public void urbanChallange() throws Exception
	{
		sonar.read();
		m.log("Left distance: " + sonar.getLeftDistance());
		SystemClock.sleep(1000);
		gridSquares.add(new GridSquare(2, 0, WEST));
		m.log("We are a GO for the Urban Challenge!");
		m.log("NOTE: This is a backup!");
		m.log("The current goal is: ");
		m.log(goalX + ", " + goalY);
		// rightSensor = rightSensor();
		frontSensor = sonar.getFrontDistance();
		m.log("read front sensor");
		leftSensor = sonar.getLeftDistance();
		// m.log("rightSensor: distance = " + rightSensor());
		m.log("Read sensors");
		SystemClock.sleep(100);
		m.log("slept successfully");
		while (gridSquares.get(counter).getX() != goalX || gridSquares.get(counter).getY() != goalY)
		{
			// m.log("" + sensorMonitor.searchForIRBeam());
			m.log("in the first while loop");
			leftWallHugger();
			// m.log("left Distance: " + leftSensor);
			// m.log("front distance: " + frontSensor);
			// if (tempBool == true)
			// {
			// leftWallHugger();
			// } else
			// {
			// m.log("front distance: " + frontSensor);
			// SystemClock.sleep(250);
			// }
		}
		m.log("I broke out.");
		ra.goForward(defaultSpeed, 30);
		SystemClock.sleep(20000);
		m.log("done sleeping");
		counter = 0;
		m.log("counter made to 0");
		gridSquares2.add(new GridSquare(2, 0, WEST));
		m.log("added new GridSquare to GridSquares2");
		while (true)
		{
			m.log("about to start mazeMapper");
			mazeMapper();
		}
		// m.log("" + sensorMonitor.getFrontDistance());
		// SystemClock.sleep(250);
		// goForward(100, 50);
		// spinRight(100, 180);
		// mazeMapper();
		// showOffSpeed();
		// Put in for sensor check
		// m.log("front Distance: " + frontSensor);
		// goForward(100, 100);
		// accelerateTo(2000);
		// SystemClock.sleep(200);
		// m.log("right Distance: " + rightsensor);
		// SystemClock.sleep(200);
		// Put in for sensor check
		// goForward(100, 1);
		// SystemClock.sleep(200);
		// m.log("rear Distance: " + rearSensor());
		/*
		 * if (sensorMonitor != null) { sensorMonitor.readAllSensors(); float duration = sensorMonitor.getFrontIRPulseDuration();m.log("Detected IR beam duration: " + duration); }
		 */
		// }
	}

	public void leftWallHugger() throws Exception
	{
		m.log("beginning of wallHugger method");
		readSensors();
		// rightSensor = rightSensor();
		m.log("left Distance: " + leftSensor);
		m.log("front distance: " + frontSensor);
		if (frontSensor < 50)
		{
			m.log("wall in front. counter = " + counter);
			ra.goForward(100, frontSensor + 2);
			goBackward(defaultSpeed, 18); // Test 20, previous 19
			sonar.read();
			frontSensor = sonar.getFrontDistance();
			// rearSensor = rearSensor();
			leftSensor = sonar.getLeftDistance();
			// rightSensor = rightSensor();
			SystemClock.sleep(1000);
			frontSensor = sonar.getFrontDistance();
			// rearSensor = rearSensor();
			leftSensor = sonar.getLeftDistance();
			// rightSensor = rightSensor();
			m.log("second left Distance: " + leftSensor);
			m.log("second front distance: " + frontSensor);
		}
		if (leftSensor >= 50)
		{
			spinLeft(defaultSpeed, 90);
			m.log("The current grid is: " + gridSquares.get(counter).getX() + ", " + gridSquares.get(counter).getY());
			tempX = gridSquares.get(counter).getX();
			tempY = gridSquares.get(counter).getY();
			if (gridSquares.get(counter).getDirection() == NORTH)
			{
				m.log("The Direction is: North");
			} else if (gridSquares.get(counter).getDirection() == SOUTH)
			{
				m.log("The Direction is: South");
			} else if (gridSquares.get(counter).getDirection() == EAST)
			{
				m.log("The Direction is: East");
			} else if (gridSquares.get(counter).getDirection() == WEST)
			{
				m.log("The Direction is: West");
			}
			if (gridSquares.get(counter).getDirection() == NORTH)
			{
				tempY = gridSquares.get(counter).getY() + 1;
			} else if (gridSquares.get(counter).getDirection() == EAST)
			{
				tempX = gridSquares.get(counter).getX() + 1;
			} else if (gridSquares.get(counter).getDirection() == SOUTH)
			{
				tempY = gridSquares.get(counter).getY() - 1;
			} else if (gridSquares.get(counter).getDirection() == WEST)
			{
				tempX = gridSquares.get(counter).getX() - 1;
			}
			gridSquares.add(new GridSquare(tempX, tempY, gridSquares.get(counter).getDirection()));
			ra.goForward(defaultSpeed, 70);
			m.log("just went forward");
			counter++;
			m.log("incremented counter");
			// goForward(-300, 50);
			// spinRight(300, 360);
		} else if (frontSensor >= 50)
		{
			m.log("The current grid is: " + gridSquares.get(counter).getX() + ", " + gridSquares.get(counter).getY());
			tempX = gridSquares.get(counter).getX();
			tempY = gridSquares.get(counter).getY();
			if (gridSquares.get(counter).getDirection() == NORTH)
			{
				m.log("The Direction is: North");
			} else if (gridSquares.get(counter).getDirection() == SOUTH)
			{
				m.log("The Direction is: South");
			} else if (gridSquares.get(counter).getDirection() == EAST)
			{
				m.log("The Direction is: East");
			} else if (gridSquares.get(counter).getDirection() == WEST)
			{
				m.log("The Direction is: West");
			}
			if (gridSquares.get(counter).getDirection() == NORTH)
			{
				tempY = gridSquares.get(counter).getY() + 1;
			} else if (gridSquares.get(counter).getDirection() == EAST)
			{
				tempX = gridSquares.get(counter).getX() + 1;
			} else if (gridSquares.get(counter).getDirection() == SOUTH)
			{
				tempY = gridSquares.get(counter).getY() - 1;
			} else if (gridSquares.get(counter).getDirection() == WEST)
			{
				tempX = gridSquares.get(counter).getX() - 1;
			}
			gridSquares.add(new GridSquare(tempX, tempY, gridSquares.get(counter).getDirection()));
			ra.goForward(defaultSpeed, 70);
			counter++;
		} else
		{
			ra.spinRight(defaultSpeed, 90);
			goBackward(100, 20);
			ra.goForward(defaultSpeed, 10);
			// tempBool = false; //WARNING: This is a test!!! Return to false
			// after testing <-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<--
		}
		m.log("End of wallHugger method");
	}

	public void goBackward(int speed, int centimeters) throws ConnectionLostException
	{
		double pulses = centimeters * 10.95;
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

	public void spinLeft(int speed, double degrees) throws ConnectionLostException
	{
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			gridSquares.get(counter).setDirection(WEST);
		} else
		{
			gridSquares.get(counter).setDirection(gridSquares.get(counter).getDirection() - 1);
		}
		// rightMotorClockPulse = ioio.openDigitalOutput(MOTOR_CLOCK_RIGHT_PIN);
		// leftMotorClockPulse = ioio.openDigitalOutput(MOTOR_CLOCK_LEFT_PIN);
		l.leftMotorDirection.write(leftBackward);
		l.rightMotorDirection.write(rightForward);
		double pulses = degrees * 2.46;
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void updateGrid()
	{
		tempX = gridSquares.get(counter).getX();
		tempY = gridSquares.get(counter).getY();
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			m.log("The Direction is: North");
		} else if (gridSquares.get(counter).getDirection() == SOUTH)
		{
			m.log("The Direction is: South");
		} else if (gridSquares.get(counter).getDirection() == EAST)
		{
			m.log("The Direction is: East");
		} else if (gridSquares.get(counter).getDirection() == WEST)
		{
			m.log("The Direction is: West");
		}
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			tempY = gridSquares.get(counter).getY() + 1;
		} else if (gridSquares.get(counter).getDirection() == EAST)
		{
			tempX = gridSquares.get(counter).getX() + 1;
		} else if (gridSquares.get(counter).getDirection() == SOUTH)
		{
			tempY = gridSquares.get(counter).getY() - 1;
		} else if (gridSquares.get(counter).getDirection() == WEST)
		{
			tempX = gridSquares.get(counter).getX() - 1;
		}
		gridSquares.add(new GridSquare(tempX, tempY, gridSquares.get(counter).getDirection()));
	}

	public void updateGrid2()
	{
		tempX = gridSquares2.get(counter).getX();
		tempY = gridSquares2.get(counter).getY();
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			m.log("The Direction is: North");
		} else if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			m.log("The Direction is: South");
		} else if (gridSquares2.get(counter).getDirection() == EAST)
		{
			m.log("The Direction is: East");
		} else if (gridSquares2.get(counter).getDirection() == WEST)
		{
			m.log("The Direction is: West");
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			tempY = gridSquares2.get(counter).getY() + 1;
		} else if (gridSquares2.get(counter).getDirection() == EAST)
		{
			tempX = gridSquares2.get(counter).getX() + 1;
		} else if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			tempY = gridSquares2.get(counter).getY() - 1;
		} else if (gridSquares2.get(counter).getDirection() == WEST)
		{
			tempX = gridSquares2.get(counter).getX() - 1;
		}
		gridSquares2.add(new GridSquare(tempX, tempY, gridSquares2.get(counter).getDirection()));
	}

	public void mazeMapper() throws ConnectionLostException, InterruptedException
	{
		m.log("Starting Maze Mapper...");
		for (int i = gridSquares.size() - 1; i >= 0; i--)
		{
			m.log("Have been in the forloop " + i + "time(s).");
			m.log("the current grid is: " + gridSquares2.get(counter).getX() + ", " + gridSquares2.get(counter).getY());
			m.log("it's comparing it to the grid of last run: " + gridSquares.get(i).getX() + ", " + gridSquares.get(i).getY());
			if (gridSquares2.get(counter).getX() == gridSquares.get(i).getX() && gridSquares2.get(counter).getY() == gridSquares.get(i).getY())
			{
				m.log("It found a match");
				if (gridSquares.get(i).getDirection() == NORTH)
				{
					goNorth();
				}
				if (gridSquares.get(i).getDirection() == SOUTH)
				{
					goSouth();
				}
				if (gridSquares.get(i).getDirection() == EAST)
				{
					m.log("It was East last time");
					goEast();
				}
				if (gridSquares.get(i).getDirection() == WEST)
				{
					goWest();
				}
				m.log("incremented counter");
				updateGrid2();
				ra.goForward(defaultSpeed, cellSize);
				counter++;
				return;
			}
		}
	}

	// TODO: Go Directions Methods
	public void goNorth() throws ConnectionLostException, InterruptedException
	{
		readSensors();
		// rightSensor = rightSensor();
		if (frontSensor < 50)
		{
			ra.goForward(100, 25);
			goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			// goForward(defaultSpeed, 70);
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			spinRight2(defaultSpeed, 90);
			spinRight2(defaultSpeed, 90);
			m.log("FAILED_BUILD_EXCEPTION");
		}
		if (gridSquares2.get(counter).getDirection() == EAST)
		{
			spinLeft2(defaultSpeed, 90);
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			spinRight2(defaultSpeed, 90);
			if (leftSensor < 30)
			{
				goBackward(100, 20);
				ra.goForward(defaultSpeed, 10);
			}
		}
	}

	public void goSouth() throws ConnectionLostException, InterruptedException
	{
		readSensors();
		// rightSensor = rightSensor();
		if (frontSensor < 50)
		{
			ra.goForward(100, 25);
			goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			// goForward(defaultSpeed, 70);
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			spinRight2(defaultSpeed, 90);
			spinRight2(defaultSpeed, 90);
			m.log("FAILED_BUILD_EXCEPTION");
		}
		if (gridSquares2.get(counter).getDirection() == EAST)
		{
			spinRight2(defaultSpeed, 90);
			if (leftSensor < 30)
			{
				goBackward(100, 20);
				ra.goForward(defaultSpeed, 10);
			}
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void goEast() throws ConnectionLostException, InterruptedException
	{
		readSensors();
		if (frontSensor < 50)
		{
			ra.goForward(100, 25);
			goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == EAST)
		{
			// goForward(defaultSpeed, 70);
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			spinRight2(defaultSpeed, 90);
			spinRight2(defaultSpeed, 90);
			m.log("FAILED_BUILD_EXCEPTION");
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			spinRight2(defaultSpeed, 90);
			if (leftSensor < 30)
			{
				goBackward(100, 20);
				ra.goForward(defaultSpeed, 10);
			}
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void readSensors() throws ConnectionLostException, InterruptedException
	{
		sonar.read();
		frontSensor = sonar.getFrontDistance();
		leftSensor = sonar.getLeftDistance();
		SystemClock.sleep(1000);
		sonar.read();
		frontSensor = sonar.getFrontDistance();
		leftSensor = sonar.getLeftDistance();
	}

	public void goWest() throws ConnectionLostException, InterruptedException
	{
		readSensors();
		if (frontSensor < 50)
		{
			ra.goForward(100, 25);
			goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			// goForward(defaultSpeed, 70);
		}
		if (gridSquares2.get(counter).getDirection() == EAST)
		{
			spinRight2(defaultSpeed, 90);
			spinRight2(defaultSpeed, 90);
			m.log("FAILED_BUILD_EXCEPTION");
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			spinRight2(defaultSpeed, 90);
			if (leftSensor < 30)
			{
				goBackward(100, 20);
				ra.goForward(defaultSpeed, 10);
			}
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void spinRight2(int speed, double degrees) throws ConnectionLostException
	{
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			gridSquares2.get(counter).setDirection(NORTH);
		} else
		{
			gridSquares2.get(counter).setDirection(gridSquares2.get(counter).getDirection() + 1);
		}
		l.rightMotorDirection.write(rightBackward);
		l.leftMotorDirection.write(leftForward);
		double pulses = degrees * 2.54;
		for (int i = 0; i < pulses; i++)
		{
			SystemClock.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void spinLeft2(int speed, double degrees) throws ConnectionLostException
	{
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			gridSquares2.get(counter).setDirection(WEST);
		} else
		{
			gridSquares2.get(counter).setDirection(gridSquares2.get(counter).getDirection() - 1);
		}
		// rightMotorClockPulse = ioio.openDigitalOutput(MOTOR_CLOCK_RIGHT_PIN);
		// leftMotorClockPulse = ioio.openDigitalOutput(MOTOR_CLOCK_LEFT_PIN);
		l.leftMotorDirection.write(leftBackward);
		l.rightMotorDirection.write(rightForward);
		double pulses = degrees * 2.46;
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
