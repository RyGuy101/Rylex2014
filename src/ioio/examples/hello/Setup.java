package ioio.examples.hello;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.os.Build;

public class Setup extends ActionBarActivity
{
	RadioButton urban;
	RadioButton gold;
	RadioButton drag;
	RadioButton test;
	public static final String CHALLENGE = "challenge";
	public static final String URBAN = "urban";
	public static final String GOLD = "gold";
	public static final String DRAG = "drag";
	public static final String TEST = "test";
	private String theChallenge;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		urban = (RadioButton) findViewById(R.id.urban);
		gold = (RadioButton) findViewById(R.id.gold);
		drag = (RadioButton) findViewById(R.id.drag);
		test = (RadioButton) findViewById(R.id.test);
	}

	public void onRadioButtonClicked(View v)
	{
		if (v.equals(urban))
		{
			theChallenge = URBAN;
		} else if (v.equals(gold))
		{
			theChallenge = GOLD;
		} else if (v.equals(drag))
		{
			theChallenge = DRAG;
		} else if (v.equals(test))
		{
			theChallenge = TEST;
		}
	}

	public void start(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(CHALLENGE, theChallenge);
		startActivity(intent);
	}
}
