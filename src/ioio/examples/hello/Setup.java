package ioio.examples.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class Setup extends Activity
{
	RadioButton urban;
	RadioButton gold;
	RadioButton drag;
	RadioButton test;
	CheckBox hazFenderz;
	public static final String CHALLENGE = "challenge";
	public static final String URBAN = "urban";
	public static final String GOLD = "gold";
	public static final String DRAG = "drag";
	public static final String TEST = "test";
	public static final String FENDERZ = "fenderz";
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
		hazFenderz = (CheckBox) findViewById(R.id.hazFenderz);
		urban.setChecked(true);
		theChallenge = URBAN;
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
		intent.putExtra(FENDERZ, hazFenderz.isChecked());
		startActivity(intent);
	}
}
