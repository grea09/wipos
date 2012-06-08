package fr.utbm.lo53.wipos.mobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import fr.utbm.lo53.wipos.mobile.R;

public class Main extends Activity
{
	private TextView cursor;
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.main);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		cursor = (TextView) findViewById(R.id.cursor);
	}
	
	public void locate (View view) {
			
	}
	
	public void mesure (View view) {
		
	}
	
	public void cancel (View view) {
		
	}
}
