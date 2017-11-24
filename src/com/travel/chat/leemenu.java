package com.travel.chat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;

public class leemenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leemenu);
	}
	
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		
		Toast.makeText(getApplicationContext(), "백버튼", Toast.LENGTH_SHORT).show();
	} */
	
	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event)
	  {
	   
	   if(keyCode == KeyEvent.KEYCODE_BACK)
	   {
	    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
	      
	    alertDlg.setMessage("종료 하시겠습니까?");
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
	      alert.setTitle("종료대화창"); //알림창 상단 타이틀
	      alert.setIcon(R.drawable.ic_launcher); // 알림창에 이미지 삽입
	      alert.show();
	   }
	   return false;
	  }




	public void myListener2(View target) {
		Intent intent = new Intent(getApplicationContext(), chat33.class);
		startActivity(intent);
	}

	public void myListener3(View target) {
		Intent intent = new Intent(getApplicationContext(), network4.class);
		startActivity(intent);
	}
	public void myListener4(View target) {
		Intent intent = new Intent(getApplicationContext(), networkcon6.class);
		startActivity(intent);
	}
	
	public void myListener5(View target) {
		Intent intent = new Intent(getApplicationContext(), lee.class);
		startActivity(intent);
	}
}