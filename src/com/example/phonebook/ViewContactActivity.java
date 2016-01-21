package com.example.phonebook;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Bradley Coles-Perkins - 642518
 * @version 1.0
 * ViewContactActivity class
 * This class extends the 'Activity' library, it connects with the DatabaseContent 
 * and displays the users details as well as calculating the age
 */
public class ViewContactActivity extends Activity {
	// variable created that allows both method to access the ID row of the user
	public static long rowId;
	
	/** 
	* The onCreate gets called when the activity starts, it connected the instances of the
	* EditText objects with the ID's from the R.java file. It then displays the details
	* of the user using the get methods from the database class 
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_contact); // Set the XML layout file
		
		// Create the 'DatabaseContent' object
		DatabaseContent databaseContent = new DatabaseContent(this); 
		
		// retrieve the row ID and store it in the variable in the header of the main class
		rowId = databaseContent.getViewTextID();
		
		// Apply the EditText objects with the right ID's
		TextView txtname = (TextView)findViewById(R.id.viewNameTitle);
		TextView txtmobilenumber = (TextView)findViewById(R.id.viewnumberTitle);
		TextView txtemailaddress = (TextView)findViewById(R.id.viewemailTitle);
		TextView txtaddresslineone = (TextView)findViewById(R.id.viewaddressOneTitle);
		TextView txtaddresslinetwo = (TextView)findViewById(R.id.viewaddressTwoTitle);
		TextView txtcounty = (TextView)findViewById(R.id.viewcountyTitle);
		TextView txtpostcode = (TextView)findViewById(R.id.viewpostcodeTitle);
		TextView txtdob = (TextView)findViewById(R.id.viewdobTitle);
		TextView txtage = (TextView)findViewById(R.id.viewAgeTitle);
		TextView txtextra = (TextView)findViewById(R.id.viewExtraTitle);

		// Setting the text of the users details retrieved from the database class
		txtname.setText(databaseContent.getViewTextName());
		txtmobilenumber.setText(databaseContent.getViewTextMobileNumber());
		txtemailaddress.setText(databaseContent.getViewTextEmailAddress());
		txtaddresslineone.setText(databaseContent.getViewTextAddressLineOne());
		txtaddresslinetwo.setText(databaseContent.getViewTextAddressLineTwo());
		txtcounty.setText(databaseContent.getViewTextCounty());
		txtpostcode.setText(databaseContent.getViewTextPostcode());
		txtdob.setText(databaseContent.getViewTextDob());
		txtextra.setText(databaseContent.getViewTextExtra());
		
		// retrieve the date of birth string for the database class 
		String usersDob = (String) txtdob.getText();
		
		// If the string is in the correct format, i.e. forward slashes in positions 2-3 and 5-6
		if ((usersDob.substring(2,3).equals("/")) && (usersDob.substring(5,6).equals("/"))){
			// substring the day, month and year -
			int day = Integer.parseInt(usersDob.substring(0,2));
			Log.d("Day",String.valueOf(day));
	        int month = Integer.parseInt(usersDob.substring(3,5));
	        Log.d("month",String.valueOf(month));
	        int year = Integer.parseInt(usersDob.substring(6,10));
	        Log.d("year",String.valueOf(year));
			
			// create a calender object
	        GregorianCalendar calender = new GregorianCalendar();
	        // store the days, month, and year
	        int currentYear = calender.get(Calendar.YEAR);
	        int currentMonth = calender.get(Calendar.MONTH); 
	        int currentDay = calender.get(Calendar.DAY_OF_MONTH);
	        
	        // Calculate the by negating the users birthday from the current date
		       int age = currentYear - year;
		
		      if(currentMonth < month ){
		    	  age--;
		      }
		
		      if(month <= currentMonth && currentDay < day){
		    	  age--;
		      }
		      
		      if(age >= 0 && age <= 100) {
		        	txtage.setText(String.valueOf(age));
		      } else {
		        	txtage.setText("Error computing age");
		      } 
		// If the date of birth is in the wrong format, display an error message
		} else {
			txtage.setText("Format Error");
		}
		
	}

	/**
	 * The 'onCreateOptionsMenu' method, used to create the menu display for the current
	 * activity calling the XML file
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_contact, menu);
		return true;
	}
	 
	/**
	 * The 'onOptionsItemSelected' method, used to start a new activity when the user 
	 * selects the 'deleteContactButton' or the 'editContactButton; 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DatabaseContent databaseContent = new DatabaseContent(this);
		switch(item.getItemId()){
			// Case statement when the 'deleteContactButton' is called. It sends the user
			// back to the previous activity which is the 'DisplayUsersActivity' and deletes
			// the user from the database
			case R.id.deleteContactButton:
				databaseContent.open(); // open the database connection
				databaseContent.deleteRow(rowId); // pass the rowID to the database and remove it
				databaseContent.close(); // close the database connection
				final Intent deleteConIntent = new Intent(this, DisplayUsersActivity.class);
				startActivity(deleteConIntent); // send the user back to the main menu
				break;
			// Case statement when the 'editContactButton' is called. It sends the user
			// to the 'EditContactActivity' activity where they are able to edit the
				// user details
			case R.id.editContactButton:
				final Intent editConIntent = new Intent(this, EditContactActivity.class); 
				startActivity(editConIntent); // send the user to the edit user activity
				break;
		}
		return true;
	}
}
