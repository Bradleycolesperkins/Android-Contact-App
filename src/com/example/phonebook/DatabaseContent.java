package com.example.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Bradley Coles-Perkins - 642518
 * @version 1.0
 * DatabaseContent class
 * This class contains methods that create the SQL database
 * along with adding, deleting, editing methods
 */
public class DatabaseContent {
	// Instantiating variables
	private final Context context;
	private DatabaseHandler createDatabaseHandler;
	private SQLiteDatabase sqlDatabase;
	
	// Database column names :
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "Name";
	public static final String KEY_MOBILE_NUMBER = "Mobile_number";
	public static final String KEY_EMAIL_ADDRESS = "Email_address";
	public static final String KEY_ADDRESS_LINE_ONE = "Address_line_one";
	public static final String KEY_ADDRESS_LINE_TWO = "Address_line_two";
	public static final String KEY_COUNTY = "County";
	public static final String KEY_POSTCODE = "Postcode";
	public static final String KEY_DOB = "DOB";
	public static final String KEY_EXTRA = "Extra";
	
	// Database column ids :
	public static final int COL_ROWID = 0;
	public static final int COL_NAME = 1;
	public static final int COL_MOBILE_NUMBER = 2;
	public static final int COL_EMAIL_ADDRESS = 3;
	public static final int COL_ADDRESS_LINE_ONE = 4;
	public static final int COL_ADDRESS_LINE_TWO = 5;
	public static final int COL_COUNTY = 6;
	public static final int COL_POSTCODE = 7;
	public static final int COL_DOB = 8;
	public static final int COL_EXTRA = 9;
	
	// Variables for the get and set methods of the individual selected user can be acessed
	public static long viewid;
	public static String viewname;
	public static String viewmobilenumber;
	public static String viewemailaddress;
	public static String viewaddresslineone;
	public static String viewaddresslinetwo;
	public static String viewcounty;
	public static String viewpostcode;
	public static String viewdob;
	public static String viewextra;
	
