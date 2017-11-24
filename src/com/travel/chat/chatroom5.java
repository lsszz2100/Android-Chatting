package com.travel.chat;

import java.net.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.unclouded.android.*;

public class chatroom5 extends Activity {
	
	// Unclouded event loop
	private Unclouded unclouded;
	
	// Flag to indicate whether device is connected to the network or not
	private boolean isOnline;
	
	// Name that is entered in the splash activity
	private String myName;
	
	// Adapter to update the list view with string messages
	private ArrayAdapter<String> conversationArrayAdapter;
	
	// List of remote references to other devices in the network
	private ArrayList<RemoteReference> buddyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatroom5);
		
		buddyList = new ArrayList<RemoteReference>();
		isOnline = false;
		
		Intent intent = getIntent();
		myName = intent.getStringExtra("NAME");
		
		ListView conversationView = (ListView) findViewById(R.id.conversation_view);
		Button sendButton = (Button) findViewById(R.id.send_button);
		
		conversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		conversationView.setAdapter(conversationArrayAdapter);
		// Make listView to scroll down automatically
		conversationView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		
		// When entering a message, clear the field and broadcast the message
		sendButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				EditText msgField = (EditText) findViewById(R.id.msg_field);
				String msg = msgField.getText().toString();
				msgField.setText("");
				broadcastMessage(msg);
			}
			
		});
		
		// Obtain Unclouded event loop
		unclouded = Unclouded.getInstance();
		
		// Go online and connected to the network
		Network network = unclouded.goOnline();
		
		// Monitor the network connection to update the isOnline flag
		network.whenever(new NetworkListener(){
			
			@Override
			public void isOnline(InetAddress ip){
				isOnline = true;
			}
				
			@Override
			public void isOffline(InetAddress ip){
				isOnline = false;
				buddyList.clear();
			}
				
		});
		
		// MESSENGER type tag to associate with the messenger service 
		TypeTag MESSENGER_TYPETAG = new TypeTag("MESSENGER");
		
		// New instance of the Messenger class.
		Messenger myMessenger = new Messenger();
		
		// myMessenger is broadcasted by reference (because Messenger implements UObject)
		// This makes a remote reference to this object to be spread across the network
		unclouded.broadcast(MESSENGER_TYPETAG, myMessenger);
		
		// Listen for remote reference associated with the MESSENGER_TYPETAG type tag
		unclouded.whenever(MESSENGER_TYPETAG, new ServiceListener<RemoteReference>(){
			
			String buddyName;

			@Override
			public void isDiscovered(RemoteReference remoteReference) {
				// When discovering a buddy, register the remote reference to its Messenger object
				buddyList.add(remoteReference);
				// Asynchronously ask for his name
				Promise promise = remoteReference.asyncInvoke("getName");
				// Listen for the name to be returned
				promise.when(new PromiseListener<String>(){

					@Override
					public void isResolved(String name) {
						// When name is returned, store it and print message on the screen
						buddyName = name;
						printBuddyJoinedMessage(name);	
					}
				});
			}
			
			@Override
			public void isDisconnected(RemoteReference remoteReference){
				// When disconnected, remove buddy from list
				buddyList.remove(remoteReference);
				// If name is already resolved, print disconnection message on the screen
				if(buddyName != null){ // Null in case disconnection occurs before name is resolved
					printBuddyDisconnectedMessage(buddyName);	
				}
			}
			
			@Override 
			public void isReconnected(RemoteReference remoteReference){
				// When reconnecting, check whether name has been resolved before
				if(buddyName == null){
					// If not, treat like a new connection
					isDiscovered(remoteReference);
				} else {
					// Otherwise, add reference to list and print message on the screen
					buddyList.add(remoteReference);
					printBuddyJoinedMessage(buddyName);	
				}
			}
			
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messenger, menu);
		return true;
	}
	
	@Override
	// Dynamically change menu depending on network status
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.clear();
	    getMenuInflater().inflate(R.menu.messenger, menu);
	    MenuItem item = menu.findItem(R.id.action_change_network_status);
	    if(isOnline){
	    	// If connected to the network, show `go offline' action
	    	item.setTitle(R.string.action_go_offline);
	    } else {
	    	// If disconnected from the network, show `go online' action
	    	item.setTitle(R.string.action_go_online);	
	    }  
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_change_network_status:
	        	if(isOnline){
	        		// When clicked and network is online, go offline
	        		unclouded.goOffline();	        		
	        	} else {
	        		// When clicked and network is offline, go online
	        		unclouded.goOnline();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	// Broadcast a message in the network
	private void broadcastMessage(String msg){
		// Loop over all discovered buddies...
		for(RemoteReference reference: buddyList){
			// ... and asynchronously invoke their receiveMsg method;
			// No need to wait for return value here
			reference.asyncInvoke("receiveMsg", myName, msg);
		}
		// Print message to the screen
		printMessage(myName, msg);
	}
	
	private void printMessage(final String name, final String msg){
		addToAdapter(name+": "+msg);
	}
	
	
	private void printBuddyJoinedMessage(final String name){
		addToAdapter(name+" 대화방에 입장하셨습니다.");
	}
	
	private void printBuddyDisconnectedMessage(final String name){
		addToAdapter(name+" 대화방을 나가셨습니다.");
	}
	
	// Main method to print something on the screen
	private void addToAdapter(final String msg){
		// Necessary because most invocations are initiated by the Unclouded event loop
		runOnUiThread(new Runnable(){
			public void run(){
				conversationArrayAdapter.add(msg);
			}	
		});	
	}
	
	
	// ------------------------------------------
	
	// Instances of the Messenger class are spread across the network
	// and allow other devices to share messages
	protected class Messenger implements UObject {
		
		public String getName(){ return myName; }
		
		public void receiveMsg(String name, String msg){
			printMessage(name, msg);
		}
		
	}
	
}
