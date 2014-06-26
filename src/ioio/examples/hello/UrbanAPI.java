package ioio.examples.hello;

import ioio.examples.hello.MainActivity.Looper;
import ioio.lib.api.exception.ConnectionLostException;
import java.util.ArrayList;
import android.os.SystemClock;
import android.util.Log;

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
	public int startDirection = NORTH;
	private int startX = 0;
	private int startY = 0;
	private int goalX = 0;
	private int goalY = 11;
	private double frontSensor;
	private double leftSensor;
	private double rightSensor;
	private double rearSensor;
	private int defaultSpeed = 100;
	public int tempX = 0;
	public int tempY = 0;
	public int cellSize = 70;
	RylexAPI ra;
	public double northDegrees = 1000;
	public double southDegrees = 1000;
	public double eastDegrees = 1000;
	public double westDegrees = 1000;
	int wallDistance = 50;
	int desiredWallDistance = 24;

	public UrbanAPI(MainActivity m, Looper l, UltraSonicSensor sonar, boolean hazFenderz)
	{
		this.m = m;
		this.l = l;
		this.sonar = sonar;
		rightForward = !hazFenderz;
		leftForward = !rightForward;
		rightBackward = !rightForward;
		leftBackward = !leftForward;
		ra = m.ra;
	}

	public void urbanChallange() throws Exception
	{
		gridSquares.add(new GridSquare(startX, startY, startDirection));
		m.log("The current goal is: ");
		m.log(goalX + ", " + goalY);
		readSensors();
		backwardAlign();
		while (gridSquares.get(counter).getX() != goalX || gridSquares.get(counter).getY() != goalY)
		{
			m.log("In the first while loop");
			leftWallHugger();
		}
		ra.goForward(defaultSpeed, 30);
		m.log("Initiating victory dance");
		ra.victoryDance();
		ra.sleep(20000);
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
		// ra.sleep(250);
		// goForward(100, 50);
		// spinRight(100, 180);
		// mazeMapper();
		// showOffSpeed();
		// Put in for sensor check
		// m.log("front Distance: " + frontSensor);
		// goForward(100, 100);
		// accelerateTo(2000);
		// ra.sleep(200);
		// m.log("right Distance: " + rightsensor);
		// ra.sleep(200);
		// Put in for sensor check
		// goForward(100, 1);
		// ra.sleep(200);
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
		if (frontSensor < wallDistance)
		{
			m.log("wall in front. counter = " + counter);
			forwardAlign();
			readSensors();
			m.log("second left Distance: " + leftSensor);
			m.log("second front distance: " + frontSensor);
		}
		if (leftSensor >= wallDistance)
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
			goOneCell();
			// goForward(-300, 50);
			// spinRight(300, 360);
		} else if (frontSensor >= wallDistance)
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
			goOneCell();
		} else
		{
			spinRight(defaultSpeed, 90);
			readSensors();
			backwardAlign();
			// tempBool = false; //WARNING: This is a test!!! Return to false
			// after testing <-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<--
		}
		m.log("End of wallHugger method");
	}

	public void goOneCell() throws Exception
	{
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			tempY = gridSquares.get(counter).getY() + 1;
			// if (northDegrees != 1000)
			// {
			// ra.goStraight(defaultSpeed, cellSize, northDegrees);
			// }
		} else if (gridSquares.get(counter).getDirection() == EAST)
		{
			tempX = gridSquares.get(counter).getX() + 1;
			// if (eastDegrees != 1000)
			// {
			// ra.goStraight(defaultSpeed, cellSize, eastDegrees);
			// }
		} else if (gridSquares.get(counter).getDirection() == SOUTH)
		{
			tempY = gridSquares.get(counter).getY() - 1;
			// if (southDegrees != 1000)
			// {
			// ra.goStraight(defaultSpeed, cellSize, southDegrees);
			// }
		} else if (gridSquares.get(counter).getDirection() == WEST)
		{
			tempX = gridSquares.get(counter).getX() - 1;
			// if (westDegrees != 1000)
			// {
			// ra.goStraight(defaultSpeed, cellSize, westDegrees);
			// }
		}
		// if (northDegrees == 1000 && eastDegrees == 1000 && westDegrees == 1000 && southDegrees == 1000)
		// {
		// ra.goForward(defaultSpeed, cellSize);
		followWallLeftOneCell(defaultSpeed, desiredWallDistance);
		// }
		gridSquares.add(new GridSquare(tempX, tempY, gridSquares.get(counter).getDirection()));
		// ra.goForward(defaultSpeed, cellSize);
		m.log("just went forward");
		counter++;
		m.log("incremented counter");
	}

	public double findBestDegrees(double degrees)
	{
		double azi = m.azimuth;
		double diff0 = degrees - azi;
		double diff1 = degrees + 360 - azi;
		double diff2 = degrees - 360 - azi;
		double diff0abs = Math.abs(diff0);
		double diff1abs = Math.abs(diff1);
		double diff2abs = Math.abs(diff2);
		double diff01abs = Math.min(diff0abs, diff1abs);
		double diff012abs = Math.min(diff01abs, diff2abs);
		double newDegrees = 0;
		if (diff012abs == diff0abs)
		{
			newDegrees = diff0 + azi;
		} else if (diff012abs == diff1abs)
		{
			newDegrees = diff1 + azi;
		} else if (diff012abs == diff2abs)
		{
			newDegrees = diff2 + azi;
		}
		return newDegrees;
	}

	public void backwardAlign() throws Exception
	{
		ra.goBackward(100, (int) (rearSensor + 2));
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			northDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == SOUTH)
		{
			southDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == EAST)
		{
			eastDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == WEST)
		{
			westDegrees = m.azimuth;
		}
		ra.goForward(defaultSpeed, 17);
	}

	public void forwardAlign() throws Exception
	{
		ra.goForward(100, (int) (frontSensor + 2));
		if (gridSquares.get(counter).getDirection() == NORTH)
		{
			northDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == SOUTH)
		{
			southDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == EAST)
		{
			eastDegrees = m.azimuth;
		} else if (gridSquares.get(counter).getDirection() == WEST)
		{
			westDegrees = m.azimuth;
		}
		ra.goBackward(defaultSpeed, 20); // Test 20, previous 19
		m.azimuth = 0;
	}

	public void spinLeft(int speed, double degrees) throws Exception
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
		double pulses = degrees * ra.degreesLeftX;
		for (int i = 0; i < pulses; i++)
		{
			ra.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void spinRight(int speed, double degrees) throws Exception
	{
		if (gridSquares.get(counter).getDirection() == WEST)
		{
			gridSquares.get(counter).setDirection(NORTH);
		} else
		{
			gridSquares.get(counter).setDirection(gridSquares.get(counter).getDirection() + 1);
		}
		l.rightMotorDirection.write(true);
		l.leftMotorDirection.write(true);
		double pulses = degrees * 2.54;
		for (int i = 0; i < pulses; i++)
		{
			ra.sleep(1000 / speed);
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

	public void mazeMapper() throws Exception
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
	public void goNorth() throws Exception
	{
		readSensors();
		// rightSensor = rightSensor();
		if (frontSensor < wallDistance)
		{
			ra.goForward(100, 25);
			ra.goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			// goForward(defaultSpeed, cellSize);
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
				readSensors();
				backwardAlign();
			}
		}
	}

	public void goSouth() throws Exception
	{
		readSensors();
		// rightSensor = rightSensor();
		if (frontSensor < wallDistance)
		{
			ra.goForward(100, 25);
			ra.goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			// goForward(defaultSpeed, cellSize);
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
				readSensors();
				backwardAlign();
			}
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void goEast() throws Exception
	{
		readSensors();
		if (frontSensor < wallDistance)
		{
			ra.goForward(100, 25);
			ra.goBackward(defaultSpeed, 18); // 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == EAST)
		{
			// goForward(defaultSpeed, cellSize);
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
				readSensors();
				backwardAlign();
			}
		}
		if (gridSquares2.get(counter).getDirection() == SOUTH)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void readSensors() throws Exception
	{
		ra.sleep(250);
		sonar.read();
		frontSensor = sonar.getFrontDistance();
		leftSensor = sonar.getLeftDistance();
		rightSensor = sonar.getRightDistance();
		rearSensor = sonar.getRearDistance();
		ra.sleep(250);
	}

	public void goWest() throws Exception
	{
		readSensors();
		if (frontSensor < wallDistance)
		{
			ra.goForward(100, 25);
			ra.goBackward(defaultSpeed, 18); // Test 20, previous 19
			readSensors();
		}
		if (gridSquares2.get(counter).getDirection() == WEST)
		{
			// goForward(defaultSpeed, cellSize);
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
				readSensors();
				backwardAlign();
			}
		}
		if (gridSquares2.get(counter).getDirection() == NORTH)
		{
			spinLeft2(defaultSpeed, 90);
		}
	}

	public void spinRight2(int speed, double degrees) throws Exception
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
		double pulses = degrees * ra.degreesRightX;
		for (int i = 0; i < pulses; i++)
		{
			ra.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void spinLeft2(int speed, double degrees) throws Exception
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
		double pulses = degrees * ra.degreesLeftX;
		for (int i = 0; i < pulses; i++)
		{
			ra.sleep(1000 / speed);
			l.rightMotorClock.write(true);
			l.rightMotorClock.write(false);
			l.leftMotorClock.write(true);
			l.leftMotorClock.write(false);
		}
	}

	public void goSuperStraight(int speed) throws Exception
	{
		sonar.read();
		if (sonar.getLeftDistance() > sonar.getRightDistance() && sonar.getLeftDistance() <= wallDistance)
		{
			spinLeft(defaultSpeed, 5);
		} else if (sonar.getLeftDistance() < sonar.getRightDistance() && sonar.getLeftDistance() <= wallDistance)
		{
			spinRight(defaultSpeed, 5);
		}
	}

	public void followWallLeftOneCell(int speed, int distance) throws Exception
	{
		sonar.read();
		int prevDistance = sonar.getLeftDistance();
		ra.goForward(speed, 5);
		for (int i = 0; i < 15; i++)
		{
			sonar.read();
			if (sonar.getLeftDistance() > prevDistance && sonar.getLeftDistance() > distance && sonar.getLeftDistance() < wallDistance && prevDistance < wallDistance)
			{
				prevDistance = sonar.getLeftDistance();
				ra.spinLeft(speed, 5);
				ra.goForward(speed, 5);
			} else if (sonar.getLeftDistance() < prevDistance && sonar.getLeftDistance() < distance && sonar.getLeftDistance() < wallDistance && prevDistance < wallDistance)
			{
				prevDistance = sonar.getLeftDistance();
				ra.spinRight(speed, 5);
				ra.goForward(speed, 5);
			} else
			{
				prevDistance = sonar.getLeftDistance();
				ra.goForward(speed, 5);
			}
		}
	}
}
