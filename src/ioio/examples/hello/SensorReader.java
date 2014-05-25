package ioio.examples.hello;

import ioio.lib.api.exception.ConnectionLostException;

public class SensorReader implements Runnable
{
	private boolean read;
	private UltraSonicSensor sonar;

	public SensorReader(UltraSonicSensor sonar, boolean read)
	{
		this.sonar = sonar;
		this.read = read;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (read)
			{
				try
				{
					sonar.read();
					Thread.sleep(60);
				} catch (ConnectionLostException e)
				{
					e.printStackTrace();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void setRead(boolean read)
	{
		this.read = read;
	}

	public boolean getRead()
	{
		return read;
	}
}
