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
	RadioButton urbanRight;
	RadioButton gold;
	RadioButton drag;
	RadioButton test;
	RadioButton testUltra;
	CheckBox hazFenderz;
	public static final String CHALLENGE = "challenge";
	public static final String URBAN = "urban - left";
	public static final String URBAN_RIGHT = "urban - right";
	public static final String GOLD = "gold";
	public static final String DRAG = "drag";
	public static final String TEST = "test";
	public static final String TEST_ULTRA = "testUltra";
	public static final String FENDERZ = "fenderz";
	private String theChallenge;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		urban = (RadioButton) findViewById(R.id.urban);
		urbanRight = (RadioButton) findViewById(R.id.urbanRight);
		gold = (RadioButton) findViewById(R.id.gold);
		drag = (RadioButton) findViewById(R.id.drag);
		test = (RadioButton) findViewById(R.id.test);
		testUltra = (RadioButton) findViewById(R.id.testUltra);
		hazFenderz = (CheckBox) findViewById(R.id.hazFenderz);
		urban.setChecked(true);
		theChallenge = URBAN;
		hazFenderz.setChecked(true);
	}

	public void onRadioButtonClicked(View v)
	{
		if (v.equals(urban))
		{
			theChallenge = URBAN;
		} else if (v.equals(urbanRight))
		{
			theChallenge = URBAN_RIGHT;
		} else if (v.equals(gold))
		{
			theChallenge = GOLD;
		} else if (v.equals(drag))
		{
			theChallenge = DRAG;
		} else if (v.equals(test))
		{
			theChallenge = TEST;
		} else if (v.equals(testUltra))
		{
			theChallenge = TEST_ULTRA;
		}
	}

	public void start(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(CHALLENGE, theChallenge);
		intent.putExtra(FENDERZ, hazFenderz.isChecked());
		startActivity(intent);
	}
	
	public void crash() {
		throw new NullPointerException("Crashing upon the user's request");
	}
}
