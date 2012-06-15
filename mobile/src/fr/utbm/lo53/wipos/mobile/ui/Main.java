package fr.utbm.lo53.wipos.mobile.ui;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import fr.utbm.lo53.wipos.mobile.R;
import fr.utbm.lo53.wipos.mobile.net.Locate;
import fr.utbm.lo53.wipos.mobile.net.Measure;

@SuppressWarnings("deprecation")
public class Main extends Activity {
	private final static int cursW = 16;
	private final static int cursH = 40;
	
	private TextView cursor;
	private LinearLayout linLayout;
	private AbsoluteLayout absLayout;

	private Button bLocate, bCancel, bMesure;
	
	private Locate mLocate;
	private Measure mMeasure;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main);
		
		mLocate = new Locate();
		mMeasure = new Measure();
	}

	@Override
	public void onStart() {
		super.onStart();
		linLayout = (LinearLayout) findViewById(R.id.linearLayout);
		absLayout = ((AbsoluteLayout) findViewById(R.id.absLayout));
		bLocate = (Button) findViewById(R.id.locateButton);
		bCancel = (Button) findViewById(R.id.cancelButton);
		bMesure = (Button) findViewById(R.id.mesureButton);

		bCancel.setVisibility(View.GONE);
		bMesure.setVisibility(View.GONE);

		absLayout.setClickable(true);
		absLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (cursor != null) {
					absLayout.removeView(cursor);
				}
				cursor = new TextView(view.getContext());
				cursor.setText("+");
				cursor.setTextSize(40);
				cursor.setLayoutParams(new AbsoluteLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						(int) event.getX() - cursW, (int) event.getY() - cursH));
				((AbsoluteLayout) findViewById(R.id.absLayout)).addView(cursor);
				return false;
			}
		});
	}

	public void toggle(View view) {
		ToggleButton bToggle = (ToggleButton) view;
		if (bToggle.isChecked()) {
			bLocate.setVisibility(View.GONE);
			bCancel.setVisibility(View.VISIBLE);
			bMesure.setVisibility(View.VISIBLE);
		} else {
			bLocate.setVisibility(View.VISIBLE);
			bCancel.setVisibility(View.GONE);
			bMesure.setVisibility(View.GONE);
		}
	}

	public void locate(View view) {
		
		Long[] posL = new Long[2];
		try {
			posL = mLocate.execute((Void[]) null).get();
		} catch (InterruptedException e) {
			Log.e("Locate", "get ", e);
		} catch (ExecutionException e) {
			Log.e("Locate", "get ", e);
		}
		
		int[] pos = new int[2];
		pos[0] = (int) (long) posL[0];
		pos[1] = (int) (long) posL[1];
		
		
		if (cursor != null) {
			linLayout.removeView(cursor);
		}
		cursor = new TextView(view.getContext());
		cursor.setText("+");
		cursor.setTextSize(40);
		cursor.setLayoutParams(new AbsoluteLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, pos[0]-cursW, pos[1]-cursH));
		((AbsoluteLayout) findViewById(R.id.absLayout)).addView(cursor);
	}

	public void mesure(View view) {
		mMeasure.execute((long) ((AbsoluteLayout.LayoutParams) absLayout.getLayoutParams()).x, 
				(long) ((AbsoluteLayout.LayoutParams) absLayout.getLayoutParams()).y);
	}

	public void cancel(View view) {

	}
}
