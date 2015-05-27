package com.hash.taid;

import java.util.ArrayList;

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

import com.hash.core.Course;
import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class ModCoursesActivity extends TAidActivity {

	boolean isNew; // Whether a new course is created.
	String courseName;
	String instrEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_courses);
		// Show the Up button in the action bar.
		setupActionBar();

		// Get information passed from intent.
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, -1);
		// -1 => to create an instance
		if (cId == -1) {
			Course newCourse = new Course("", "", new ArrayList<Tutorial>());
			clist.addCourse(newCourse);
			cId = clist.numCourses() - 1; // Course to modify is the last course
			isNew = true;
		}
		// Initialize edittest elements with course info.
		courseName = clist.getCourse(cId).getCourseName();
		instrEmail = clist.getCourse(cId).getInstructorEmail();
		((EditText) findViewById(R.id.editText1)).setText(courseName);
		((EditText) findViewById(R.id.editText2)).setText(instrEmail);

	}

	public void saveButton(View view) {
		// Actions to take when save button is pressed.
		// Get content from edittext elements.
		courseName = ((EditText) findViewById(R.id.editText1)).getText()
				.toString();
		String newInstrEmail = ((EditText) findViewById(R.id.editText2)).getText()
				.toString();

		// Check if given course already exists, if specified course name is
		// empty, and if user changed the email.
		boolean ifSameEmail = instrEmail.equals(newInstrEmail);
		boolean isEmptyName = courseName.trim().equals("");
		boolean containsCourse = clist.containsCourse(courseName);
		if (isEmptyName || (containsCourse && ifSameEmail)) {
			if (isEmptyName) // Do not allow empty course name.
				Toast.makeText(this, "Cannot create course with no name.",
						Toast.LENGTH_SHORT).show();
			else
				// Do not allow duplicate names.
				Toast.makeText(this, "Course with given name already exists.",
						Toast.LENGTH_SHORT).show();
			onBackPressed(); // Pop the newly added empty course, if user was
								// adding a course.
			return;
		}
		clist.getCourse(cId).setCourseName(courseName);
		clist.getCourse(cId).setInstrEmail(newInstrEmail);
		isNew = false;
		onBackPressed();
	}

	public void cancelButton(View view) {
		// Actions to take when cancel button is pressed.
		// If an empty course was appended since user wanted to add a course,
		// pop it.
		onBackPressed();
	}

	public void deleteButton(View view) {
		// Actions to take when delete button is pressed.
		clist.popCourse(cId);
		isNew = false;
		onBackPressed();
	}

	// private void startPrevActivity(){
	// Intent intent = new Intent(ModCoursesActivity.this, MainActivity.class);
	// startActivity(intent);
	// }

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
		getMenuInflater().inflate(R.menu.mod_courses, menu);
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

	@Override
	public void onBackPressed() {
		if (isNew) {
			clist.popCourse();
		}
		NavUtils.navigateUpFromSameTask(this);
		finish();
	}

}
