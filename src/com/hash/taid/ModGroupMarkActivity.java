package com.hash.taid;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class ModGroupMarkActivity extends TAidActivity {

	String[] aNames;
	Spinner sp;
	TextView markText;
	TextView notifyModification;
	EditText markEdit;
	Tutorial tut;
	CheckBox ifUnmarked;
	String unmarkedText = "-";
	int[] markStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_group_mark);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();

		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		gId = intent.getStringExtra(modGroupTag);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		// Get assignment array. Should contain at least one assignment entry.
		aNames = clist.getCourse(cId).getTutorial(tId)
				.getGroupAssignmentNames();

		// Set adapter for spinner (The list of assignment names);
		ArrayAdapter<String> spAdpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, aNames);
		spAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Get the views from resource
		markText = (TextView) findViewById(R.id.markTextView);
		notifyModification = (TextView) findViewById(R.id.modNoticeTextView);
		markEdit = (EditText) findViewById(R.id.markEditText);
		sp = (Spinner) findViewById(R.id.assignmentSpinner);
		ifUnmarked = (CheckBox) findViewById(R.id.ifMarkedCheckBox);
		// Set adapter for spinner
		sp.setAdapter(spAdpt);
		notifyModification.setText("");
		// Initialize markStorage to -2 to signify that user has not specified
		// marks
		markStorage = new int[aNames.length];
		Arrays.fill(markStorage, -2);

		// Set spinner selection behavior
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				// Get the maximum mark of this group assignment.
				int maxMark = tut.getAssignment(
						tut.getAssignmentIndex(aNames[position])).getMaxMark();
				markText.setText("/" + maxMark);
				if (markStorage[position] == -1) {
					// User has set this assignment as unmarked.
					markEdit.setEnabled(false);
					markEdit.setText(unmarkedText);
					ifUnmarked.setChecked(true);
				} else if (markStorage[position] == -2) {
					// User has not made modifications to this assignment.
					markEdit.setEnabled(false);
					markEdit.setText(unmarkedText);
					ifUnmarked.setChecked(true);
				} else {
					// User has made modifications, and we display the
					// modification.
					markEdit.setEnabled(true);
					markEdit.setText(Integer.toString(markStorage[position]));
					ifUnmarked.setChecked(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				markEdit.setEnabled(false);
				markEdit.setText("N/A");
				markText.setText("/ N/A");
			}
		});// OnItemSelectedListener set.

		markEdit.addTextChangedListener(new TextWatcher() {
			// modifies markStorage when user modifies the edit test.
			public void afterTextChanged(Editable s) {
				if (markEdit.isEnabled() && s.length() > 0)
					// Only make the move if edit text field is enabled.
					markStorage[sp.getSelectedItemPosition()] = Integer
							.parseInt(s.toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});// Text watcher attached.

		// Set check box event listener
		ifUnmarked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				notifyModification
						.setText("Changes have been made. Pressing save will store changes.");
				int position = sp.getSelectedItemPosition();
				if (ifUnmarked.isChecked()) { // Set unmarked
					// ifUnmarked.setChecked(true);
					markStorage[position] = -1;
					markEdit.setEnabled(false);
					markEdit.setText(unmarkedText);
				} else {
					// ifUnmarked.setChecked(false);
					markStorage[position] = 0;
					markEdit.setText("");
					markEdit.setEnabled(true);
				}
			}

		});
	}

	public void saveButton(View view) {
		// Go through mark storage, and store modified changes. Ignore -2's.
		for (int i = 0; i < markStorage.length; i++) {
			if (markStorage[i] == -2)
				continue;
			tut.changeGroupMark(gId, markStorage[i], aNames[i]);
		}
		this.onBackPressed();
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
		getMenuInflater().inflate(R.menu.mod_group_mark, menu);
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
		Intent intent = new Intent(this, TabStuActivity.class);
		intent.putExtra(modCourseTag, cId);
		intent.putExtra(modTutorialTag, tId);
		intent.putExtra(startAtTabTag, tabId);
		NavUtils.navigateUpTo(this, intent);;
		finish();
	}

}
