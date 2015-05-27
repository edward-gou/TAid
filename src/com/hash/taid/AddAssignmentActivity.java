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

public class AddAssignmentActivity extends TAidActivity {

	EditText aName, maxMark;
	Button saveButton;
	CheckBox isGroup;
	Tutorial tut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		// Get the view elements.
		aName = (EditText) findViewById(R.id.aNameEditText);
		maxMark = (EditText) findViewById(R.id.aMaxMarkEditText);
		saveButton = (Button) findViewById(R.id.saveButton);
		isGroup = (CheckBox) findViewById(R.id.isGroupCheckBox);

		// Attach text watchers
		boolean[] allFilled = new boolean[2];
		Arrays.fill(allFilled, false);
		aName.addTextChangedListener(new EmptyTextButtonDisabler(saveButton, allFilled, 0));
		maxMark.addTextChangedListener(new EmptyTextButtonDisabler(saveButton, allFilled, 1));
		saveButton.setEnabled(false);
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

		// Don't save if assignment name already exists..
		if (tut.addAssignment(newAName, newMaxMark, isGroup.isChecked()) > 0)
			Toast.makeText(this, "Assignment added.", Toast.LENGTH_SHORT)
					.show();
		else
			Toast.makeText(this, "Assignment already exists.",
					Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.add_assignment, menu);
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
