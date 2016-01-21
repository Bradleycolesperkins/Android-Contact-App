package com.example.phonebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 
 * @author Bradley Coles-Perkins - 642518
 * @version 1.0
 * DisplayUsersActivy class
 * This class extends the 'Activity' library, it connects with the DatabaseContent 
 * (database) class, and populates the listview with contacts stored on the SQL database
 * It also contains a button called "AddContactButton" that when pressed
 * sends the user to the "AddUserActivity" class
 */
public class DisplayUsersActivity extends Activity {
	// Create instances of variables access globally to all classes within
	// The 'DisplayUsersActivity
	DatabaseContent databaseContent;  // Creates an instance of the 'DatabaseContent' class
	
	public static long idDB;
	public static String name;
	public static String mobilenumber;
	public static String emailaddress;
	public static String addresslineone;
	public static String addresslinetwo;
	public static String county;
	public static String postcode;
	public static String dob;
	public static String extra;
	
		/** 
	* The onCreate gets called when the activity starts, It opens up a database connection
 	* and then populates the ListView with contacts, whilst also creating an instance of
	* an AdapterView to register user interaction
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_users);
		openDB();
		populateTheListView();
		handleUserInteraction();
	}
	
	/**
	 * The 'onCreateOptionsMenu' method, used to create the menu display for the current
	 * activity calling the XML file
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_users, menu);
		return true;
	}
	
	/**
	 * The 'onOptionsItemSelected' method, used to start a new activity when the user 
	 * selects the 'AddContactButton' 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.AddContactButton:
		    Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
		}
		return true;
	}
	
	/**
	 * The 'onDestroy' method, this gets called when the user closes the application,
	 * When this gets called it safely closes the connection of the database
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}

	/**
	 * The 'openDB' method that creates an object of the 'DatabaseContent' class
	 * It then calls the open method to create an instance of the SQL database
	 */
	private void openDB() {
		databaseContent = new DatabaseContent(this);
		databaseContent.open();
	}
	
	/**
	 * The 'closeDB' method used to close an instance of the SQL database
	 */
	private void closeDB() {
		databaseContent.close();
	}
	
	/**
	 * The 'populateTheListView' class, this is used to iterate through each
	 * row in the database and add them to the ListLivew 
	 */
	private void populateTheListView() {
		
		Cursor cursor = databaseContent.getAllRows(); // stores all the rows of SQL database in a cursor object
		
		startManagingCursor(cursor);
		
		// Arrays for each user, adds the name and mobile numbers to be displayed on the listview
		String[] fromFieldNames = new String[] {DatabaseContent.KEY_NAME, DatabaseContent.KEY_MOBILE_NUMBER};
		int[] toViewIDs = new int[]{ R.id.textViewNameLayout, R.id.textViewNumberLayout};
		
		// Add each user to the cursorAdapter
		SimpleCursorAdapter cursorAdapter = 
				new SimpleCursorAdapter(
						this,		//
						R.layout.listviewlayout,	// Row layout template
						cursor,				
						fromFieldNames,			
						toViewIDs				
						);
		
		// Add the cursorAdapter to the ListView
		ListView setListView = (ListView) findViewById(R.id.listview);
		setListView.setAdapter(cursorAdapter);
		
	}
	
	/**
	 * The 'handleUserInteraction', used for getting the position of the ListView selected
	 */
	private void handleUserInteraction() {
		// Create an instance of an intent to send the user to the 'ViewContactActivity' class
		final Intent viewerIntent = new Intent(this, ViewContactActivity.class);
		ListView listView = (ListView) findViewById(R.id.listview);
		
		// Create the 'setOnItemClickListener' method to listen when the user presses on the screen
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// Create the 'onItemClick' method to get details of the position pressed on the screen
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, 
					int position, long idPosition) {
				// Using the idPosition long to retrieve the details of the user with
				// that ID in the database
				Cursor cursor = databaseContent.getRow(idPosition);
				if (cursor.moveToFirst()) {
					idDB = cursor.getLong(DatabaseContent.COL_ROWID);
					name = cursor.getString(DatabaseContent.COL_NAME);
					mobilenumber = cursor.getString(DatabaseContent.COL_MOBILE_NUMBER);
					emailaddress = cursor.getString(DatabaseContent.COL_EMAIL_ADDRESS);
					addresslineone = cursor.getString(DatabaseContent.COL_ADDRESS_LINE_ONE);
					addresslinetwo = cursor.getString(DatabaseContent.COL_ADDRESS_LINE_TWO);
					county = cursor.getString(DatabaseContent.COL_COUNTY);
					postcode = cursor.getString(DatabaseContent.COL_POSTCODE);
					dob = cursor.getString(DatabaseContent.COL_DOB);
					extra = cursor.getString(DatabaseContent.COL_EXTRA);
				}
				
				// Set the object details of the user pressed within the database class
				// This then gets accessed when displaying the user and then editing the user
				databaseContent.setViewText(idDB, name, mobilenumber, emailaddress, addresslineone, addresslinetwo, county, postcode, dob, extra);
		
				cursor.close(); // Close the database connection once the data is retrieved
				startActivity(viewerIntent); // pass the user onto the 'ViewContactActivy'
			}
		});
		
	}

}
