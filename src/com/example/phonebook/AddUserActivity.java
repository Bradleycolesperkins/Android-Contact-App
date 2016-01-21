package com.example.phonebook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Bradley Coles-Perkins - 642518
 * @version 1.0
 * AddUserActivity class
 * This class extends the 'Activity' library, it connects with the DatabaseContent 
 * (database) class and allows users to enter details, with validations on the 
 * name, mobile number and date of birth fields. It then adds the details to
 * a new row within the SQL database in the database class
 */
public class AddUserActivity extends Activity {

	/** 
	* The onCreate gets called when the activity starts, it creates the layout of the
	* activity using the XML layout called 'activity_add_user 
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
	}

	/**
	 * The 'onCreateOptionsMenu' method, used to create the menu display for the current
	 * activity calling the XML file
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}
	
	/**
	 * The 'onOptionsItemSelected' method, used to start a new activity when the user 
	 * selects the 'ConfirmButton' or the 'CancelButton; 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			// Case statement when the 'confirmButton' is called. It sends the details
			// of the users input to the database and sends them back to the previous
			// activity
			case R.id.ConfirmButton:
			
			// Create the instances of the EditText objects and applying the EditText 
			// objects with the right ID's
			EditText name = (EditText)findViewById(R.id.name_textfield);
			EditText mobile = (EditText)findViewById(R.id.mobile_number_textfield);
			EditText emailaddress = (EditText)findViewById(R.id.email_address_textfield);
			EditText addresslineone = (EditText)findViewById(R.id.address_one_textfield);
			EditText addresslinetwo = (EditText)findViewById(R.id.address_two_textfield);
			EditText county = (EditText)findViewById(R.id.county_textfield);
			EditText postcode = (EditText)findViewById(R.id.postcode_textfield);
			EditText dob = (EditText)findViewById(R.id.dob_textfield);
			EditText extra = (EditText)findViewById(R.id.extra_textfield);
			
			
			int nameEntered = name.getText().toString().length(); // get the length the entered name
			int mobileEntered = mobile.getText().toString().length(); // get the length the entered mobile number
			
			// If the user has not entered a legitimate mobile number, which has to be greater
			// that 10 digits AND not entered a name
			if(nameEntered == 0 && mobileEntered < 10){
				Toast.makeText(AddUserActivity.this, "No Name and Mobile Number entered", Toast.LENGTH_LONG).show();
				break;
			// If the user has not entered a name 
			} else if (nameEntered == 0){
				Toast.makeText(AddUserActivity.this, "Error, no name entered", Toast.LENGTH_LONG).show();
				break;
			// If the user has not entered a mobile number greater than 10
			} else if (mobileEntered < 10) {
				Toast.makeText(AddUserActivity.this, "Error, no Mobile Number entered", Toast.LENGTH_LONG).show();
				break;
			// If the user has not entered a Date of birth in the correct format	
			} else if (!(dob.getText().toString().substring(2,3).equals("/") && dob.getText().toString().substring(5,6).equals("/"))) {
				Toast.makeText(AddUserActivity.this, "Wrong format of date of birth", Toast.LENGTH_LONG).show();
				break;
			} else {
				// If the user has left the Date of birth blank
				if (dob.getText().toString().equals("DD/MM/YYYY")){
					DatabaseContent databaseContent = new DatabaseContent(this);
					databaseContent.open();
					databaseContent.insertRow(name.getText().toString(), mobile.getText().toString(), emailaddress.getText().toString(), addresslineone.getText().toString(), addresslinetwo.getText().toString(), county.getText().toString(), postcode.getText().toString(), "00/00/0000", extra.getText().toString());
					databaseContent.close();
					this.finish();
					break;
				}
				// Otherwise apply the correct date of birth
				DatabaseContent databaseContent = new DatabaseContent(this);
				databaseContent.open();
				databaseContent.insertRow(name.getText().toString(), mobile.getText().toString(), emailaddress.getText().toString(), addresslineone.getText().toString(), addresslinetwo.getText().toString(), county.getText().toString(), postcode.getText().toString(), dob.getText().toString(), extra.getText().toString());
				databaseContent.close();
				this.finish();
				break;
			}
			
			// Case statement when the 'cancelButton' is called. It sends stops the current activity
			// sending the user back to the previous activity which is the displaying of the stored
			// database contacts
			case R.id.CancelButton:
			this.finish();	
		}
		return true;
	}
}
