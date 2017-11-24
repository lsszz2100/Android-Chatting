package com.travel.chat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Launcher extends Activity {
	
	private TextView editText1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		editText1 = (TextView)findViewById(R.id.editText1);
		editText1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Launcher.this, leemenu.class );
				startActivity(intent);
				
			}
			
		});
	
	
	new Handler().postDelayed(new Runnable()
	{
		public void run()
		{
			Intent intent = new Intent(Launcher.this, leemenu.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		}
	}
	, 3000);
	
}
	
	
	
	
	}
