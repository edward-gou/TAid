package com.hash.taid;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hash.core.Tutorial;
import com.hash.taid.R;
import com.hash.taid.customs.EmptyTextButtonDisabler;
import com.hash.taid.customs.TAidActivity;

public class ModAssignmentActivity extends TAidActivity {

	EditText aName, maxMark;
	CheckBox isGroup;
	String oldAName;
	Button saveButton;
	Tutorial tut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_assignment);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		aId = intent.getIntExtra(modAsmtTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		aName = (EditText) findViewById(R.id.aNameEditText);
		maxMark = (EditText) findViewById(R.id.aMaxMarkEditText);
		saveButton = (Button) findViewById(R.id.saveButton);
		isGroup = (CheckBox) findViewById(R.id.isGroupCheckBox);

		// Set the fields to the assignment's info.
		oldAName = tut.getAssignment(aId).getName();
		aName.setText(oldAName);
		maxMark.setText(Integer.toString(tut.getAssignment(aId).getMaxMark()));
		isGroup.setChecked(tut.getAssignment(aId).isGroupAssignment());

		// Attach the text watcher
		boolean[] allFilled = new boolean[2];
		Arrays.fill(allFilled, true);
		aName.addTextChangedListener(new EmptyTextButtonDisabler(saveButton,
				allFilled, 0));
		maxMark.addTextChangedListener(new EmptyTextButtonDisabler(saveButton,
				allFilled, 1));
	}

	public void saveButton(View view) {
		// Get info from edit text fields.
		String newAName = aName.getText().toString();
		int newMaxMark = Integer.parseInt(maxMark.getText().toString());
		// Do not allow 0 as max mark.
		if (newMaxMark == 0) {
			Toast.makeText(this, "Cannot set max mark as 0.",
					Toast.LENGTH_SHORT).show();
			onBackPressed();
			return;
		}
		// Don't save if new assignment name already exists, and is not the same
		// as old.
		if (tut.containsAssignment(newAName) && !oldAName.equals(newAName))
			Toast.makeText(this, "Assignment already exists.",
					Toast.LENGTH_SHORT).show();
		else {
			tut.getAssignment(aId).setName(newAName);
			tut.getAssignment(aId).setMaxMark(newMaxMark);
			tut.getAssignment(aId).setIsGroup(isGroup.isChecked());
			Toast.makeText(this, "Assignment modified.", Toast.LENGTH_SHORT)
					.show();
		}
		onBackPressed();
	}

	public void deleteButton(View view) {
		tut.delAssignment(aId);
		Toast.makeText(this, "Assignment deleted.", Toast.LENGTH_SHORT).show();
		onBackPressed();
	}

	public void cancelButton(View view) {
		onBackPressed();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mod_assignment, menu);
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
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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
