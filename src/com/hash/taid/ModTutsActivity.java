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

import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class ModTutsActivity extends TAidActivity {

	boolean isNew;
	String tutName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_tuts);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, -1);
		isNew = false;
		// User choose to add a tutorial.
		if (tId == -1) {
			clist.getCourse(cId).addTutorial(new Tutorial(""));
			tId = clist.getCourse(cId).numTuts() - 1; // Course to modify is the
														// last course
			isNew = true;
		}
		tutName = clist.getCourse(cId).getTutorial(tId).getName();
		((EditText) findViewById(R.id.tutEditText1)).setText(tutName);
	}

	public void saveButton(View view) {
		tutName = ((EditText) findViewById(R.id.tutEditText1)).getText()
				.toString();

		// Check if given tutorial already exists, or specified tutorial name is
		// empty.
		boolean isEmptyName = tutName.trim().equals("");
		boolean containsTut = clist.getCourse(cId).containsTutorial(tutName);
		if (isEmptyName || containsTut) {
			if (isEmptyName) // Do not allow empty course name.
				Toast.makeText(this, "Cannot create tutorial with no name.",
						Toast.LENGTH_SHORT).show();
			else
				// Do not allow duplicate names.
				Toast.makeText(this, "Tutorial with given name already exists.",
						Toast.LENGTH_SHORT).show();
			onBackPressed(); // Pop the newly added empty tutorial, if user was
								// adding a course.
			return;
		}

		clist.getCourse(cId).getTutorial(tId).setName(tutName);
		isNew = false;
		onBackPressed();
	}

	public void cancelButton(View view) {
		this.onBackPressed();
	}

	public void deleteButton(View view) {
		clist.getCourse(cId).popTut();
		isNew = false;
		onBackPressed();
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
		getMenuInflater().inflate(R.menu.mod_tuts, menu);
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

	// private void startPrevActivity(){
	// Intent intent = new Intent(ModTutsActivity.this, TutorialActivity.class);
	// intent.putExtra(modCourseTag, cId);
	// startActivity(intent);
	// }

	@Override
	public void onBackPressed() {
		if (isNew) {
			clist.getCourse(cId).popTut(tId);
		}
		Intent intent = new Intent(this, TutorialActivity.class);
		intent.putExtra(modCourseTag, cId);
		NavUtils.navigateUpTo(this, intent);
		finish();
	}

}
