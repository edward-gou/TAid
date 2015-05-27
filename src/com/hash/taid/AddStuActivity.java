package com.hash.taid;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hash.taid.customs.TAidActivity;

public class AddStuActivity extends TAidActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stu);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);
	}

	public void saveButton(View view) {
		// Get the content currently inside the edittext elements, and store it.
		// The below three stores the contents as given by the user.
		String newName = ((EditText) findViewById(R.id.stuNameEditTxt))
				.getText().toString();
		String newEmail = ((EditText) findViewById(R.id.stuEmailEditTxt))
				.getText().toString();
		String newStuNumStr = ((EditText) findViewById(R.id.stuIdEditTxt))
				.getText().toString();
		int newStuNum;
		String toastMsg = "";

		if (newStuNumStr.equals(""))
			newStuNum = -1;
		else
			newStuNum = Integer.parseInt(newStuNumStr);

		// Check if use entered empty student number
		if (newStuNum == -1) {
			Toast.makeText(this, "Cannot add student with no student number.",
					Toast.LENGTH_LONG).show();
			this.cancelButton(view);
			return;
		}

		// Check if student already exists in bank
		boolean ifNotExists = sbank.addStudent(newName, newEmail, newStuNum);
		if (ifNotExists) {
			// If student is not in bank, and name is not specified, do not add
			// the student. (Does not allow adding of empty student names)
			if (newName.trim().equals("")) {
				Toast.makeText(this, "Cannot add student with no name.",
						Toast.LENGTH_LONG).show();
				sbank.removeStudent(newStuNum); //Remove the student we've just added.
				this.cancelButton(view);
				return;
			}
			toastMsg += "Added student to database. ";
		} else 
			toastMsg += "Student already exists in database. ";
		
		// Check if student already exists in tutorial
		if (clist.getCourse(cId).getTutorial(tId).addStudentId(newStuNum))
			toastMsg += "Student added to this tutorial.";
		else
			toastMsg += "Student already exists in this tutorial.";

		// Show user what occurred.
		Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
		// this.startPrevActivity();

		this.onBackPressed();
	}

	public void cancelButton(View view) {
		this.onBackPressed();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_stu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			this.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// private void startPrevActivity() {
	// Intent intent = new Intent(this, TabStuActivity.class);
	// intent.putExtra(MainActivity.modCourseTag, cId);
	// intent.putExtra(MainActivity.modTutorialTag, tId);
	// startActivity(intent);
	// }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, TabStuActivity.class);
		intent.putExtra(modCourseTag, cId);
		intent.putExtra(modTutorialTag, tId);
		intent.putExtra(startAtTabTag, tabId);
		NavUtils.navigateUpTo(this, intent);;
		finish();
	}

}