	// Store a string of all the column rows, used for returning all the columns when getting a users
	// row from the database
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_MOBILE_NUMBER, KEY_EMAIL_ADDRESS, KEY_ADDRESS_LINE_ONE, KEY_ADDRESS_LINE_TWO, KEY_COUNTY, KEY_POSTCODE, KEY_DOB, KEY_EXTRA};
	
	// Declaring the database names and table and version
	public static final String DATABASE_NAME = "databaseName1";
	public static final String DATABASE_TABLE = "databaseTable1";
	public static final int DATABASE_VERSION = 17;	
	
	// The SQL command used to create a new database
	private static final String DATABASE_CREATE_SQL = 
	"create table " + DATABASE_TABLE + " (" + 
	KEY_ROWID + " integer primary key autoincrement, " + 
	KEY_NAME + " text not null, " + 
	KEY_MOBILE_NUMBER + " text not null,"+
	KEY_EMAIL_ADDRESS + " text,"+
	KEY_ADDRESS_LINE_ONE + " text," +
	KEY_ADDRESS_LINE_TWO + " text," +
	KEY_COUNTY + " text," +
	KEY_POSTCODE + " text," +
	KEY_DOB + " text," +
	KEY_EXTRA + " text)";
	
	public DatabaseContent(Context ctx) {
		this.context = ctx;
		createDatabaseHandler = new DatabaseHandler(context);
	}
	
	// Open the database connection.
	public DatabaseContent open() {
		sqlDatabase = createDatabaseHandler.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		createDatabaseHandler.close();
	}
	

	// The method called from the adding a new user class that insert the details
	// passed into the parameters 
	public long insertRow(String name, String mobilenumber, String emailaddress, String addresslineone, String addresslinetwo, String county, String postcode, String dob, String extra) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_MOBILE_NUMBER, mobilenumber);
		initialValues.put(KEY_EMAIL_ADDRESS, emailaddress);
		initialValues.put(KEY_ADDRESS_LINE_ONE, addresslineone);
		initialValues.put(KEY_ADDRESS_LINE_TWO, addresslinetwo);
		initialValues.put(KEY_COUNTY, county);
		initialValues.put(KEY_POSTCODE, postcode);
		initialValues.put(KEY_DOB, dob);
		initialValues.put(KEY_EXTRA, extra);

		return sqlDatabase.insert(DATABASE_TABLE, null, initialValues);
	}

	// The 'deleteRow' method, It uses the ID number for the row in the database, then deletes the 
	// instance of the row
	public boolean deleteRow(long rowId) {
		String findIdString = KEY_ROWID + "=" + rowId;
		return sqlDatabase.delete(DATABASE_TABLE, findIdString, null) != 0;
	}
	
	// A cursor method that returns all the rows in the database.
	// This method is used to populate the ListView in the 'DisplayUsersActivity' class
	public Cursor getAllRows() {
		Log.w("HERE", "Getting all rows");
		String findIdString = null;
		Cursor cursor = 	sqlDatabase.query(true, DATABASE_TABLE, ALL_KEYS, 
			findIdString, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	// A cursor method called 'getRow' that returns an individual row within the database
	// using the row ID 
	public Cursor getRow(long rowId) {
		String findIdString = KEY_ROWID + "=" + rowId;
		Cursor cursor = 	sqlDatabase.query(true, DATABASE_TABLE, ALL_KEYS, 
				findIdString, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	// The 'updateRow' method that uses the ID row of the user within the database
	// and overwrites the current details with the ones passed into this method
	public boolean updateRow(long rowId, String name, String mobilenumber, String emailaddress, String addresslineone, String addresslinetwo, String county, String postcode, String dob, String extra) {
		String findIdString = KEY_ROWID + "=" + rowId;

		ContentValues updateValue = new ContentValues();
		updateValue.put(KEY_NAME, name);
		updateValue.put(KEY_MOBILE_NUMBER, mobilenumber);
		updateValue.put(KEY_EMAIL_ADDRESS, emailaddress);
		updateValue.put(KEY_ADDRESS_LINE_ONE, addresslineone);
		updateValue.put(KEY_ADDRESS_LINE_TWO, addresslinetwo);
		updateValue.put(KEY_COUNTY, county);
		updateValue.put(KEY_POSTCODE, postcode);
		updateValue.put(KEY_DOB, dob);
		updateValue.put(KEY_EXTRA, dob);

		return sqlDatabase.update(DATABASE_TABLE, updateValue, findIdString, null) != 0;
	}
	
	// Creates an instance of the 'DatabaseContent' class
	private static class DatabaseHandler extends SQLiteOpenHelper {
		DatabaseHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		// The 'onCreate' method, that creates a new database if one has'nt been created already
		@Override
		public void onCreate(SQLiteDatabase _sqlDatabase) {
			_sqlDatabase.execSQL(DATABASE_CREATE_SQL);			
		}
	
	// If a database has already been created then the upgrade method gets called
	@Override
	public void onUpgrade(SQLiteDatabase _sqlDatabase, int oldVersion, int newVersion) {
		Log.w("DatabaseContent", "Upgrading application's database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data!");
		
		_sqlDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	
		onCreate(_sqlDatabase);
		}
	}
	
	// the 'setViewText' method that creates an object of a user that allows the users details of
	// the selected user
	public void setViewText(long _id, String _name, String _mobilenumber, String _emailaddress, String _addresslineone, String _addresslinetwo, String _county, String _postcode, String _dob, String _extra){
		viewid = _id;
		viewname = _name;
		viewmobilenumber = _mobilenumber;
		viewemailaddress = _emailaddress;
		viewaddresslineone = _addresslineone;
		viewaddresslinetwo = _addresslinetwo;
		viewcounty = _county;
		viewpostcode = _postcode;
		viewdob = _dob;
		viewextra = _extra;
	}
	
	// the 'getViewTextID' method that returns the viewID
	public Long getViewTextID(){
		return viewid;
	}
	
	// the 'getViewTextID' method that returns the viewID
	public String getViewTextName(){
		return viewname;
	}
	
	// the 'getViewTextMobileNumber' method that returns the mobile number of the user
	public String getViewTextMobileNumber(){
		return viewmobilenumber;
	}
	
	// the 'getViewTextEmailAddress' method that returns the email address  of the user
	public String getViewTextEmailAddress(){
		return viewemailaddress;
	}
	
	// the 'getViewTextAddressLineOne' method that returns the first address line of the user
	public String getViewTextAddressLineOne(){
		return viewaddresslineone;
	}
		
	// the 'getViewTextAddressLineTwo' method that returns the second address line of the user
	public String getViewTextAddressLineTwo(){
		return viewaddresslinetwo;
	}
	
	// the 'getViewTextAddressLineTwo' method that returns the postcode of the user
	public String getViewTextPostcode(){
		return viewpostcode;
	}
	
	// the 'getViewTextCounty' method that returns the the county of the user
	public String getViewTextCounty(){
		return viewcounty;
	}
	
	// the 'getViewTextDob' method that returns the Date Of Birth of the user
	public String getViewTextDob(){
		return viewdob;
	}
		
	// the 'getViewTextExtra' method that returns the extra text of the user
	public String getViewTextExtra(){
		return viewextra;
	}
}
