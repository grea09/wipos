package fr.utbm.lo53.wipos.mobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import fr.utbm.lo53.wipos.mobile.R;
import fr.utbm.lo53.wipos.mobile.R.id;

public class Main extends Activity
{
	private TextView cursor;
	private ImageView map;
	
	private Button bLocate, bCancel, bMesure;
	
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
		map = (ImageView) findViewById(id.mapView);
		bLocate = (Button) findViewById(R.id.locateButton);
		bCancel = (Button) findViewById(R.id.cancelButton);
		bMesure = (Button) findViewById(R.id.mesureButton);

		bCancel.setVisibility(View.GONE);
		bMesure.setVisibility(View.GONE);
	}
	
	public void toggle (View view) {
		ToggleButton bToggle = (ToggleButton) view;
		if (bToggle.isChecked()) {
			bLocate.setVisibility(View.GONE);
			bCancel.setVisibility(View.VISIBLE);
			bMesure.setVisibility(View.VISIBLE);
		}
		else {
			bLocate.setVisibility(View.VISIBLE);
			bCancel.setVisibility(View.GONE);
			bMesure.setVisibility(View.GONE);
		}
	}
	
	public void locate (View view) {
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		
		MarginLayoutParams params = new MarginLayoutParams(
				mapWidth/2 - cursor.getWidth()/2, 
				mapHeight/2 - cursor.getHeight()/2);
		cursor.setLayoutParams(params);
	}
	
	public void mesure (View view) {
		
	}
	
	public void cancel (View view) {
		
	}
}
