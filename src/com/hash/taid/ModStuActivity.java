package com.hash.taid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hash.core.Student;
import com.hash.taid.customs.MultiSelectionSpinner;
import com.hash.taid.customs.TAidActivity;

public class ModStuActivity extends TAidActivity {

	Student s; // An student object to hold the information of the old student
				// going
				// under modification.
	String[] sessionIds;
	MultiSelectionSpinner sp;
	boolean[] stuAttendance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_stu);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		sId = intent.getIntExtra(modStuTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);

		s = sbank.getStudent(sId);
		sessionIds = clist.getCourse(cId).getTutorial(tId).getSessionIds();
		stuAttendance = clist.getCourse(cId).getTutorial(tId)
				.getAttendanceArray(sId);

		// set the edittext elements.
		((EditText) findViewById(R.id.stuNameEditTxt)).setText(s.getName());
		((EditText) findViewById(R.id.stuEmailEditTxt)).setText(s.getEmail());
		((EditText) findViewById(R.id.stuIdEditTxt)).setText(((Integer) s
				.getStudentNum()).toString());
		sp = (MultiSelectionSpinner) findViewById(R.id.attendanceSpinner);
		sp.setItems(sessionIds);
		ArrayList<Integer> selectedIndicies = new ArrayList<Integer>();
		for (int i = 0; i < stuAttendance.length; i++)
			if (stuAttendance[i])
				selectedIndicies.add(i);
		int[] selected = new int[selectedIndicies.size()];
		for (int i = 0; i < selected.length; i++)
			selected[i] = selectedIndicies.get(i);
		sp.setSelection(selected);

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
		List<Integer> selectedIndicies = sp.getSelectedIndicies();

		if (newStuNumStr.equals(""))
			newStuNum = -1;
		else
			newStuNum = Integer.parseInt(newStuNumStr);

		String toastMsg = ""; // Message to alert user with.

		if (newStuNum == -1) {
			// User decides to modify a student to have no student number.
			// The user is a horrible person.
			Toast.makeText(this,
					"Cannot have a student with no student number.",
					Toast.LENGTH_LONG).show();
			this.cancelButton(view);
			return;
		}

		Student newS = new Student(newName, newEmail, newStuNum);
		// Modify and get result of modifying entry in student bank.
		int modBankResult = sbank.modify(s, newS);
		// Modify student number in tutorial from s to newS.
		boolean tutStuModified = clist.getCourse(cId).getTutorial(tId)
				.modifyStudent(s, newS);

		if (tutStuModified) {
			// Generate boolean array to set as attendance.
			boolean[] aList = new boolean[clist.getCourse(cId).getTutorial(tId)
					.getNumTotalSessions()];
			Arrays.fill(aList, false);
			for (Integer i : selectedIndicies)
				aList[i] = true;
			clist.getCourse(cId).getTutorial(tId)
					.setAttendance(newS.getStudentNum(), aList);
		}

		switch (modBankResult) {
		case -1: // Should not happen, since s should be in bank.
			toastMsg = "You broke my app D:";
			break;
		case 0: // Student id of newS is already in bank. Cannot add.
			toastMsg = "Student already exists in database. ";
			break;
		case 1: // Stu id of s and newS are same. Modify s in sbank to have same
				// credentials as newS.
			if (!s.getName().equals(newS.getName())
					|| !s.getEmail().equals(newS.getEmail()))
				// Do not display notification if user is only modifying
				// attendance.
				toastMsg = "Modified student in database. ";
			break;
		default: // Stu id of newS is not in bank. Simply add newS to bank.
			toastMsg = "Added new student to database. ";
		}
		toastMsg += "Modified student in tutorial.";

		Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
		// this.startPrevActivity();

		this.onBackPressed();
	}

	public void cancelButton(View view) {
		this.onBackPressed();
	}

	public void deleteButton(View view) {
		String stuGroupName = clist.getCourse(cId).getTutorial(tId)
				.getStudentGroupName(s.getStudentNum());
		int deleteResult = clist.getCourse(cId).getTutorial(tId)
				.removeStudentId(s.getStudentNum());
		// this.startPrevActivity();
		switch (deleteResult) {
		case 1:
			Toast.makeText(this, "Deleted student.", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(
					this,
					"Deleted student. Group \"" + stuGroupName
							+ "\" has been removed.", Toast.LENGTH_LONG).show();
			break;
		case -1:
			Log.e("com.hash.taid", "YOU DUN GOOFED.");
		}
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
		getMenuInflater().inflate(R.menu.mod_stu, menu);
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
	// intent.putExtra(modCourseTag, cId);
	// intent.putExtra(modTutorialTag, tId);
	// startActivity(intent);
	// }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, TabStuActivity.class);
		intent.putExtra(modCourseTag, cId);
		intent.putExtra(modTutorialTag, tId);
		intent.putExtra(startAtTabTag, tabId);
		NavUtils.navigateUpTo(this, intent);
		;
		finish();
	}

}
