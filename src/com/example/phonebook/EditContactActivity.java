package com.example.phonebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Bradley Coles-Perkins - 642518
 * @version 1.0
 * EditContactActivity class
 * This class contains methods that get the users input
 * and then update the user within the database
 */
public class EditContactActivity extends Activity {
	
	// Variables that store the users' details so both methods can access these 
	public static long rowId;
	public static String name;
	public static String mobilenumber;
	public static String emailaddress;
	public static String addresslineone;
	public static String addresslinetwo;
	public static String county;
	public static String postcode;
	public static String dob;
	public static String extra;
	
	// Create the instances of the EditText objects
	public static EditText txtname;
	public static EditText txtmobilenumber;
	public static EditText txtemailaddress;
	public static EditText txtaddresslineone;
	public static EditText txtaddresslinetwo;
	public static EditText txtcounty;
	public static EditText txtpostcode;
	public static EditText txtdob;
	public static EditText txtextra;
	
	// Create the 'DatabaseContent' object
	DatabaseContent databaseContent = new DatabaseContent(this);
	
	/** 
	* The onCreate gets called when the activity starts, it connected the instances of the
	* EditText objects with the ID's from the R.java file
	* Then gets the input from them and stores them in the variables within the header of the main class 
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		
		// Retrieve the original values of the user details, using the
		// get methods from the database class
		rowId = databaseContent.getViewTextID();
		name = databaseContent.getViewTextName();
		mobilenumber = databaseContent.getViewTextMobileNumber();
		emailaddress = databaseContent.getViewTextEmailAddress();
		addresslineone = databaseContent.getViewTextAddressLineOne();
		addresslinetwo = databaseContent.getViewTextAddressLineTwo();
		county = databaseContent.getViewTextCounty();
		postcode = databaseContent.getViewTextPostcode();
		dob = databaseContent.getViewTextDob();
		extra = databaseContent.getViewTextExtra();
		
		// Apply the EditText objects with the right ID's
		txtname = (EditText)findViewById(R.id.editNameTitle);
		txtmobilenumber = (EditText)findViewById(R.id.editnumberTitle);
		txtemailaddress = (EditText)findViewById(R.id.editemailTitle);
		txtaddresslineone = (EditText)findViewById(R.id.editaddressOneTitle);
		txtaddresslinetwo = (EditText)findViewById(R.id.editaddressTwoTitle);
		txtcounty = (EditText)findViewById(R.id.editcountyTitle);
		txtpostcode = (EditText)findViewById(R.id.editpostcodeTitle);
		txtdob = (EditText)findViewById(R.id.editdobTitle);
		txtextra = (EditText)findViewById(R.id.editextraTitle);
		
		// Setting the text of the users details retrieved from the database class
		txtname.setText(name);
		txtmobilenumber.setText(mobilenumber);
		txtemailaddress.setText(emailaddress);
		txtaddresslineone.setText(addresslineone);
		txtaddresslinetwo.setText(addresslinetwo);
		txtcounty.setText(county);
		txtpostcode.setText(postcode);
		txtdob.setText(dob);
		txtextra.setText(extra);
		
	}

	/**
	 * The 'onCreateOptionsMenu' method, used to create the menu display for the current
	 * activity calling the XML file
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}

	/**
	 * The 'onOptionsItemSelected' method, used to start a new activity when the user 
	 * selects the 'cancelEditButton' or the 'confirmEditButton; 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			// Case statement when the 'CancelEditButton' is called. It sends the user
			// back to the previous activity which is the 'ViewContactActivity'
			case R.id.CancelEditButton:
				final Intent returnToEditIntent = new Intent(this, ViewContactActivity.class);
				startActivity(returnToEditIntent);
				break;
				// Case statement when the 'ConfirmEditButton' is called. It sends the user
				// back to the previous activity which is the 'ViewContactActivity' if the
				// details are validated correctly. It also updates the users database row with
				// the inputted details
			case R.id.ConfirmEditButton:				
				int nameEntered = txtname.getText().toString().length(); // get the length the entered name
				int mobileEntered = txtmobilenumber.getText().toString().length(); // get the length the entered mobile number
				
				// If the user has not entered a legitimate mobile number, which has to be greater
				// that 10 digits AND not entered a name
				if(nameEntered == 0 && mobileEntered < 10){
					Toast.makeText(EditContactActivity.this, "No Name and Mobile Number entered", Toast.LENGTH_LONG).show();
					break;
				// If the user has not entered a name 
				} else if (nameEntered == 0){
					Toast.makeText(EditContactActivity.this, "Error, no name entered", Toast.LENGTH_LONG).show();
					break;
				// If the user has not entered a mobile number greater than 10
				} else if (mobileEntered < 10) {
					Toast.makeText(EditContactActivity.this, "Error, no Mobile Number entered", Toast.LENGTH_LONG).show();
					break; 
				// If the user has not entered a Date of birth in the correct format
				} else if (!(txtdob.getText().toString().substring(2,3).equals("/") && txtdob.getText().toString().substring(5,6).equals("/"))) {
					Toast.makeText(EditContactActivity.this, "Wrong format of date of birth", Toast.LENGTH_LONG).show();
					break;
				} else {
					// If the user has left the Date of birth blank
					if (txtdob.getText().toString().equals("DD/MM/YYYY")){
						DatabaseContent databaseContent = new DatabaseContent(this);
						databaseContent.open();
						databaseContent.updateRow(rowId, txtname.getText().toString(), txtmobilenumber.getText().toString(), txtemailaddress.getText().toString(), txtaddresslineone.getText().toString(), txtaddresslinetwo.getText().toString(), txtcounty.getText().toString(), txtpostcode.getText().toString(), txtdob.getText().toString(), txtextra.getText().toString());
						databaseContent.close();
						final Intent returnToViewIntent = new Intent(this, DisplayUsersActivity.class);
						startActivity(returnToViewIntent);
						break;
					}
					// Otherwise apply the correct date of birth
					DatabaseContent databaseContent = new DatabaseContent(this);
					databaseContent.open();
					databaseContent.updateRow(rowId, txtname.getText().toString(), txtmobilenumber.getText().toString(), txtemailaddress.getText().toString(), txtaddresslineone.getText().toString(), txtaddresslinetwo.getText().toString(), txtcounty.getText().toString(), txtpostcode.getText().toString(), txtdob.getText().toString(), txtextra.getText().toString());
					databaseContent.close();
					final Intent returnToViewIntent = new Intent(this, DisplayUsersActivity.class);
					startActivity(returnToViewIntent);
					break;
				}
		}
		return true;
	}
}

