package com.travel.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.travel.chat.R;


import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class lee extends Activity implements OnClickListener {

	public String SERVER_IP = "172.30.1.25";
	public static int SERVER_PORT = 15464;

	Button sendButton;
	Button controlNext;
	Button controlPrevious;
	Button controlEsc;
	EditText inputField;
	TextView textView;

	String name;
	
	public String controlMessage;
	
	Socket socket;
	DataInputStream input;
	DataOutputStream output;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lee);

		sendButton = (Button) findViewById(R.id.sendButton);
		controlNext = (Button) findViewById(R.id.controlNext);
		controlPrevious = (Button) findViewById(R.id.controlPrevious);
		controlEsc = (Button) findViewById(R.id.controlEsc);
		inputField = (EditText) findViewById(R.id.inputField);
		textView = (TextView) findViewById(R.id.textView);
		
		sendButton.setOnClickListener(this);
		controlNext.setOnClickListener(this);
		controlPrevious.setOnClickListener(this);
		controlEsc.setOnClickListener(this);
		
		intro();
	}
	
	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event)
	  {
	   
	   if(keyCode == KeyEvent.KEYCODE_BACK)
	   {
	    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
	      
	    alertDlg.setMessage("뒤로 가시겠습니까?");
	    alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener() { //확인 버튼
	     public void onClick(DialogInterface dialog, int whichButton) {
	      
	    	 System.exit(0); 
	     
	     }
	      }) ;        
	      alertDlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() { // 취소 버튼
	       public void onClick(DialogInterface dialog, int whichButton) {         
	       
	        dialog.cancel();   
	       }
	      });
	      AlertDialog alert = alertDlg.create();
	      alert.setTitle("뒤로가기버튼"); //알림창 상단 타이틀
	      alert.setIcon(R.drawable.ic_launcher); // 알림창에 이미지 삽입
	      alert.show();
	   }
	   return false;
	  }
	@Override
	public void onDestroy() {
		try {
			socket.close();
			input.close();
			output.close();
		} catch (IOException e) {
		}
	}

	public void intro() {
		print("type IP -> sned -> set ID-> send");
	}

	public void print(Object message) {
		textView.append(message + "\n");
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sendButton:

			if (inputField.getText().toString() == "")
				return;
			if (SERVER_IP==null){
				String text = inputField.getText().toString();
				inputField.setText("");
				SERVER_IP=text;
				return;
			}
			if (name == null) {
				String text = inputField.getText().toString();
				inputField.setText("");
				name = text;
				connect();
			} else {
				String text = inputField.getText().toString();
				inputField.setText("");

				try {
					output.writeUTF("c`" + text);
					output.flush();
				} catch (IOException e) {
					print("chatting error1");
				}

			}
			break;
			
		case R.id.controlNext:
			controlMessage = "abcd";
			try {
				output.writeUTF("m`" + controlMessage);
				output.flush();
			} catch (IOException e) {
				print("chatting error2");
			}
			break;
		case R.id.controlPrevious: 
			controlMessage = "bcda";
			try {
				output.writeUTF("m`" + controlMessage);
				output.flush();
			} catch (IOException e) {
				print("chatting error3");
			}
			break;
		case R.id.controlEsc: 
			controlMessage = "cdab";
			try {
				output.writeUTF("m`" + controlMessage);
				output.flush();
			} catch (IOException e) {
				print("chatting error4");
			}
			break;
			
		}
		
		
	}

	public void connect() {
		try {

			print(SERVER_IP + ":" + SERVER_PORT + "type");

			socket = new Socket(SERVER_IP, SERVER_PORT);

			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			while (socket != null) {
				if (socket.isConnected()) {
					output.writeUTF("r`1`1`" + name + "`");
					output.flush();
					break;
				}
			}

			MessageReciver messageReceiver = new MessageReciver();
			messageReceiver.start();

		} catch (Exception e) {
			print("������ ������ �� �����ϴ�.");
			this.finish();

		}

	}

	public String chatMessage;

	public class MessageReciver extends Thread {
		public void run() {
			try {
				String received;

				while ((received = input.readUTF()) != null) {

					String[] buffer = received.split("`");

					switch (buffer[0].charAt(0)) {
					case 'n':
						chatMessage = "�ڡڡ�" + buffer[1] + "�ڡڡ�";

						break;
					case 'c':
						chatMessage = buffer[1] + ": " + buffer[2];
						break;
					case 'x':
						chatMessage = "�١١�" + buffer[1] + "�١١�";
						break;
					}

					Message message = handler.obtainMessage(1, received);

					handler.sendMessage(message);

				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(Message message) {
			super.handleMessage(message);

			print(chatMessage);

		}
	};

}
