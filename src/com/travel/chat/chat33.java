package com.travel.chat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class chat33 extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat3);
	
		Button button = (Button) findViewById(R.id.enter_button);
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				EditText nameField = (EditText) findViewById(R.id.name_field);
				String name = nameField.getText().toString();
				startMessenger(name);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	private void startMessenger(String name){
		Intent intent = new Intent(this, chatroom5.class);
		intent.putExtra("NAME", name);
		startActivity(intent);
		finish();
	}

}
